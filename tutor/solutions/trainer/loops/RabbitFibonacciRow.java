import java.util.Scanner;

public class RabbitFibonacciRow {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long previous = 0;
        long current = 1;
        for (int i = 1; i <= n; i++) {
            System.out.print(current + " ");
            long next = previous + current;
            previous = current;
            current = next;
        }
        System.out.println();
    }
}
