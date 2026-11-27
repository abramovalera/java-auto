import java.util.Scanner;

public class CountDigitsOfNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int digits = 0;
        do {
            n /= 10;
            digits++;
        } while (n != 0);
        System.out.println(digits);
    }
}
