import java.util.Scanner;

public class PrimesUpTo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        printPrimesUpTo(n);
    }

    static void printPrimesUpTo(int n) {
        boolean found = false;
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                System.out.print(i + " ");
                found = true;
            }
        }
        if (found) {
            System.out.println();
        } else {
            System.out.println("Простых чисел нет");
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
