import java.util.Scanner;

public class CurrencyExchangeMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double rubles = scanner.nextDouble();
        double rate = scanner.nextDouble();
        System.out.println("Получишь: " + convert(rubles, rate));
    }

    static double convert(double rubles, double rate) {
        return Math.round(rubles / rate * 100) / 100.0;
    }
}
