import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class UniqueWordsInOrder {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] words = scanner.nextLine().trim().split("\\s+");

        Set<String> unique = new LinkedHashSet<>();   // без повторов, в порядке первого появления
        for (String word : words) {
            unique.add(word);
        }

        System.out.println(String.join(" ", unique));
    }
}
