import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MissingHomeworkList {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<String> journal = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            journal.add(sc.next());
        }
        int m = sc.nextInt();
        List<String> submitted = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            submitted.add(sc.next());
        }
        journal.removeAll(submitted);
        if (journal.isEmpty()) {
            System.out.println("Все сдали");
        }
        for (String name : journal) {
            System.out.println(name);
        }
    }
}
