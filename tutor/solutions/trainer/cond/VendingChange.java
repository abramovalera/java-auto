import java.util.Scanner;

public class VendingChange {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int price = scanner.nextInt();
        int paid = scanner.nextInt();
        if (paid < price) {
            System.out.println("Не хватает " + (price - paid) + " ₽");
        } else if (paid == price) {
            System.out.println("Спасибо, без сдачи");
        } else {
            System.out.println("Ваша сдача: " + (paid - price) + " ₽");
        }
    }
}
