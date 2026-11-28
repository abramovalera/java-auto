import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DeliveryCost {
    public static void main(String[] args) {
        // Все стратегии регистрируем один раз: ключ — как способ набирают на вводе.
        Map<String, DeliveryRule> rules = new HashMap<>();
        rules.put("courier", new CourierRule());
        rules.put("pickup", new PickupRule());
        rules.put("post", new PostRule());

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int total = 0;
        for (int i = 0; i < n; i++) {
            String method = in.next();
            int weight = in.nextInt();
            int distance = in.nextInt();

            DeliveryRule rule = rules.get(method);
            if (rule == null) {
                System.out.println(method + ": нет такого способа доставки");
                continue;
            }
            int cost = rule.cost(weight, distance);
            total += cost;
            System.out.println(rule.title() + ", " + weight + " кг, " + distance + " км: " + cost + " ₽");
        }
        System.out.println("Итого: " + total + " ₽");
    }
}

interface DeliveryRule {
    String title();

    int cost(int weightKg, int distanceKm);
}

class CourierRule implements DeliveryRule {
    @Override
    public String title() {
        return "Курьер";
    }

    @Override
    public int cost(int weightKg, int distanceKm) {
        int cost = 300 + 25 * distanceKm;
        if (weightKg > 10) {
            cost += 200;
        }
        return cost;
    }
}

class PickupRule implements DeliveryRule {
    @Override
    public String title() {
        return "Самовывоз";
    }

    @Override
    public int cost(int weightKg, int distanceKm) {
        return weightKg <= 5 ? 0 : 99;
    }
}

class PostRule implements DeliveryRule {
    @Override
    public String title() {
        return "Почта";
    }

    @Override
    public int cost(int weightKg, int distanceKm) {
        return 150 + 40 * weightKg;
    }
}
