import java.util.Scanner;

public class RocketCountdownRecursion {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        countdown(scanner.nextInt());
    }

    static void countdown(int n) {
        if (n == 0) {
            System.out.println("Пуск!");
            return;
        }
        System.out.println(n);
        countdown(n - 1);
    }
}
