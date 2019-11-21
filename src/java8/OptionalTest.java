package java8;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * java8新特性
 * 主要用来解决空指针
 * api: http://www.matools.com/api/java8
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/20 22:14
 */
public class OptionalTest {
    public static void main(String[] args) {
        /**
         * 参考博客:
         * https://www.jdon.com/52008
         * https://www.jianshu.com/p/c169ddd34903
         * https://www.cnblogs.com/zhangboyu/p/7580262.html
         */

        // 个人认为最好不要使用get()，应使用orElse()做兜底方法
        // 不要使用isPresent()，这跟==null好像没什么差别
        // 另外optional可以使用链式编程，是真的方便

        /* -----------------------------------构造----------------------------------- */

        /**
         * Optional构造
         * empty() value为null
         * of() value必须不为null，否则throw NullPointerException
         * ofNullable() 就比较包容了，null或非null都可  ===》 推荐使用
         */
        Optional<Object> optional = Optional.empty();
        Optional<String> optionalS = Optional.of("args");           // 严格使用
        Optional<Object> optionalO = Optional.ofNullable(null);     // 推荐使用

        /**
         * get() 获取Optional的value
         * 通过源码可知，value为null会throw NoSuchElementException
         * 尽量避免使用，用orElse()替代
         */
        // System.out.println(optional.get());
        System.out.println(optionalS.get());

        /**
         * isPresent() value是否为null
         */
        System.out.println("optional: " + optional.isPresent());
        System.out.println("optionalS: " + optionalS.isPresent());
        System.out.println("optionalO: " + optionalO.isPresent());

        /**
         * ifPresent(Consumer<? super T> consumer)
         * value不为null就开始消费consumer
         */
        List<String> asList = Arrays.asList("a", "b", "c", "d");
        optional = Optional.ofNullable(asList);
        optional.ifPresent(System.out::println);

        /* -----------------------------------orElse----------------------------------- */

        /**
         * orElse(T other)
         * 若value存在，返回value，否则返回other
         */
        optional = Optional.empty();
        System.out.println(optional.orElse("xyn"));
        System.out.println(Optional.ofNullable("args")
                .orElse("xyn"));

        /**
         * orElseGet(Supplier<? extends T> other)
         * 若value存在，返回value，否则返回other的get
         */
        optional = Optional.empty();
        System.out.println(optional.orElseGet(LocalDateTime::now));

        /**
         * orElseThrow(Supplier<? extends X> exceptionSupplier)
         * 若value存在，返回value，否则抛异常(自定义)
         */
        optional = Optional.empty();
        System.out.println(optional.orElseThrow(NullPointerException::new));

        /* ---------------------------------------------------------------------------- */

        /**
         * filter(Predicate<? super T> predicate)
         * 过滤操作，返回为true的值
         */
        String result = Optional.ofNullable("abcdefg")
                .filter(value -> value != null && value.length() > 5)
                .orElse("abc");
        System.out.println(result);

        /**
         * map(Predicate<? super T> predicate)
         * 不为null，进行处理返回Optional
         * 为null，返回空Optional
         */
        Integer value = Optional.ofNullable("abcdefg")
                .map(String::length)
                .filter(val -> val > 8)
                .orElse(10);
        System.out.println(value);
    }
}
