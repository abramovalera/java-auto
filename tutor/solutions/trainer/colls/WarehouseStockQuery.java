import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WarehouseStockQuery {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, Integer> stock = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String item = sc.next();
            int amount = sc.nextInt();
            stock.put(item, stock.getOrDefault(item, 0) + amount);
        }
        String query = sc.next();
        System.out.println(query + ": " + stock.getOrDefault(query, 0));
    }
}
