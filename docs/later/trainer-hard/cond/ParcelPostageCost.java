import java.util.Scanner;

public class ParcelPostageCost {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double weight = scanner.nextDouble();
        if (weight <= 0) {
            System.out.println("Неверный вес");
        } else if (weight > 20) {
            System.out.println("Слишком тяжёлая посылка");
        } else {
            int cost;
            if (weight <= 1) {
                cost = 200;
            } else if (weight <= 5) {
                cost = 350;
            } else {
                int extraKg = (int) Math.ceil(weight - 5);
                cost = 350 + extraKg * 30;
            }
            System.out.println("Стоимость: " + cost + " ₽");
        }
    }
}
