import java.util.Scanner;

public class WordCountInLine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        if (line.isBlank()) {
            System.out.println(0);
        } else {
            String[] words = line.trim().split("\\s+");
            System.out.println(words.length);
        }
    }
}
