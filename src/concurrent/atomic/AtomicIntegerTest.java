package concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * atomic包下的原子操作类有四种原子更新方式:
 * 1. 原子更新基本类型
 * 2. 原子更新数组
 * 3. 原子更新引用
 * 4. 原子更新字段(属性)
 *
 * @author xyn
 * @description 原子操作类: 通过源码可知这些类基本都是Unsafe实现的包装类
 * @data 2019/11/28 16:07
 */
public class AtomicIntegerTest {
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {


    }


    /* 加锁 */
    private void lock() {
        for (; ; ) {
            /* 大名鼎鼎的cas，线程获取锁就是用它来做的 */
            if (atomicInteger.get() == 0)
                return;


        }
    }

    private void unlock() {
        atomicInteger.set(0);
    }
}
