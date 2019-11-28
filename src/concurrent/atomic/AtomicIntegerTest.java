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
    public static void main(String[] args) {
        AtomicInteger ai = new AtomicInteger(0);

        /**
         * 原子方式自增1
         */
        ai.getAndIncrement();
        System.out.println(ai.get());

        /**
         * 原子方式设置ai的value为10
         */
        ai.getAndSet(10);
        System.out.println(ai.get());

        /**
         * 比较并设置
         * 如果与期望值(10)相同，则原子方式设置ai的value为20
         * 此方法使用较多，可以用来做锁的持有状态判断
         */
        ai.compareAndSet(10, 20);
        System.out.println(ai.get());
    }
}
