import java.util.Scanner;

public class ReverseThreeDigit {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int hundreds = n / 100;
        int tens = n / 10 % 10;
        int ones = n % 10;
        int reversed = ones * 100 + tens * 10 + hundreds;
        System.out.println(reversed);
    }
}
