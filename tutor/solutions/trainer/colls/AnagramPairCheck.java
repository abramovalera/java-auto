import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AnagramPairCheck {
    static Map<Character, Integer> letterCounts(String word) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : word.toLowerCase().toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }
        return counts;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.next();
        String b = sc.next();
        if (letterCounts(a).equals(letterCounts(b))) {
            System.out.println("Анаграммы");
        } else {
            System.out.println("Не анаграммы");
        }
    }
}
