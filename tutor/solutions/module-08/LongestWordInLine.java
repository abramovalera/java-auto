import java.util.Scanner;

public class LongestWordInLine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        if (line.isBlank()) {
            System.out.println("нет слов");
            return;
        }
        String best = "";
        for (String w : line.trim().split("\\s+")) {
            if (w.length() > best.length()) {
                best = w;
            }
        }
        System.out.println(best + " " + best.length());
    }
}
