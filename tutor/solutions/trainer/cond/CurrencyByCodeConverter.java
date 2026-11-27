import java.util.Scanner;

public class CurrencyByCodeConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int amount = scanner.nextInt();
        String code = scanner.next();
        switch (code) {
            case "USD" -> System.out.println(amount * 90 + " ₽");
            case "EUR" -> System.out.println(amount * 100 + " ₽");
            case "CNY" -> System.out.println(amount * 12 + " ₽");
            default -> System.out.println("Неизвестная валюта: " + code);
        }
    }
}
