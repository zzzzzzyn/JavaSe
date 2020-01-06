package concurrent.Singleton;

/**
 * 单例
 * Created by xyn on 2020/1/6
 */
public class Singleton1 {

    /**
     * 此处用volatie修饰，变量 Singleton1指向共享内存变量
     * 若不用volatile修饰，可能发生如下情况：
     * 线程1创建Singleton对象，在线程内存中，但还没有及时刷入主内存
     * 而此时线程2从主内存中读入Singleton时发现Singleton为null，还是会创建对象
     */
    private volatile static Singleton1 uniqueSingleton;

    private Singleton1() {
    }

    public static Singleton1 getInstance() {
        // 1
        if (uniqueSingleton == null) {
            synchronized (Singleton1.class) {
                if (uniqueSingleton == null) {
                    /**
                     * 1. 创建对象Singleton1
                     * 2. 初始化对象Singleton1
                     * 3. 为uniqueSingleton赋值
                     * 考虑指令重排及执行三个过程间，可能在1出有线程进行判断，
                     * 所以获取锁后要重新判断是否赋值
                     */
                    uniqueSingleton = new Singleton1();
                }
            }
        }
        return uniqueSingleton;
    }
}
