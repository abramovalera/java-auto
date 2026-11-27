import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class UniqueVisitorsCount {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<String> visitors = new HashSet<>();
        for (int i = 0; i < n; i++) {
            visitors.add(sc.next());
        }
        System.out.println("Уникальных посетителей: " + visitors.size());
    }
}
