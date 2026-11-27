import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WordPatternMatch {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String pattern = sc.nextLine();
        String[] words = sc.nextLine().split(" ");
        if (words.length != pattern.length()) {
            System.out.println("Не подходит");
            return;
        }
        Map<Character, String> letterToWord = new HashMap<>();
        Map<String, Character> wordToLetter = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            char letter = pattern.charAt(i);
            String word = words[i];
            if (letterToWord.containsKey(letter) && !letterToWord.get(letter).equals(word)) {
                System.out.println("Не подходит");
                return;
            }
            if (wordToLetter.containsKey(word) && wordToLetter.get(word) != letter) {
                System.out.println("Не подходит");
                return;
            }
            letterToWord.put(letter, word);
            wordToLetter.put(word, letter);
        }
        System.out.println("Подходит");
    }
}
