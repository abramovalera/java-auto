import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class FirstLonelyLetter {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String word = sc.next();
        Map<Character, Integer> counts = new LinkedHashMap<>();
        for (char c : word.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }
        for (Map.Entry<Character, Integer> e : counts.entrySet()) {
            if (e.getValue() == 1) {
                System.out.println("Первая уникальная: " + e.getKey());
                return;
            }
        }
        System.out.println("Все буквы повторяются");
    }
}
