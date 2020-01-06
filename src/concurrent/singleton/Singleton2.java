package concurrent.singleton;

/**
 * 单例
 * Created by xyn on 2020/1/6
 */
public class Singleton2 {

    private static class SingletonClassInstance {
        private static final Singleton2 instance = new Singleton2();
    }

    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        return SingletonClassInstance.instance;
    }

}
