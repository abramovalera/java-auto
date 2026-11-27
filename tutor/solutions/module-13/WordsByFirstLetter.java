import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class WordsByFirstLetter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] words = scanner.nextLine().trim().toLowerCase().split("\\s+");

        Map<Character, List<String>> groups = new TreeMap<>();   // буквы по алфавиту
        for (String word : words) {
            char letter = word.charAt(0);
            if (!groups.containsKey(letter)) {
                groups.put(letter, new ArrayList<>());          // буква впервые — заводим пустой список
            }
            groups.get(letter).add(word);
        }

        for (Map.Entry<Character, List<String>> entry : groups.entrySet()) {
            System.out.println(entry.getKey() + ": " + String.join(", ", entry.getValue()));
        }
    }
}
