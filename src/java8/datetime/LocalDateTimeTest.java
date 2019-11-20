import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * java8 日历类
 * 加强对日期时间的处理
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

        // 设置时间
        LocalDateTime localDateTime = LocalDateTime.of(1997, 1, 18, 11, 9);
        System.out.println(localDateTime);

        // with...()系列方法,修改时间并返回
        localDateTime = localDateTime.withYear(1998);
        System.out.println(localDateTime);
    }
}
