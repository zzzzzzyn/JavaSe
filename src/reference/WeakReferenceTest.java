package reference;


import java.lang.ref.WeakReference;

/**
 * Reference:
 *     SoftReference    -> 软引用: 发生gc且空间不够时被回收(通过最大堆内存-上次gc剩余内存来决定是否回收，
 *                                 在1.7的文档介绍中使用此类做简单缓存)
 *     WeakReference    -> 弱引用: 发生gc时必定被回收 -->也可做缓存
 *     PhantomReference -> 虚引用: 引用后和没引用没啥区别，随时可能被回收
 *     来自: https://zhuanlan.zhihu.com/p/28226360
 *
 * jps: 查看java进程id和函数名称
 * jstat -gcutil 进程号 : 显示垃圾收集信息
 *     命令参考: https://blog.csdn.net/l2580258/article/details/80147602
 *
 * jvm:
 *     实用参数: http://ifeve.com/useful-jvm-flags/
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/20 22:14
 */
public class WeakReferenceTest {
    public static void main(String[] args) {
        // 听说mybatis缓存就用的弱引用
        WeakReference<Object> wr = new WeakReference<Object>(new Object());

        if (wr.get() != null) {
            System.out.println("not null before gc");
        }

        /**
         * hotspot会调用full gc
         * 可通过-XX:DisableExplicitGC 使程序中的System.gc()失效
         */
        System.gc();

        if (wr.get() != null) {
            System.out.println("not null after gc");
        }
        else {
            System.out.println("null after gc");
        }
    }
}
