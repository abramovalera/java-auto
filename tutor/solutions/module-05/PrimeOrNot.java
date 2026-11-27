import java.util.Scanner;

public class PrimeOrNot {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        boolean prime = n >= 2;                    // флаг: 0 и 1 — не простые
        for (int d = 2; d * d <= n; d++) {         // делители ищем до корня из n
            if (n % d == 0) {
                prime = false;
                break;                             // нашли делитель — дальше искать незачем
            }
        }
        if (prime) {
            System.out.println("Простое");
        } else {
            System.out.println("Не простое");
        }
    }
}
