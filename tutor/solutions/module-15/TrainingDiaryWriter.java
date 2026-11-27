import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

// Эталон m15-t09: много строк одним BufferedWriter (write + newLine), потом чтение файла обратно.
public class TrainingDiaryWriter {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        Path diary = Path.of("diary.txt");

        try (BufferedWriter out = Files.newBufferedWriter(diary)) {
            int total = 0;
            for (int day = 1; day <= n; day++) {
                int minutes = Integer.parseInt(sc.nextLine().trim());
                total += minutes;
                out.write("День " + day + ": " + minutes + " мин");
                out.newLine();
            }
            out.write("Итого: " + total + " мин");
            out.newLine();
        }

        List<String> lines = Files.readAllLines(diary);
        System.out.println("Строк в diary.txt: " + lines.size());
        for (String line : lines) {
            System.out.println(line);
        }
    }
}
