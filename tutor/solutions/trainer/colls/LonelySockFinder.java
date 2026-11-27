import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class LonelySockFinder {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<String> waiting = new HashSet<>();
        for (int i = 0; i < n; i++) {
            String color = sc.next();
            if (waiting.contains(color)) {
                waiting.remove(color);
            } else {
                waiting.add(color);
            }
        }
        System.out.println("Без пары: " + waiting.iterator().next());
    }
}
