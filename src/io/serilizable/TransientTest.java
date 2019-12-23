package io.serilizable;

import java.io.*;

/**
 * 关于Transient的使用
 *  1)变量被transient修饰,变量将不再是对象持久化的一部分,该变量内容在序列化后无法获得访问。
 *  2)transient关键字只能修饰变量，而不能修饰方法和类。注意,本地变量是不能被transient关键字修饰的。
 *    变量如果是用户自定义类变量,则该类需要实现Serializable接口。
 *  3)被transient关键字修饰的变量不再能被序列化,一个静态变量不管是否被transient修饰,均不能被序列化。
 *    反序列化后类中static型变量username的值为当前JVM中对应static变量的值,这个值是JVM中的不是反序列化得出的。
 *
 *  若实现的是Externalizable接口,则没有任何东西可以自动序列化,
 *  需要在writeExternal方法中进行手工指定所要序列化的变量,这与是否被transient修饰无关。
 * @author xyn
 * @description 描述信息
 * @data 2019/12/22 19:10
 */
public class TransientTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // User.name = "xyn";
        User user = new User();
        user.setId(10);
        user.setAge(10);

        // 序列化
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:/user.txt"));
        oos.writeObject(user);
        oos.flush();
        oos.close();

        // 反序列化
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("D:/user.txt"));
        User user2 = (User) ois.readObject();
        System.out.println(user2.getId());
        System.out.println(user2.getAge());
        System.out.println(User.name);
    }



}
