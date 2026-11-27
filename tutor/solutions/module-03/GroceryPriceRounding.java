import java.util.Scanner;

public class GroceryPriceRounding {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int pricePerKg = scanner.nextInt();
        int grams = scanner.nextInt();

        double cost = pricePerKg * grams / 1000.0;
        double costRounded = Math.round(cost * 100) / 100.0;
        long toPay = (long) Math.ceil(cost);

        System.out.println("Стоимость: " + costRounded);
        System.out.println("К оплате: " + toPay);
    }
}
