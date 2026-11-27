import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class MutualFriendsFinder {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Set<String> anya = new TreeSet<>();
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            anya.add(sc.next());
        }
        Set<String> borya = new TreeSet<>();
        int m = sc.nextInt();
        for (int i = 0; i < m; i++) {
            borya.add(sc.next());
        }
        anya.retainAll(borya);
        if (anya.isEmpty()) {
            System.out.println("Общих друзей нет");
        } else {
            System.out.println("Общие друзья: " + String.join(", ", anya));
        }
    }
}
