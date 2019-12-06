package concurrent.lock;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Lock的实现，可重入锁
 *
 * @author xyn
 */
public class ReentrantLock implements Lock, java.io.Serializable {
    private static final long serialVersionUID = 7373984872572414699L;
    /** Synchronizer providing all implementation mechanics */
    private final ReentrantLock.Sync sync;

    /**
     * 实现锁的同步控制，有公平锁和非公平锁两种实现，所以此内部类是抽象的
     */
    abstract static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -5179523762034025860L;

        /**
         * 加锁(由子类重写)
         */
        abstract void lock();

        /**
         * 非公平获取同步状态
         *
         * @param acquires
         * @return boolean
         */
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                // c为同步状态=0说明锁没有被任何线程持有
                if (compareAndSetState(0, acquires)) {
                    // 原子设置成功后，设置当前线程为锁的独占持有者并返回
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                /**
                 * 此线程是重进入
                 * nextc为下一次设置的同步状态
                 * 因为此线程是锁的持有者，所以设置同步状态不需要使用原子方式
                 */
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            // 获取失败，返回
            return false;
        }

        /**
         * 独占释放同步状态
         *
         * @param releases
         * @return boolean
         */
        protected final boolean tryRelease(int releases) {
            // 一般情况下release为1，持有锁的线程每一次离开，都会同步状态都会-1
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            // free: 可以理解为锁是否被释放
            boolean free = false;
            if (c == 0) {
                /**
                 * c=0说明锁被释放，此时需要：
                 *      1. 设置free为true
                 *      2. 设置锁的持有者为null
                 */
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }

        /**
         * 同步器是否被当前线程独占
         *
         * @return boolean
         */
        protected final boolean isHeldExclusively() {
            // While we must in general read state before owner,
            // we don't need to do so to check if current thread is owner
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        // Methods relayed from outer class
        // 从外部类继承来的方法

        /**
         * 获取锁的持有者
         *
         * @return Thread
         */
        final Thread getOwner() {
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }

        /**
         * 获取计数器
         *
         * @return int
         */
        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }

        /**
         * 锁是否被持有
         */
        final boolean isLocked() {
            return getState() != 0;
        }

        /**
         * Reconstitutes the instance from a stream (that is, deserializes it).
         */
        private void readObject(java.io.ObjectInputStream s)
                throws java.io.IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }
    }

    /**
     * Sync对象的非公平锁实现
     */
    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = 7316153563782823691L;

        final void lock() {
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }

    /**
     * Sync对象的公平锁实现
     */
    static final class FairSync extends Sync {
        private static final long serialVersionUID = -3000897897090466540L;

        final void lock() {
            acquire(1);
        }

        /**
         * 尝试获取同步状态(公平锁)
         *
         * @param acquires
         * @return boolean
         */
        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                // 检查同步队列中是否有比自己等待时间更长的线程并尝试原子设置同步状态
                if (!hasQueuedPredecessors() &&
                        compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }

    /**
     * 构造，默认非公平锁实现
     */
    public ReentrantLock() {
        sync = new NonfairSync();
    }

    /**
     * 构造
     *
     * @param fair 根据fair来决定锁的公平和非公平实现
     */
    public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }

    /**
     * 获取锁：
     *   1. 获取成功：
     *      1.1 锁原来持有者是自己，计数器+1，返回
     *      1.2 锁原来持有者不是自己，设置计数器为1，返回
     *   2. 获取失败，休眠等待，等持有后，设置计数器为1，返回
     */
    public void lock() {
        sync.lock();
    }

    /**
     * 可中断地获取锁
     *
     * @throws InterruptedException 若当前线程被中断
     */
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    /**
     * 非阻塞地获取锁
     *   1. 获取成功
     *      1.1 锁原来持有者是自己，计数器+1，返回
     *      1.2 锁原来持有者不是自己，设置计数器为1，返回
     *   2. 获取失败，返回
     */
    public boolean tryLock() {
        return sync.nonfairTryAcquire(1);
    }

    /**
     * 尝试获取锁(超时等待且可响应中断)
     *
     * @param timeout the time to wait for the lock
     * @param unit the time unit of the timeout argument
     * @return {@code true} if the lock was free and was acquired by the
     *         current thread, or the lock was already held by the current
     *         thread; and {@code false} if the waiting time elapsed before
     *         the lock could be acquired
     * @throws InterruptedException if the current thread is interrupted
     * @throws NullPointerException if the time unit is null
     */
    public boolean tryLock(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

    /**
     * 尝试释放锁
     *
     * 若当前线程是锁的持有者，计数器-1，此时计数器若为0，释放锁，
     * 若当前线程不是锁持有者会判处异常(一般不会出现)
     * 使用时，应将unlock放入finally中，因为可能会抛出异常
     *
     * @throws IllegalMonitorStateException 当前线程未持有锁
     */
    public void unlock() {
        sync.release(1);
    }

    /**
     * 使用的前提是获得锁，一般用来作生产消费模型
     * 和Object的wait()，notify()相对应
     * 方法为: await()，signal()
     *
     * @return Condition
     */
    public Condition newCondition() {
        return sync.newCondition();
    }

    /**
     * 当前锁的持有次数
     *
     * @return int 若锁未被持有，返回0，若被持有，返回计数器
     */
    public int getHoldCount() {
        return sync.getHoldCount();
    }

    /**
     * 查询线程所有者是否为此线程
     *
     * @return boolean
     */
    public boolean isHeldByCurrentThread() {
        return sync.isHeldExclusively();
    }

    /**
     * 查看锁是否被线程持有
     *
     * @return boolean
     */
    public boolean isLocked() {
        return sync.isLocked();
    }

    /**
     * 是否公平锁
     *
     * @return boolean
     */
    public final boolean isFair() {
        return sync instanceof FairSync;
    }

    /**
     * 获取锁的持有者
     *
     * @return Thread
     */
    protected Thread getOwner() {
        return sync.getOwner();
    }

    /**
     * 队列(aqs中实现)中是否有线程等待获取锁
     *
     * @return boolean
     */
    public final boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    /**
     * 查看thread是否在队列中等待锁
     *
     * @param thread the thread
     * @return {@code true} if the given thread is queued waiting for this lock
     * @throws NullPointerException if the thread is null
     */
    public final boolean hasQueuedThread(Thread thread) {
        return sync.isQueued(thread);
    }

    /**
     * 获取队列的长度(即等待锁的线程数量)
     *
     * @return the estimated number of threads waiting for this lock
     */
    public final int getQueueLength() {
        return sync.getQueueLength();
    }

    /**
     * 返回队列中等待该锁的线程的集合
     *
     * @return Collection<Thread> the collection of threads
     */
    protected Collection<Thread> getQueuedThreads() {
        return sync.getQueuedThreads();
    }

    /**
     * 是否有线程等待condition
     *
     * @param condition the condition
     * @return {@code true} if there are any waiting threads
     * @throws IllegalMonitorStateException if this lock is not held
     * @throws IllegalArgumentException if the given condition is
     *         not associated with this lock
     * @throws NullPointerException if the condition is null
     */
    public boolean hasWaiters(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.hasWaiters((AbstractQueuedSynchronizer.ConditionObject)condition);
    }

    /**
     * Returns an estimate of the number of threads waiting on the
     * given condition associated with this lock. Note that because
     * timeouts and interrupts may occur at any time, the estimate
     * serves only as an upper bound on the actual number of waiters.
     * This method is designed for use in monitoring of the system
     * state, not for synchronization control.
     *
     * @param condition the condition
     * @return the estimated number of waiting threads
     * @throws IllegalMonitorStateException if this lock is not held
     * @throws IllegalArgumentException if the given condition is
     *         not associated with this lock
     * @throws NullPointerException if the condition is null
     */
    public int getWaitQueueLength(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitQueueLength((AbstractQueuedSynchronizer.ConditionObject)condition);
    }

    /**
     * Returns a collection containing those threads that may be
     * waiting on the given condition associated with this lock.
     * Because the actual set of threads may change dynamically while
     * constructing this result, the returned collection is only a
     * best-effort estimate. The elements of the returned collection
     * are in no particular order.  This method is designed to
     * facilitate construction of subclasses that provide more
     * extensive condition monitoring facilities.
     *
     * @param condition the condition
     * @return the collection of threads
     * @throws IllegalMonitorStateException if this lock is not held
     * @throws IllegalArgumentException if the given condition is
     *         not associated with this lock
     * @throws NullPointerException if the condition is null
     */
    protected Collection<Thread> getWaitingThreads(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitingThreads((AbstractQueuedSynchronizer.ConditionObject)condition);
    }

    /**
     * Returns a string identifying this lock, as well as its lock state.
     * The state, in brackets, includes either the String {@code "Unlocked"}
     * or the String {@code "Locked by"} followed by the
     * {@linkplain Thread#getName name} of the owning thread.
     *
     * @return a string identifying this lock, as well as its lock state
     */
    public String toString() {
        Thread o = sync.getOwner();
        return super.toString() + ((o == null) ?
                "[Unlocked]" :
                "[Locked by thread " + o.getName() + "]");
    }
}

