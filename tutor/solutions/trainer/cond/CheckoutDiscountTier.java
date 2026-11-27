import java.util.Scanner;

public class CheckoutDiscountTier {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sum = scanner.nextInt();
        int percent;
        if (sum >= 5000) {
            percent = 10;
        } else if (sum >= 1000) {
            percent = 5;
        } else {
            percent = 0;
        }
        double toPay = sum - sum * percent / 100.0;
        toPay = Math.round(toPay * 100) / 100.0;
        System.out.println("Скидка: " + percent + "%");
        System.out.println("К оплате: " + toPay);
    }
}
