import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AnagramFamilies {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, List<String>> families = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            String word = sc.next();
            char[] letters = word.toCharArray();
            Arrays.sort(letters);
            String key = new String(letters);
            if (!families.containsKey(key)) {
                families.put(key, new ArrayList<>());
            }
            families.get(key).add(word);
        }
        System.out.println("Семей: " + families.size());
        for (List<String> family : families.values()) {
            System.out.println(String.join(" ", family));
        }
    }
}
