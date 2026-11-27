import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class WordFrequencySorted {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] words = scanner.nextLine().trim().toLowerCase().split("\\s+");

        Map<String, Integer> counts = new TreeMap<>();   // TreeMap держит ключи по алфавиту
        for (String word : words) {
            counts.put(word, counts.getOrDefault(word, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
