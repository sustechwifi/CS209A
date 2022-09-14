package streamAPI;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 从流中收集结果
 * 收集器默认为List,和hashmap
 */
@SuppressWarnings("checkstyle:WhitespaceAround")
public class API02 {

    static Stream<String> stream = Stream.of("123", "121548", "7777", "461968", "844aa");
    static Stream<Integer> intstream = Stream.of(1, 2, 3, 4, 5, 6);

    /** 将流中元素返回为集合(使用Collectors类的工厂方法)
     *  <R, A> R collect(Collector<? super T, A, R> collector);
     *  指定将stream收集到collector中，T为流中元素类型 ；<R,A>泛型方法声明，R 为返回值，其为保存T的容器。
     *
     *  static <T> Collector<T, ?, List<T>> toList()
     *  static <T> Collector<T, ?, Set<T>> toSet()
     *  static <T, C extends Collection<T>> Collector<T, ?, C> toCollection(Supplier<C> collectionFactory)
     *  T 是保存元素的类型 ，C是保存了 T 的Collection子类，collectionFactory是C的构造器引用
     */
    static void test01(){
        List<String> l = stream.collect(Collectors.toList());
        Set<String> s  = stream.collect(Collectors.toSet());
        TreeSet<String> ts = stream.collect(Collectors.toCollection(TreeSet<String>::new));
    }


    /** 特殊收集
     *  static Collector<CharSequence, ?, String> joining(CharSequence delimiter)
     *  将存有字符串的流拼接为一个长字符串，并可以指定字符串之间的分隔符，和长字符串的前后缀；
     *  static <T> Collector<T, ?, IntSummaryStatistics> summarizingInt(ToIntFunction<? super T> mapper)
     *  将数字流转为一个SummaryStatistics，可以用其查找数据最大值，平均值，和等
     */
    static void test02(){
        String res = stream.collect(Collectors.joining(",", "prefix", "suffix"));
        IntSummaryStatistics summary = intstream.collect(Collectors.summarizingInt(Integer::reverse));
        System.out.println(summary.getMax()+ summary.getSum()+summary.getAverage());
    }



    static class People {
        int id ;
        String name ;

        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public People(int id, String name){
            this.id = id;
            this.name = name;
        }
    }


    /** 收集到Map中
     * static <T, K, U> Collector<T, ?, M> toMap(Function<? super T, ? extends K> keyMapper,
     *                                 Function<? super T, ? extends U> valueMapper,
     *                                 BinaryOperator<U> mergeFunction,
     *                                 Supplier<M> mapSupplier)
     * T 为流中元素，M<K,U> 为 Map<Key,Value>
     * keyMapper ,valueMapper为 T->K,T->U的映射函数
     * mergeFunction(Function的子类)为键冲突时对 两个值的处理
     * mapSupplier 为指定Map的构造器
     * ====================================
     * 按键值分组
     * static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(Function<? super T, ? extends K> classifier)
     * 传入一个参数为T，返回值为K的函数，并依据此函数将 返回同一个K的 所有T 放入一个集合中(如果传入的是Predicate断言函数，依据真假分组)
     * static <T, K, A, D> Collector<T, ?, Map<K, D>> groupingBy(Function<? super T, ? extends K> classifier,
     *                                           Collector<? super T, A, D> downstream)
     * 第二个参数Collector<? super T, A, D> downstream 为下游收集器，可选，默认为toList()，用于指定同键值的元素的收集形式，如计数，等
     * 即二次收集，用收集器 将流中元素同组元素 收集在指定的二级收集器中
     */
    static void test03(){
        People seed = new People(0, "id:0");
        Stream<People> people = Stream.iterate(seed, people1 -> new People(people1.id+1, "id:"+people1.id + 1)).limit(10);
        //Function.identity() 返回T -> T的映射
        Map<Integer, People> idToPerson = people.collect(Collectors.toMap(People::getId, Function.identity()));
        Map<String, People> nameToPerson = people.collect(Collectors.toMap(People::getName, Function.identity(), (p1, p2) -> p1));
        TreeMap<String, People> treeMap = people.collect(Collectors.toMap(People::getName, Function.identity(), (p1,p2) -> { throw new IllegalStateException();}, TreeMap::new));
        //按name分组
        Map<String, List<People>> groupMap = people.collect(Collectors.groupingBy(People::getName));
    }

    public static void main(String[] args) {
        test01();
    }
}
