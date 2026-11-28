import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PizzaOrder {
    private final String name;
    private final int size;
    private final String sauce;
    private final boolean extraCheese;
    private final List<String> toppings;

    // Готовый заказ собирает только строитель
    private PizzaOrder(Builder builder) {
        this.name = builder.name;
        this.size = builder.size;
        this.sauce = builder.sauce;
        this.extraCheese = builder.extraCheese;
        this.toppings = new ArrayList<>(builder.toppings);
    }

    int price() {
        int base = switch (size) {
            case 25 -> 400;
            case 30 -> 500;
            case 35 -> 600;
            default -> 700;
        };
        if (extraCheese) {
            base += 80;
        }
        return base + toppings.size() * 60;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + ", " + size + " см, соус " + sauce);
        if (extraCheese) {
            sb.append(", двойной сыр");
        }
        if (!toppings.isEmpty()) {
            sb.append(", добавки: ").append(String.join(", ", toppings));
        }
        return sb.toString();
    }

    static class Builder {
        private final String name;
        private int size = 30;
        private String sauce = "томатный";
        private boolean extraCheese = false;
        private final List<String> toppings = new ArrayList<>();

        Builder(String name) {
            this.name = name;
        }

        Builder size(int size) {
            this.size = size;
            return this;
        }

        Builder sauce(String sauce) {
            this.sauce = sauce;
            return this;
        }

        Builder extraCheese() {
            this.extraCheese = true;
            return this;
        }

        Builder topping(String topping) {
            toppings.add(topping);
            return this;
        }

        PizzaOrder build() {
            if (size != 25 && size != 30 && size != 35 && size != 40) {
                throw new IllegalArgumentException("размера " + size + " см нет");
            }
            return new PizzaOrder(this);
        }
    }

    public static void main(String[] args) {
        PizzaOrder ofTheDay = new Builder("Деревенская")
                .size(35)
                .sauce("сливочный")
                .topping("бекон")
                .topping("лук")
                .build();
        System.out.println("Пицца дня: " + ofTheDay + " = " + ofTheDay.price() + " руб.");

        Scanner in = new Scanner(System.in);
        int n = Integer.parseInt(in.nextLine().trim());
        int accepted = 0;
        int sum = 0;
        for (int k = 1; k <= n; k++) {
            String[] parts = in.nextLine().trim().split("\\s+");
            Builder builder = new Builder(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                String option = parts[i];
                if (option.equals("cheese")) {
                    builder.extraCheese();
                } else if (option.startsWith("size=")) {
                    builder.size(Integer.parseInt(option.substring(5)));
                } else if (option.startsWith("sauce=")) {
                    builder.sauce(option.substring(6));
                } else if (option.startsWith("add=")) {
                    builder.topping(option.substring(4));
                }
            }
            try {
                PizzaOrder order = builder.build();
                System.out.println("Заказ " + k + ": " + order + " = " + order.price() + " руб.");
                accepted++;
                sum += order.price();
            } catch (IllegalArgumentException e) {
                System.out.println("Заказ " + k + " не принят: " + e.getMessage());
            }
        }
        System.out.println("Принято заказов: " + accepted + " из " + n + ", сумма: " + sum + " руб.");
    }
}
