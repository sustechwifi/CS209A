package streamAPI;

import java.util.stream.Stream;

/** 流的其他操作
 * reduce，基本类型流，并行流
 */
public class API03 {

    static Stream<String> stream = Stream.of("123", "121548", "7777", "461968", "844aa");
    static Stream<Integer> intstream = Stream.of(1, 2, 3, 4, 5, 6);

    /** reduce，用自定义函数通过流中元素计算某一个值
     * Optional<T> reduce(BinaryOperator<T> accumulator);
     * BinaryOperator 为二元参数的函数
     */
    @SuppressWarnings("checkstyle:WhitespaceAround")
    static void test01(){
        //System.out.println(intstream.reduce((x, y) -> x + y));
        System.out.println(intstream.reduce(Integer::sum));
    }

}
