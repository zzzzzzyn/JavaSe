package java8.datetime;

import java.time.LocalTime;

/**
 * java8 日期类
 * 加强对日期时间的处理
 * api: http://www.matools.com/api/java8
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/20 22:14
 */
public class LocalTimeTest {
    public static void main(String[] args) {
        // 和LocalDate大致相同
        LocalTime localTime = LocalTime.now();
        LocalTime localTime1 = LocalTime.of(13, 14);
        System.out.println("当前时间: " + localTime);
        System.out.println("当前时间: " + localTime1);
    }
}
