import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class MostFrequentWordsTopN {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine().trim());
        String text = scanner.nextLine().trim().toLowerCase();

        Map<String, Integer> counts = new TreeMap<>();
        if (!text.isEmpty()) {
            for (String word : text.split("\s+")) {
                counts.put(word, counts.getOrDefault(word, 0) + 1);
            }
        }

        // словарь по значениям не сортируется — сортируем список его пар
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                int byCount = Integer.compare(b.getValue(), a.getValue()); // по убыванию
                if (byCount != 0) {
                    return byCount;
                }
                return a.getKey().compareTo(b.getKey());                 // поровну — по алфавиту
            }
        });

        for (int i = 0; i < Math.min(n, entries.size()); i++) {
            Map.Entry<String, Integer> entry = entries.get(i);
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
