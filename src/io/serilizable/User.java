package io.serilizable;

import java.io.Serializable;

/**
 * @author xyn
 * @description 描述信息
 * @data 2019/12/22 19:04
 */
public class User implements Serializable {

    private static final long serialVersionUID = -2536876560251601181L;

    private Integer id;
    public static String name;
    private transient int age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
