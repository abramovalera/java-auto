import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class OrderCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Set<Integer> seen = new HashSet<>();
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            seen.add(scanner.nextInt());
        }
        int m = scanner.nextInt();
        for (int i = 0; i < m; i++) {
            int order = scanner.nextInt();
            boolean isNew = seen.add(order);
            System.out.println(order + ": " + (isNew ? "ок" : "повтор"));
        }
    }
}
