import java.util.Scanner;

public class ShopDiscountMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double price = scanner.nextDouble();
        int percent = scanner.nextInt();
        System.out.println("К оплате: " + applyDiscount(price, percent));
    }

    static double applyDiscount(double price, int percent) {
        return price * (100 - percent) / 100;
    }
}
