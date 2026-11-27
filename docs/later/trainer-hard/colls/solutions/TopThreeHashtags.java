import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class HashtagRankOrder implements Comparator<Map.Entry<String, Integer>> {
    @Override
    public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
        int byCount = Integer.compare(b.getValue(), a.getValue());
        if (byCount != 0) {
            return byCount;
        }
        return a.getKey().compareTo(b.getKey());
    }
}

public class TopThreeHashtags {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String tag = sc.next().toLowerCase();
            counts.put(tag, counts.getOrDefault(tag, 0) + 1);
        }
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(new HashtagRankOrder());
        for (int i = 0; i < Math.min(3, entries.size()); i++) {
            Map.Entry<String, Integer> e = entries.get(i);
            System.out.println((i + 1) + ". " + e.getKey() + " — " + e.getValue());
        }
    }
}
