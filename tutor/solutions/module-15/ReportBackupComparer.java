import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Эталон m15-t07: скопировать отчёт в backup, поправить копию и сравнить с оригиналом.
public class ReportBackupComparer {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<String> report = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            report.add(sc.nextLine());
        }
        int k = Integer.parseInt(sc.nextLine().trim());
        String newText = sc.nextLine();

        Path original = Path.of("report.txt");
        Files.write(original, report);

        Path backupDir = Path.of("backup");
        Files.createDirectories(backupDir);
        Path copy = backupDir.resolve(original.getFileName());
        Files.copy(original, copy, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Копия создана: " + (Files.isRegularFile(copy) ? "да" : "нет"));
        System.out.println("До правки совпадает: " + (Files.mismatch(original, copy) == -1 ? "да" : "нет"));

        List<String> copyLines = new ArrayList<>(Files.readAllLines(copy));
        copyLines.set(k - 1, newText);
        Files.write(copy, copyLines);

        List<String> a = Files.readAllLines(original);
        List<String> b = Files.readAllLines(copy);
        System.out.println("После правки совпадает: " + (a.equals(b) ? "да" : "нет"));

        int firstDiff = -1;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) {
                firstDiff = i + 1;
                break;
            }
        }
        System.out.println(firstDiff == -1 ? "Отличий нет" : "Первое отличие в строке: " + firstDiff);
    }
}
