import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShoppingListPrinter {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(sc.next());
        }
        System.out.println(list);
        System.out.println("Всего покупок: " + list.size());
    }
}
