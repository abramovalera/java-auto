import java.util.Scanner;

public class PowerOfTwoCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        if (n <= 0) {
            System.out.println("Нет");
        } else {
            int power = 0;
            while (n % 2 == 0) {
                n /= 2;
                power++;
            }
            if (n == 1) {
                System.out.println("2^" + power);
            } else {
                System.out.println("Нет");
            }
        }
    }
}
