import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ShoeSizeCounts {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<Integer, Integer> counts = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            int size = sc.nextInt();
            counts.put(size, counts.getOrDefault(size, 0) + 1);
        }
        if (counts.isEmpty()) {
            System.out.println("Продаж нет");
        }
        for (Map.Entry<Integer, Integer> e : counts.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }
}
