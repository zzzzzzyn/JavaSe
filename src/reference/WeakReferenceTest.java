package reference;


import java.lang.ref.WeakReference;

/**
 * Reference:
 *     WeakReference -> 弱引用
 * api: http://www.matools.com/api/java8
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/20 22:14
 */
public class WeakReferenceTest {
    public static void main(String[] args) {
        WeakReference<Object> wr = new WeakReference<Object>(new Object());

        if (wr.get() != null) {
            System.out.println("not null before gc");
        }

        System.gc();

        if (wr.get() != null) {
            System.out.println("not null after gc");
        }
        else {
            System.out.println("null after gc");
        }
    }
}
