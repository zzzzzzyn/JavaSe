package concurrent.singleton;

/**
 * 单例
 * Created by xyn on 2020/1/6
 */
public class Singleton1 {

    /**
     * 此处用volatie修饰，变量 Singleton1指向共享内存变量
     * 若不用volatile修饰，可能发生如下情况：
     * 发生指令重排，uniqueSingleton赋值了但没有完成初始化，
     * 此时使用uniqueSingleton变量的线程就会报错
     */
    private volatile static Singleton1 uniqueSingleton;

    private Singleton1() {
    }

    public static Singleton1 getInstance() {
        if (uniqueSingleton == null) {                  // 1
            synchronized (Singleton1.class) {
                /**
                 * 这里再次判断是考虑指令重排及执行三个过程间
                 * 线程1的uniqueSingleton值分配了内存空间和初始化了对象还没有赋值
                 * 此时线程2执行到语句1发现uniqueSingleton字段为null，开始尝试获取锁
                 * 获取到锁后，uniqueSingleton字段也赋值好了
                 * 所以获取锁后要重新判断是否赋值
                 */
                if (uniqueSingleton == null) {
                    /**
                     * 执行的操作
                     * 1. 分配内存空间
                     * 2. 初始化对象Singleton1
                     * 3. 为uniqueSingleton赋值
                     */
                    uniqueSingleton = new Singleton1();
                }
            }
        }
        return uniqueSingleton;
    }
}
