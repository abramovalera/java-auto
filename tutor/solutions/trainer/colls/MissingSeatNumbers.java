import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MissingSeatNumbers {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        int n = sc.nextInt();
        Set<Integer> sold = new HashSet<>();
        for (int i = 0; i < n; i++) {
            sold.add(sc.nextInt());
        }
        StringBuilder free = new StringBuilder();
        for (int seat = 1; seat <= m; seat++) {
            if (!sold.contains(seat)) {
                free.append(" ").append(seat);
            }
        }
        if (free.length() == 0) {
            System.out.println("Мест нет");
        } else {
            System.out.println("Свободны:" + free);
        }
    }
}
