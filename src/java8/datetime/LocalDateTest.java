package java8.datetime;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

/**
 * java8 日历类
 * 加强对日期的处理
 * api: http://www.matools.com/api/java8
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/20 22:14
 */
public class LocalDateTest {
    public static void main(String[] args) {
        // 大致和LocalDateTime方法相同
        LocalDate localDate = LocalDate.now();
        System.out.println("当前日期: " + localDate);

        // atTime()系列方法 结合LocalTime构建一个LocalDateTime
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = localDate.atTime(localTime);
        System.out.println("当前日期时间: " + localDateTime);

        // get() 获取指定字段int
        System.out.println(String.format("这个月是%d月",localDate.get(ChronoField.MONTH_OF_YEAR)));

        // 是否闰年
        System.out.println("是否闰年: "+localDate.isLeapYear());
    }
}
