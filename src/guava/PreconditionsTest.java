//package guava;
//
//import com.google.common.base.Preconditions;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 前置条件检查，失败抛出异常
// * Created by xyn on 2020/1/14
// */
//public class PreconditionsTest {
//    public static void main(String[] args) {
//
//        /**
//         * 检查参数是true。用于验证方法的参数
//         */
//        Preconditions.checkArgument(true, "检查参数是否为%s", true);
//
//        /**
//         * 检查值是否不为null。直接返回值，因此可以使用checkNotNull(value)链式编程
//         */
//        String str = "xyn";
//        Preconditions.checkNotNull(str).substring(1).substring(2);
//        Preconditions.checkNotNull(str, "检查值是否为%s", "null");
//
//        /**
//         * 检查对象的某些状态
//         */
//        Preconditions.checkState(true, "对象状态是否为%s", true);
//
//        /**
//         * 检查index作为索引值对某个列表、字符串或数组是否有效。index>=0 && index<size
//         */
//        List<Integer> list = new ArrayList<>(10);
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        Preconditions.checkElementIndex(1, list.size());
//
//        /**
//         * 检查index作为位置值对某个列表、字符串或数组是否有效。index>=0 && index<=size
//         */
//        Preconditions.checkPositionIndex(3, list.size());
//
//        /**
//         * 检查[start, end]表示的位置范围对某个列表、字符串或数组是否有效
//         */
//        Preconditions.checkPositionIndexes(1, 4, list.size());
//    }
//}
