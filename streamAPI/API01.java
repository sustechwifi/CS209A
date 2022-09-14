package streamAPI;

/**
 * @author 25874
 */

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * stream api
 * 流 是一种连续的数据集，在终结操作之前可以修改其中元素，（如过滤，跳过元素）
 * 一旦使用了终结操作（如 计数，查找，遍历等）之后，便不能再操作流
 */
@SuppressWarnings("all")
public class API01 {
    private static  List<String> test = new ArrayList<>();
    private static  Integer[] arr = {1,2,3,4,5,6,8,9};

    /** 01 以集合或数组的流代替传统迭代
     * Stream<T> filter(Predicate<? super T> predicate);
     * 传入返回值为boolean类型的函数或lambda表达式
     *
     */
    static void test01(){
        long cnt = test.stream().filter(s -> s.length() >= 3).count();
        System.out.println(cnt);
        //返回数组的流对象
        Stream<String> chars = Stream.of("1234567485549".split("4"));
    }

    /** 02 创建自定义流对象
     * static <T> Stream<T> stream(T[] array, int startInclusive, int endExclusive) 返回数组某一范围作为流
     * static<T> Stream<T> of(T... values) 静态方法 传入可变长度的对象或数组
     * static<T> Stream<T> empty() 静态方法 返回空流
     * static<T> Stream<T> generate(Supplier<T> s) 静态方法 传入一个无参有返回值的函数，（supplier 类中的 get()方法）反复调用形成流，limit限制流大小
     */
    static void test02() {
        Stream<Integer> s0 = Arrays.stream(arr,0,5);
        Stream<String> s1 = Stream.of("ssss","111","555","777");
        Stream<String> s2 = Stream.empty();
        s2 = Stream.generate(() -> "insert").limit(3);
        Stream<Double> s3 = Stream.generate(Math::random).limit(100);
    }

    /** 03 流的映射
     * <R> Stream<R> map(Function<? super T, ? extends R> mapper) 将流的元素映射成一个新的流
     * <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper); 将流的元素映射成一个新的流后合并为一个新流
     */
    static void test03(){
        Function<String,Stream<String>> function = str -> {
            List<String> res = new ArrayList<>();
            int i = 0;
            while(i < str.length()){
                int j = str.offsetByCodePoints(i,1);
                res.add(str.substring(i,j));
                i = j;
            }
            return res.stream();
        };
        Stream<Stream<String>> ss = test.stream().map(function);
        Stream<String> s1 = test.stream().flatMap(function);
        s1.forEach(System.out::println);
    }

    /** 04 流的过滤及筛选，组合
     * Stream<T> skip(long n); 跳过前n个元素
     * static <T> Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b) 静态方法 拼接两个流 返回到a
     * Stream<T> sorted(Comparator<? super T> comparator); 排序
     * void forEach(Consumer<? super T> action); 传入有参无返回的函数 进行遍历操作
     * Stream<T> peek(Consumer<? super T> action); 传入有参无返回的函数 对流中元素进行复制，并在复制中调用action，可用于调试
     * Stream<T> distinct(); 将流中元素去重
     * <R, A> R collect(Collector<? super T, A, R> collector); 将流中的元素转为集合，可传入工厂方法Collectors.toSet()，Collectors.toList()
     */
    static void test04(){
        Stream<Integer> s1 = Arrays.stream(arr).skip(2);
        s1 = Stream.concat(s1,Arrays.stream(arr).sorted(Integer::compareTo));
        s1.distinct().forEach(System.out::println); //方法引用：输出每一个元素
        Consumer<Integer> consumer = i -> {
          if((i & 1) == 1) System.out.println("odd found");
        };
        Stream<Integer> s2 = s1.peek(consumer);
        Set<Integer> res = s1.collect(Collectors.toSet());
    }

    /** 05 特定查找
     * Optional<T> max(min) (Comparator<? super T> comparator); 找到流中最大(最小)值 并返回包装对象
     * boolean anyMatch(noneMatch) (Predicate<? super T> predicate);  判断是否有无满足条件的元素
     * Optional<T> findFirst(); 返回第一个元素
     * void ifPresent(Consumer<? super T> consumer) 如果存在元素，则将元素传递给 这个无返回函数进行调用
     */
    static void test05(){
        Stream<Integer> s1 = Arrays.stream(arr);
        System.out.printf("max = %d,min = %d",s1.max(Integer::compareTo),s1.min(Integer::compareTo));
        s1.findFirst().ifPresent(integer -> System.out.println(integer));
        Optional<Integer> first = s1.filter(i -> i / 2 == 0).findFirst(); //指定条件下查找的第一个
        boolean b = s1.anyMatch(i -> i > 5);
        boolean b1 = s1.noneMatch(i -> i < 100);
    }

    /** 根据指定标准返回序列
     * static<T> Stream<T> iterate(final T seed, final UnaryOperator<T> f)
     * 传入UnaryOperator迭代函数(Function的子类) 和起始值seed，返回序列
     * Iterator<T> iterator()
     * 终结操作，返回流的迭代器
     */
    static void test06(){
        Iterator<Integer> iter = Stream.iterate(0,n -> n+1).limit(10).iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());//0 1 2 3 4 ..
        }

    }
    public static void main(String[] args) {
        test.add("001");
        test.add("002");
        test.add("003");
        test06();
    }
}
