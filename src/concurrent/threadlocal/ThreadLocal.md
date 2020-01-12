##### ThreadLocal

ThreadLocal对外提供了三个公共方法

- set()
- get()
- remove()

```java
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
}

public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    return setInitialValue();
}

// 线程池条件下必须手动清理threadlocal
public void remove() {
    ThreadLocalMap m = getMap(Thread.currentThread());
    if (m != null)
        m.remove(this);
}
```



Thread，ThreadLocal，ThreadLocalMap总览图

<img style="center" src="http://www.jiangxinlingdu.com/assets/images/2019/2019061906258.png">