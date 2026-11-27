import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class HasRepeatedNumber {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < n; i++) {
            int ticket = sc.nextInt();
            if (!seen.add(ticket)) {
                System.out.println("Есть повтор: " + ticket);
                return;
            }
        }
        System.out.println("Повторов нет");
    }
}
