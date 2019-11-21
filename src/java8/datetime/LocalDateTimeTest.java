package java8.datetime;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * java8 日期类
 * 加强对日期时间的处理
 * api: http://www.matools.com/api/java8
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/20 22:14
 */
public class LocalDateTimeTest {
    public static void main(String[] args) {
        // 当前日期时间
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("当前日期时间为: " + currentTime);

        // 当前日期
        System.out.println("当前日期为: " + currentTime.toLocalDate());
        // 当前时间
        System.out.println("当前时间为: " + currentTime.toLocalTime());

        int year = currentTime.getYear();
        // Month month = currentTime.getMonth();
        int month = currentTime.getMonthValue();
        int day = currentTime.getDayOfMonth();
        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int seconds = currentTime.getSecond();

        System.out.println(String.format("当前日期时间为: %d年%d月%d日 %d时%d分%d秒",
                year, month, day, hour, minute, seconds));

        int dayOfYear = currentTime.getDayOfYear();
        System.out.println(String.format("今天是今年的第%s天", dayOfYear));

        DayOfWeek dayOfWeek = currentTime.getDayOfWeek();
        System.out.println("今天是星期" + dayOfWeek.getValue());

        // ------------------------------------分割线------------------------------------

        // 日期时间格式化
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = currentTime.format(dtf);
        System.out.println("格式化时间: " + format);
        String dateTime = "2007-12-03T10:15:30";
        LocalDateTime parseDateTime = LocalDateTime.parse(dateTime);
        System.out.println("字符串转换后的时间为: " + parseDateTime);
        dateTime = "2007-12-03 10:15:30";
        parseDateTime = LocalDateTime.parse(dateTime, dtf);
        System.out.println("格式化时间字符串转换后时间为: " + parseDateTime);


        // of...()系列方法 设置日期时间
        LocalDateTime localDateTime = LocalDateTime.of(1997, 1, 18, 11, 9);
        System.out.println(localDateTime);

        /**
         * 运算操作
         * minus...()系列方法 -
         * plus...()系列方法 +
         */
        LocalDateTime localDateTime1 = localDateTime.minusSeconds(1);
        System.out.println("修剪后的时间为: " + localDateTime1);
        localDateTime1 = localDateTime.plusSeconds(1);
        System.out.println("修剪后的时间为: " + localDateTime1);
        // 增加一星期
        localDateTime1 = localDateTime.plusWeeks(1);
        System.out.println("修剪后的时间为: " + localDateTime1);

        // with...()系列方法,修改时间并返回
        localDateTime = localDateTime.withYear(1998);
        System.out.println(localDateTime);
    }
}
