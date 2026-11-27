import java.util.Scanner;

public class RabbitFibonacciRecursion {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println("Пар кроликов в месяце " + n + ": " + fib(n));
    }

    static long fib(int n) {
        if (n <= 1) {
            return n;
        }
        return fib(n - 1) + fib(n - 2);
    }
}
