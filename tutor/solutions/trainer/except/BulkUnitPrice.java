import java.util.Scanner;

public class BulkUnitPrice {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String priceText = scanner.nextLine();
        String countText = scanner.nextLine();
        try {
            int price = Integer.parseInt(priceText);
            int count = Integer.parseInt(countText);
            System.out.println("Одна штука: " + price / count + " руб.");
        } catch (NumberFormatException | ArithmeticException e) {
            System.out.println("Не получилось посчитать цену");
        }
    }
}
