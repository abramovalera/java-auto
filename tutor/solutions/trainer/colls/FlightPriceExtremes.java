import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FlightPriceExtremes {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Integer> prices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            prices.add(sc.nextInt());
        }
        System.out.println("Дешевле всего: " + Collections.min(prices));
        System.out.println("Дороже всего: " + Collections.max(prices));
    }
}
