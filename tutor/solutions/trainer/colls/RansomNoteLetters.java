import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class RansomNoteLetters {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String note = sc.nextLine().toLowerCase();
        String paper = sc.nextLine().toLowerCase();
        Map<Character, Integer> available = new HashMap<>();
        for (char c : paper.toCharArray()) {
            available.put(c, available.getOrDefault(c, 0) + 1);
        }
        Set<Character> missing = new TreeSet<>();
        for (char c : note.toCharArray()) {
            if (c == ' ') {
                continue;
            }
            int left = available.getOrDefault(c, 0);
            if (left == 0) {
                missing.add(c);
            } else {
                available.put(c, left - 1);
            }
        }
        if (missing.isEmpty()) {
            System.out.println("Можно составить");
        } else {
            StringBuilder sb = new StringBuilder();
            for (char c : missing) {
                sb.append(c);
            }
            System.out.println("Не хватает: " + sb);
        }
    }
}
