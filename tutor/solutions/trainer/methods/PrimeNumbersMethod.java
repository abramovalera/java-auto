import java.util.Scanner;

public class PrimeNumbersMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        for (int i = 0; i < k; i++) {
            int n = scanner.nextInt();
            if (isPrime(n)) {
                System.out.println(n + " — простое");
            } else {
                System.out.println(n + " — не простое");
            }
        }
    }

    static boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        for (int d = 2; d * d <= n; d++) {
            if (n % d == 0) {
                return false;
            }
        }
        return true;
    }
}
