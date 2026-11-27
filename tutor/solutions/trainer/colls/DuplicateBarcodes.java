import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class DuplicateBarcodes {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<Integer> seen = new HashSet<>();
        Set<Integer> repeated = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            int code = sc.nextInt();
            if (!seen.add(code)) {
                repeated.add(code);
            }
        }
        if (repeated.isEmpty()) {
            System.out.println("Повторов нет");
            return;
        }
        System.out.print("Повторы:");
        for (int code : repeated) {
            System.out.print(" " + code);
        }
        System.out.println();
    }
}
