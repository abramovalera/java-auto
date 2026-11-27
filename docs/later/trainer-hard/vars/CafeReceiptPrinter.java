import java.util.Scanner;

public class CafeReceiptPrinter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String dish = scanner.nextLine();
        int price = scanner.nextInt();
        int quantity = scanner.nextInt();
        scanner.nextLine();
        String guest = scanner.nextLine();
        int total = price * quantity;
        int bonus = (int) (total * 0.05);
        System.out.println("Гость: " + guest);
        System.out.println("Блюдо: " + dish + " x " + quantity);
        System.out.println("Сумма: " + total + " руб.");
        System.out.println("Бонусы: " + bonus);
    }
}
