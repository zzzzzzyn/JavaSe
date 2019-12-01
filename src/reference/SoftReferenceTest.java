package reference;


import java.lang.ref.SoftReference;

/**
 * 软引用:
 * 内存足够时不会被回收
 * 内存不足时才会被回收
 *
 * 我们来设置初始堆内存和最大堆内存来验证
 * -Xms20m -Xmx20m -XX:+PrintGC
 *
 * 用处：做缓存
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/12/01 22:14
 */
public class SoftReferenceTest {
    public static void main(String[] args) {
        SoftReference<Object> sr = new SoftReference<Object>(new Object());
        if (sr.get() != null) {
            System.out.println("not null before gc");
        }

        /**
         * hotspot会调用full gc
         * 可通过-XX:DisableExplicitGC 使程序中的System.gc()失效
         */
        System.gc();

        if (sr.get() != null) {
            System.out.println("not null after gc");
        } else {
            System.out.println("null after gc");
        }

        // 因为堆内存最大为20m，这里直接创建一个20m的数组来触发gc
        try {
            byte[] bytes = new byte[20 * 1024 * 1024];
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.err.println(sr.get());
        }
    }
}
