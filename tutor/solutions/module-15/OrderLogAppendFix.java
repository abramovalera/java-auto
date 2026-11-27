import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

// Эталон m15-t03 (почини): журнал заказов с дописыванием.
public class OrderLogAppendFix {
    public static void main(String[] args) throws IOException {        // ошибка 1: не было throws IOException
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        Path log = Path.of("orders.log");
        Files.writeString(log, "");                                     // ошибка 3: файл не создавался и не очищался

        for (int i = 1; i <= n; i++) {
            String line = sc.nextLine();
            Files.writeString(log, i + ") " + line + "\n", StandardOpenOption.APPEND); // ошибка 2: не было APPEND
        }

        List<String> lines = Files.readAllLines(log);
        System.out.println("Строк в журнале: " + lines.size());
        for (String l : lines) {
            System.out.println(l);
        }
    }
}
