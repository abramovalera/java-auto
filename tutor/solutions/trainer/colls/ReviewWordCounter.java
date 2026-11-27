import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ReviewWordCounter {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.hasNextLine() ? sc.nextLine() : "";
        if (line.isBlank()) {
            System.out.println("Пустой отзыв");
            return;
        }
        Map<String, Integer> counts = new TreeMap<>();
        for (String word : line.toLowerCase().split(" ")) {
            counts.put(word, counts.getOrDefault(word, 0) + 1);
        }
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            System.out.println(e.getKey() + " - " + e.getValue());
        }
    }
}
