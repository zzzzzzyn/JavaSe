package concurrent.lock.countdownlatch;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author xyn
 */
public class CountDownLatch {
    /**
     * CountDownLatch的同步控件，继承的aqs用state进行计数
     */
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;

        Sync(int count) {
            setState(count);
        }

        int getCount() {
            return getState();
        }

        /**
         * 尝试共享式获取
         */
        protected int tryAcquireShared(int acquires) {
            return (getState() == 0) ? 1 : -1;
        }

        /**
         * 返回值:
         * true: nextc==0 时
         * false: c==0 --> 初始化时c为0
         */
        protected boolean tryReleaseShared(int releases) {
            // 递减计数，到0发出信息
            for (; ; ) {
                int c = getState();
                if (c == 0)
                    return false;

                int nextc = c - 1;
                if (compareAndSetState(c, nextc))
                    return nextc == 0;
            }
        }
    }

    // sync被final修饰，初始化后不可再改变，当然也固定了内部计数器的值
    private final Sync sync;

    /**
     * 构造给定count的锁存器
     *
     * @param count 线程通过前CountDown必须被调用的次数
     * @throws IllegalArgumentException count<0
     */
    public CountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.sync = new Sync(count);
    }

    /**
     * 使当前线程等待，直至计数器为0为止，除非当前线程中断
     * 当前计数器为0时立即返回
     *
     * @throws InterruptedException 当前线程等待时被中断
     */
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    /**
     * @throws InterruptedException if the current thread is interrupted
     *                              while waiting
     */
    public boolean await(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    /**
     * 减少锁存器的计数，如果计数达到零，则释放所有等待线程。
     * 这主要的思想就是给定了state值，调用一次就去减少一次，直到state为0
     * 而await则是获取同步状态，都在tryAcquireShared方法处做了限制
     */
    public void countDown() {
        sync.releaseShared(1);
    }

    /**
     * 当前返回计数
     */
    public long getCount() {
        return sync.getCount();
    }

    /**
     * Returns a string identifying this latch, as well as its state.
     * The state, in brackets, includes the String {@code "Count ="}
     * followed by the current count.
     *
     * @return a string identifying this latch, as well as its state
     */
    public String toString() {
        return super.toString() + "[Count = " + sync.getCount() + "]";
    }
}

