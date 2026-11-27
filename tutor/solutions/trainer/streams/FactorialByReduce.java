import java.util.Scanner;
import java.util.stream.LongStream;

public class FactorialByReduce {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long factorial = LongStream.rangeClosed(1, n).reduce(1, (acc, x) -> acc * x);
        System.out.println(n + "! = " + factorial);
    }
}
