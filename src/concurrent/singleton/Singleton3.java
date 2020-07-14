package concurrent.singleton;

/**
 * 枚举类天然单例
 * Created by xyn on 2020/1/6
 */
public enum Singleton3 {

    INSTANCE;

    public Singleton3 singletonOperation() {
        return Singleton3.INSTANCE;
    }

}
