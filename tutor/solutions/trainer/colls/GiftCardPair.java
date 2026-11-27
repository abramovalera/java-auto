import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GiftCardPair {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int s = sc.nextInt();
        int n = sc.nextInt();
        Map<Integer, Integer> firstIndex = new HashMap<>();
        for (int j = 1; j <= n; j++) {
            int price = sc.nextInt();
            if (firstIndex.containsKey(s - price)) {
                System.out.println("Товары " + firstIndex.get(s - price) + " и " + j);
                return;
            }
            firstIndex.putIfAbsent(price, j);
        }
        System.out.println("Пары нет");
    }
}
