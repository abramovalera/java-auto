import java.util.Map;
import java.util.Scanner;

public class TrafficLightAdvice {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, String> advice = Map.of(
                "красный", "Стой",
                "жёлтый", "Жди",
                "зелёный", "Иди");
        String color = sc.next().toLowerCase();
        System.out.println(advice.getOrDefault(color, "Неизвестный сигнал"));
    }
}
