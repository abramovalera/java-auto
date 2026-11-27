import java.util.Scanner;
import java.util.stream.LongStream;

public class CubeStaircase {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        long total = LongStream.rangeClosed(1, n).sum();
        System.out.println("Кубиков: " + total);
    }
}
