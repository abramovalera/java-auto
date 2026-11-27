import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class OrderItemsDiff {
    static Set<String> readItems(Scanner sc) {
        int count = sc.nextInt();
        Set<String> items = new TreeSet<>();
        for (int i = 0; i < count; i++) {
            items.add(sc.next());
        }
        return items;
    }

    static String listOrDash(Set<String> items) {
        return items.isEmpty() ? "-" : String.join(", ", items);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Set<String> ordered = readItems(sc);
        Set<String> delivered = readItems(sc);
        if (ordered.equals(delivered)) {
            System.out.println("Всё верно");
            return;
        }
        Set<String> missing = new TreeSet<>(ordered);
        missing.removeAll(delivered);
        Set<String> extra = new TreeSet<>(delivered);
        extra.removeAll(ordered);
        System.out.println("Не привезли: " + listOrDash(missing));
        System.out.println("Лишнее: " + listOrDash(extra));
    }
}
