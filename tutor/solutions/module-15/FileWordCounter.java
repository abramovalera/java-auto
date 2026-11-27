import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Эталон m15-t02: записать текст в text.txt, прочитать и посчитать строки и слова.
public class FileWordCounter {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<String> input = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            input.add(sc.nextLine());
        }

        Path file = Path.of("text.txt");
        Files.write(file, input);

        int lineCount = Files.readAllLines(file).size();
        String text = Files.readString(file);

        int words = 0;
        String longest = "";
        for (String w : text.split("\\s+")) {
            if (w.isEmpty()) {
                continue;
            }
            words++;
            if (w.length() > longest.length()) {
                longest = w;
            }
        }

        System.out.println("Строк: " + lineCount);
        System.out.println("Слов: " + words);
        System.out.println("Самое длинное слово: " + (words == 0 ? "нет" : longest));
    }
}
