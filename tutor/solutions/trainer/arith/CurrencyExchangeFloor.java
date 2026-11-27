import java.util.Scanner;

public class CurrencyExchangeFloor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int rubles = scanner.nextInt();
        double rate = scanner.nextDouble();
        double dollars = Math.floor(rubles / rate * 100) / 100.0;
        System.out.println("Выдадим: " + dollars + " $");
    }
}
