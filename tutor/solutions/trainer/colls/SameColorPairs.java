import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SameColorPairs {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String color = sc.next();
            counts.put(color, counts.getOrDefault(color, 0) + 1);
        }
        long pairs = 0;
        for (int c : counts.values()) {
            pairs += (long) c * (c - 1) / 2;
        }
        System.out.println("Пар: " + pairs);
    }
}
