import java.util.Scanner;

public class SumDigitsOfNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Math.abs(scanner.nextInt());
        int sum = 0;
        while (n > 0) {
            sum += n % 10;
            n /= 10;
        }
        System.out.println(sum);
    }
}
