import java.util.Scanner;

public class DeliveryCalculator {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int total = 0;
        int maxDays = 0;
        for (int i = 0; i < n; i++) {
            DeliveryTariff tariff = DeliveryTariff.valueOf(in.next());
            int kg = in.nextInt();
            int cost = tariff.cost(kg);
            System.out.println(tariff.getTitle() + ": " + kg + " кг * " + tariff.getPricePerKg()
                    + " = " + cost + " руб., " + tariff.getDays() + " дн.");
            total += cost;
            if (tariff.getDays() > maxDays) {
                maxDays = tariff.getDays();
            }
        }
        System.out.println("Итого: " + total + " руб.");
        System.out.println("Всё приедет за " + maxDays + " дн.");
    }
}

enum DeliveryTariff {
    ECONOMY("Эконом", 50, 7),
    STANDARD("Стандарт", 80, 3),
    EXPRESS("Экспресс", 150, 1);

    private final String title;
    private final int pricePerKg;
    private final int days;

    DeliveryTariff(String title, int pricePerKg, int days) {
        this.title = title;
        this.pricePerKg = pricePerKg;
        this.days = days;
    }

    String getTitle() { return title; }
    int getPricePerKg() { return pricePerKg; }
    int getDays() { return days; }

    int cost(int kg) {
        return kg * pricePerKg;
    }
}
