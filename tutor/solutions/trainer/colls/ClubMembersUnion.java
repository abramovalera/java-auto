import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class ClubMembersUnion {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Set<String> all = new TreeSet<>();
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            all.add(sc.next());
        }
        int m = sc.nextInt();
        for (int i = 0; i < m; i++) {
            all.add(sc.next());
        }
        for (String name : all) {
            System.out.println(name);
        }
        System.out.println("Всего: " + all.size());
    }
}
