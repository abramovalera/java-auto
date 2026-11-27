import java.util.Scanner;

public class ReverseHouseNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextInt();
        long sign = 1;
        if (n < 0) {
            sign = -1;
            n = -n;
        }
        long reversed = 0;
        while (n > 0) {
            reversed = reversed * 10 + n % 10;
            n /= 10;
        }
        System.out.println(sign * reversed);
    }
}
