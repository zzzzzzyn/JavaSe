### BlockingQueue

BlockingQueue是支持两个附加操作的队列

- 阻塞地添加(队列满时，会阻塞进行插入的线程，直至队列不满)
- 阻塞地移除(队列为空时，会阻塞进行移除的线程，直至队列不空)

|   方法   |  array   |  queue   | blocking |
| :------: | :------: | :------: | :------: |
| 插入方法 |  add(e)  | offer(e) |  put(e)  |
| 移除方法 | remove() |  poll()  |  take()  |

常用于生产消费场景，更复杂一些就需要考虑使用mq了

### BlockingQueue的实现类

- ArrayBlockingQueue：数组结构，有界阻塞队列 ---> **常用**

- LinkedBlockingQueue：链表结构，有界阻塞队列 ---> **常用**
- LinkedTransferQueue：链表结构，无界阻塞队列 
- LinkedBlockingDeque：链表结构，双向阻塞队列
- PriorityBlockingQueue：优先级排序无界阻塞队列

- DelayQueue：使用优先级队列实现的无界阻塞队列 

- SynchronousQueue：一个不存储元素的阻塞队列 ---> 单进单出，没有进入则一直阻塞

**当队列为无界阻塞队列时，队列永远不会满(应考虑信息过多会不会干崩系统)，添加方法会永远成功**

### ArrayBlockingQueue方法

下面是有界阻塞队列ArrayBlockingQueue的添加和删除的方法

```java
/**
 * 添加(实际是调用offer(e)方法) --->array
 * 若添加失败，抛出异常
 * 
 * @param e
 * @return boolean
 * @throws IllegalStateException
 */
public boolean add(E e) {
    if (offer(e))
        return true;
    else
        throw new IllegalStateException("Queue full");
}

/**
 * 安全(上锁)移除元素o ---> array
 *
 * @param o
 * @return boolean
 */
public boolean remove(Object o) {
    if (o == null) return false;
    final Object[] items = this.items;
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        if (count > 0) {
            final int putIndex = this.putIndex;
            int i = takeIndex;
            do {
                if (o.equals(items[i])) {
                    removeAt(i);
                    return true;
                }
                if (++i == items.length)
                    i = 0;
            } while (i != putIndex);
        }
        return false;
    } finally {
        lock.unlock();
    }
}

/**
 * 安全(上锁)加入元素e ---> queue
 *
 * @param e
 * @return boolean
 */
public boolean offer(E e) {
    // 非空校验
    checkNotNull(e);
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        if (count == items.length)
            // 队列满
            return false;
        else {
            // 加入
            enqueue(e);
            return true;
        }
    } finally {
        lock.unlock();
    }
}

/**
 * 安全(上锁)移除 ---> queue
 *
 * @return E
 */
public E poll() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        return (count == 0) ? null : dequeue();
    } finally {
        lock.unlock();
    }
}

/**
 * 阻塞添加 ---> blocking
 *
 * @param e
 * @throws InterruptedException
 */
public void put(E e) throws InterruptedException {
    checkNotNull(e);
    final ReentrantLock lock = this.lock;
    // 加可响应中断锁
    lock.lockInterruptibly();
    try {
        while (count == items.length)
            notFull.await();    // notFull 等待
        enqueue(e);
    } finally {
        lock.unlock();
    }
}

/**
 * 阻塞移除 ---> blocking
 *
 * @return E
 * @throws InterruptedException
 */
public E take() throws InterruptedException {
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
        while (count == 0)
            notEmpty.await();
        return dequeue();
    } finally {
        lock.unlock();
    }
}

private E dequeue() {
    final Object[] items = this.items;
    @SuppressWarnings("unchecked")
    E x = (E) items[takeIndex];
    // takeIndex --> 下一个移除的索引
    items[takeIndex] = null;
    if (++takeIndex == items.length)
        takeIndex = 0;
    count--;
    if (itrs != null)
        itrs.elementDequeued();
    // notFull的唤醒
    notFull.signal();
    return x;
}


private void enqueue(E x) {
    final Object[] items = this.items;
    // putIndex --> 下一次要放入元素位置索引
    items[putIndex] = x;
    if (++putIndex == items.length)
        putIndex = 0;
    count++;
    // notEmpty的唤醒
    notEmpty.signal();
}
```

