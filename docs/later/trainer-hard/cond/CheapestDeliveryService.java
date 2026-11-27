import java.util.Scanner;

public class CheapestDeliveryService {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int courier = scanner.nextInt();
        int post = scanner.nextInt();
        int pickup = scanner.nextInt();
        String best = "Курьер";
        int bestPrice = courier;
        if (post < bestPrice) {
            best = "Почта";
            bestPrice = post;
        }
        if (pickup < bestPrice) {
            best = "Пункт выдачи";
            bestPrice = pickup;
        }
        System.out.println("Дешевле всего: " + best + " — " + bestPrice + " ₽");
    }
}
