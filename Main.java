import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Stream;


@SuppressWarnings("all")
public class Main {
    static Main main = new Main();
    static Integer[] arr;
    static Scanner sc = new Scanner(System.in);

    void queston(){
        System.out.println("Please input the funtion No:");
        System.out.println("1 - Get even numbers");
        System.out.println("2 - Get odd numbers");
        System.out.println("3 - Get prime numbers");
        System.out.println("4 - Get prime numbers that are bigger than 5");
        System.out.println("0 - Quit");
    }

    void init(int size){
        arr = new Integer[size];
        for (int i = 0; i < size; i++){
            arr[i] = sc.nextInt();
        }
    }

    Integer[] result(int c){
        Stream<Integer> s = Arrays.stream(arr);
        Predicate<Integer> f = (n)->{
            if (n <= 3) {
                return n > 1;
            }
            for(int i = 2; i < n; i++){
                if (n % i == 0) {
                    return false;
                }
            }
            return true;
        };
        switch (c) {
            case 1:
                return s.filter(i -> (i & 1) == 0).toArray(Integer[]::new);
            case 2 :
                return s.filter(i -> (i & 1) != 0).toArray(Integer[]::new);
            case 3 :
                return s.filter(f).toArray(Integer[]::new);
            case 4 :
                return s.filter((i) -> {return f.test(i) && i > 5;}).toArray(Integer[] ::new);
        }
        return null;
    }

    public static void main(String[] args) {
        int c ,size;
        while(true){
            main.queston();
            c = sc.nextInt();
            if(c == 0 || c > 4)break;
            System.out.println("Input size of the list:");
            size = sc.nextInt();
            System.out.println("Input elements of the list:");
            main.init(size);
            System.out.println("Filter results:");
            System.out.println(Arrays.toString(main.result(c)));
        }
        sc.close();
    }
}
