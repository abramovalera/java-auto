import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Эталон m15-t08 (Мастерская): orders.csv → сводка в summary.txt → печать сводки из файла.
public class OrderDaySummary {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<String> rows = new ArrayList<>();
        rows.add("item;status;price");
        for (int i = 0; i < n; i++) {
            rows.add(sc.nextLine());
        }
        Path orders = Path.of("orders.csv");
        Files.write(orders, rows);

        int total = 0;
        int paid = 0;
        int cancelled = 0;
        int pending = 0;
        long paidSum = 0;
        long maxPrice = -1;
        String maxItem = null;
        List<String> cancelledItems = new ArrayList<>();

        List<String> lines = Files.readAllLines(orders);
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).isBlank()) {
                continue;
            }
            String[] c = lines.get(i).split(";");
            String item = c[0].trim();
            String status = c[1].trim().toUpperCase();
            long price = Long.parseLong(c[2].trim());
            total++;
            if (status.equals("PAID")) {
                paid++;
                paidSum += price;
            } else if (status.equals("CANCELLED")) {
                cancelled++;
                cancelledItems.add(item);
            } else if (status.equals("PENDING")) {
                pending++;
            }
            if (price > maxPrice) {
                maxPrice = price;
                maxItem = item;
            }
        }
        cancelledItems.sort(null);

        List<String> summary = new ArrayList<>();
        summary.add("Всего заказов: " + total);
        summary.add("PAID: " + paid);
        summary.add("CANCELLED: " + cancelled);
        summary.add("PENDING: " + pending);
        summary.add("Оплачено: " + paidSum + " руб.");
        summary.add("Самый дорогой: " + (maxItem == null ? "нет" : maxItem + " (" + maxPrice + " руб.)"));
        summary.add("Отменены: " + (cancelledItems.isEmpty() ? "нет" : String.join(", ", cancelledItems)));

        Path summaryFile = Path.of("summary.txt");
        Files.write(summaryFile, summary);
        System.out.print(Files.readString(summaryFile));
    }
}
