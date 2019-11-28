package concurrent.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 原子更新数组
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/28 21:18
 */
public class AtomicIntegerArrayTest {
    public static void main(String[] args) {
        AtomicIntegerArray aia = new AtomicIntegerArray(new int[]{1, 2, 3, 4, 5, 6});

        /**
         * 如果i索引位置的值与预期值except相等，能原子更新i位置的值为update
         */
        aia.compareAndSet(0,1,5);
        System.out.println(aia.get(0));

        /**
         * 原子方式使i位置值自增1
         */
        aia.getAndIncrement(0);
        System.out.println(aia.get(0));

        /**
         * 获取i位置的值并以原子方式设置为新值newValue
         */
        aia.getAndSet(0, 10);
        System.out.println(aia.get(0));
    }
}
