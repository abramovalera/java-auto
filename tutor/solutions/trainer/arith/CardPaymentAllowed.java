import java.util.Scanner;

public class CardPaymentAllowed {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean blocked = scanner.nextBoolean();
        int balance = scanner.nextInt();
        int price = scanner.nextInt();
        boolean paid = !blocked && balance >= price;
        System.out.println(paid);
    }
}
