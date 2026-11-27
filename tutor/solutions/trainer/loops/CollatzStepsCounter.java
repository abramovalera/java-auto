import java.util.Scanner;

public class CollatzStepsCounter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextInt();
        long max = n;
        int steps = 0;
        while (n != 1) {
            if (n % 2 == 0) {
                n /= 2;
            } else {
                n = 3 * n + 1;
            }
            steps++;
            if (n > max) {
                max = n;
            }
        }
        System.out.println("Шагов: " + steps + ", максимум: " + max);
    }
}
