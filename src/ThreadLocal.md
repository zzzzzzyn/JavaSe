以下为ThreadLocal的常用方法

```java
/**
 * 初始化value为null，被get调用
 */
protected T initialValue() {
    return null;
}

/**
 * 获取ThreadLocalMap中的值，如果没有初始化，则返回setInitialValue的值
 */
public T get() {
    // 获取当前线程
    Thread t = Thread.currentThread();
    // 获取当前线程属性ThreadLocalMap
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        // 获取entry
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T) e.value;
            return result;
        }
    }
    return setInitialValue();
}

// 获取线程的threadLocals属性
ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}
```



