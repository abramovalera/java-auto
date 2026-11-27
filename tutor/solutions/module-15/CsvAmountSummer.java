import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Эталон m15-t04: записать платежи в payments.csv с заголовком, прочитать файл и посчитать сумму.
public class CsvAmountSummer {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<String> rows = new ArrayList<>();
        rows.add("id,merchant,amount");
        for (int i = 0; i < n; i++) {
            rows.add(sc.nextLine());
        }

        Path csv = Path.of("payments.csv");
        Files.write(csv, rows);

        List<String> lines = Files.readAllLines(csv);
        int count = 0;
        long sum = 0;
        long maxAmount = 0;
        String maxId = null;
        String maxMerchant = null;
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            String[] cells = line.split(",");
            String id = cells[0].trim();
            String merchant = cells[1].trim();
            long amount = Long.parseLong(cells[2].trim());
            count++;
            sum += amount;
            if (maxId == null || amount > maxAmount) {
                maxAmount = amount;
                maxId = id;
                maxMerchant = merchant;
            }
        }

        System.out.println("Записей: " + count);
        System.out.println("Сумма: " + sum);
        if (maxId == null) {
            System.out.println("Самый крупный: нет");
        } else {
            System.out.println("Самый крупный: " + maxId + " (" + maxMerchant + ", " + maxAmount + ")");
        }
    }
}
