package concurrent.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子更新引用
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/28 21:33
 */
public class AtomicReferenceTest {
    public static void main(String[] args) {

        User user = new User("xyn", 22);
        AtomicReference<User> ar = new AtomicReference<>(user);
        System.out.println(ar.get());
        User updateUser = new User("xxx", 21);
        ar.compareAndSet(ar.get(), updateUser);
        System.out.println(ar.get().getName());
        System.out.println(ar.get().getAge());

    }

    static class User {
        private String name;
        private int age;

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


