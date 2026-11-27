import java.util.Scanner;

public class ThreeDigitSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();
        int hundreds = number / 100;       // 472 / 100 = 4
        int tens = number / 10 % 10;       // 472 / 10 = 47, 47 % 10 = 7
        int units = number % 10;           // 472 % 10 = 2
        System.out.println("Сумма цифр: " + (hundreds + tens + units));
    }
}
