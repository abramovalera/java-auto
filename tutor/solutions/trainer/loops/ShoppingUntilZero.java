import java.util.Scanner;

public class ShoppingUntilZero {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = 0;
        long sum = 0;
        int price = scanner.nextInt();
        while (price != 0) {
            count++;
            sum += price;
            price = scanner.nextInt();
        }
        System.out.println("Покупок: " + count + ", сумма: " + sum);
    }
}
