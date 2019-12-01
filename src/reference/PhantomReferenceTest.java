package reference;


import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 虚引用:
 * 引用前和引用后没什么区别，主要在对象被finalize时
 * 做一些后置通知(有点spring的后置通知的味道)，需要
 * 配合ReferenceQueue使用，回收后会被放入引用队列
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/12/01 22:14
 */
public class PhantomReferenceTest {
    public static void main(String[] args) {
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        PhantomReference<Object> pr = new PhantomReference<Object>(new Object(), referenceQueue);

        // --------------------------------before gc--------------------------------

        System.out.println(pr.get());
        // 出队列
        System.out.println(referenceQueue.poll());

        /**
         * hotspot会调用full gc
         * 可通过-XX:DisableExplicitGC 使程序中的System.gc()失效
         */
        System.gc();

        // --------------------------------after gc--------------------------------
        System.err.println(pr.get());
        System.err.println(referenceQueue.poll());
    }
}
