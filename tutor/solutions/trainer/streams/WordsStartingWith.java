import java.util.Arrays;
import java.util.Scanner;

public class WordsStartingWith {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] words = scanner.nextLine().trim().split(" ");
        String letter = scanner.nextLine().trim().toLowerCase();
        long count = Arrays.stream(words)
                .map(String::toLowerCase)
                .filter(w -> w.startsWith(letter))
                .count();
        System.out.println(count);
    }
}
