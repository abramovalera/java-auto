import java.util.Scanner;

public class ChangeInCoins {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int change = scanner.nextInt();

        int tens = change / 10;
        change %= 10;
        int fives = change / 5;
        change %= 5;
        int twos = change / 2;
        int ones = change % 2;

        System.out.println("10 руб.: " + tens);
        System.out.println("5 руб.: " + fives);
        System.out.println("2 руб.: " + twos);
        System.out.println("1 руб.: " + ones);
        System.out.println("Всего монет: " + (tens + fives + twos + ones));
    }
}
