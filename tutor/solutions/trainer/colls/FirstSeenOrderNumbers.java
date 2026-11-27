import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class FirstSeenOrderNumbers {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<Integer> houses = new LinkedHashSet<>();
        for (int i = 0; i < n; i++) {
            houses.add(sc.nextInt());
        }
        for (int h : houses) {
            System.out.print(h + " ");
        }
        System.out.println();
    }
}
