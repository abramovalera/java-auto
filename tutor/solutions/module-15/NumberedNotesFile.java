import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Эталон m15-t01: записать заметки в notes.txt, прочитать файл и вывести с номерами.
public class NumberedNotesFile {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<String> notes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            notes.add(sc.nextLine());
        }

        Path file = Path.of("notes.txt");
        Files.write(file, notes);

        List<String> lines = Files.readAllLines(file);
        System.out.println("Записано в notes.txt: " + lines.size());
        if (lines.isEmpty()) {
            System.out.println("Файл пуст");
            return;
        }
        for (int i = 0; i < lines.size(); i++) {
            System.out.println((i + 1) + ". " + lines.get(i));
        }
    }
}
