import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Warehouse {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> stock = new TreeMap<>();
        while (true) {
            String command = scanner.next();
            if (command.equals("END")) {
                break;
            }
            String item = scanner.next();
            if (command.equals("ADD")) {
                int amount = scanner.nextInt();
                stock.put(item, stock.getOrDefault(item, 0) + amount);
                // короче то же самое: stock.merge(item, amount, Integer::sum);
            } else if (command.equals("GET")) {
                System.out.println(item + ": " + stock.getOrDefault(item, 0));
            }
        }
        System.out.println("Склад: " + stock);
    }
}
