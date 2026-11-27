import java.util.Scanner;

public class DiscountedSneakersPrice {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double price = scanner.nextDouble();
        int percent = scanner.nextInt();
        double toPay = price * (100 - percent) / 100;
        System.out.println("К оплате: " + Math.round(toPay * 100) / 100.0);
    }
}
