import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ClassPresidentElection {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        if (n == 0) {
            System.out.println("Выборы не состоялись");
            return;
        }
        Map<String, Integer> votes = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            votes.put(name, votes.getOrDefault(name, 0) + 1);
        }
        String winner = null;
        int best = 0;
        for (Map.Entry<String, Integer> e : votes.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
            if (e.getValue() > best) {
                best = e.getValue();
                winner = e.getKey();
            }
        }
        System.out.println("Староста: " + winner);
    }
}
