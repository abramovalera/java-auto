import java.util.Scanner;

public class DigitCountMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        for (int i = 0; i < k; i++) {
            System.out.print(countDigits(scanner.nextLong()) + " ");
        }
        System.out.println();
    }

    static int countDigits(long n) {
        int count = 0;
        do {
            count++;
            n = n / 10;
        } while (n != 0);
        return count;
    }
}
