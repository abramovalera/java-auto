import java.util.Scanner;

public class TaxiFareByDistance {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int km = scanner.nextInt();
        String time = scanner.next();
        int price = 150;
        if (km > 2) {
            price = price + (km - 2) * 30;
        }
        if (time.equals("ночь")) {
            price = price + 100;
        }
        System.out.println("Поездка: " + price + " ₽");
    }
}
