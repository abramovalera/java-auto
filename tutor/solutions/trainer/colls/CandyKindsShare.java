import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CandyKindsShare {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<Integer> kinds = new HashSet<>();
        for (int i = 0; i < n; i++) {
            kinds.add(sc.nextInt());
        }
        System.out.println("Разных сортов у сестры: " + Math.min(n / 2, kinds.size()));
    }
}
