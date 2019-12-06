## ReentrantLock
ReentrantLock: 向外部提供可供使用的锁，锁内部实现则是调用了继承AQS的内部类sync来完成的锁是面向使用者，而sync主要面向锁的实现，所以学习锁的重点应该是AQS的实现。

### 面试题
#### synchronize和ReentrantLock的异同
|     特性     | synchronize | ReentrantLock |
| :----------: | :---------: | :-----------: |
|    可重入    |      √      |       ×       |
|    公平锁    |      ×      |       √       |
|   非公平锁   |      √      |       √       |
|   超时等待   |      ×      |       √       |
|   尝试加锁   |      ×      |       √       |
|   Java特性   |      √      |       ×       |
|   响应中断   |      ×      |       √       |
|  自动释放锁  |      √      |       ×       |
| 对异常的处理 | 自动释放锁  |  手动释放锁   |

#### 公平锁和非公平锁实现及异同
默认实现的是非公平锁，可以通过构造方法```ReentrantLock(true)```来实现公平锁

**公平锁**

若同步队列中没有比自己等待时间更长的线程```!hasQueuedPredecessors()```才会尝试抢占设置同步状态这是实现公平锁的关键，如果没有```!hasQueuedPredecessors()```判断，就会直接尝试抢占设置同步状态就变成了非公平锁
```java
final void lock() {
    acquire(1);
}

public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}

protected final boolean tryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
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
```
**非公平锁**

先尝试抢占式设置同步状态，失败后加入到同步队列尾部
```java
final void lock() {
    if (compareAndSetState(0, 1))
        setExclusiveOwnerThread(Thread.currentThread());
    else
        acquire(1);
}

public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}

protected final boolean tryAcquire(int acquires) {
    return nonfairTryAcquire(acquires);
}

final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```


> 参考: [田小波技术博客](http://www.tianxiaobo.com/2018/05/07/Java-%E9%87%8D%E5%85%A5%E9%94%81-ReentrantLock-%E5%8E%9F%E7%90%86%E5%88%86%E6%9E%90/)