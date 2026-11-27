import java.util.Scanner;

public class DigitalRootRepeat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        System.out.print(n);
        while (n >= 10) {
            long sum = 0;
            while (n > 0) {
                sum += n % 10;
                n /= 10;
            }
            n = sum;
            System.out.print(" -> " + n);
        }
        System.out.println();
    }
}
