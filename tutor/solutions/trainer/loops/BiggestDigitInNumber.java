import java.util.Scanner;

public class BiggestDigitInNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Math.abs(scanner.nextInt());
        int maxDigit = 0;
        int count = 0;
        do {
            int digit = n % 10;
            if (digit > maxDigit) {
                maxDigit = digit;
                count = 1;
            } else if (digit == maxDigit) {
                count++;
            }
            n /= 10;
        } while (n > 0);
        System.out.println("Максимальная цифра: " + maxDigit + ", раз: " + count);
    }
}
