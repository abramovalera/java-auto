import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class LetterFrequency {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.hasNextLine() ? scanner.nextLine() : "";
        Map<Character, Long> freq = text.chars()
                .filter(Character::isLetter)
                .map(Character::toLowerCase)
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, TreeMap::new, Collectors.counting()));
        if (freq.isEmpty()) {
            System.out.println("букв нет");
        } else {
            freq.forEach((letter, count) -> System.out.println(letter + ": " + count));
        }
    }
}
