package concurrent.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 原子更新字段
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/28 21:43
 */
public class AtomicIntegerFieldUpdateTest {
    /**
     * 使用原子更新整型字段有两个注意点:
     * 1. 使用时必须使用静态方法newUpdate()创建一个更新器，并设置更新的类和字段名
     * 2. 被更新的字段(属性)用public volatile修饰
     */
    private static AtomicIntegerFieldUpdater<User> aifu =
            AtomicIntegerFieldUpdater.newUpdater(User.class, "age");

    public static void main(String[] args) {
        User user = new User("xyn", 20);
        aifu.compareAndSet(user, 20,22);
        System.out.println(aifu.get(user));
    }

    static class User {
        private String name;
        public volatile int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
