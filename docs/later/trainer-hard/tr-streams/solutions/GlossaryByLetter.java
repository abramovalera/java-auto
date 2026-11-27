import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class GlossaryByLetter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<Character, String> glossary = Arrays.stream(scanner.nextLine().trim().split(" "))
                .collect(Collectors.groupingBy(
                        w -> Character.toLowerCase(w.charAt(0)),
                        TreeMap::new,
                        Collectors.joining(", ")));
        glossary.forEach((letter, words) -> System.out.println(letter + ": " + words));
    }
}
