import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class PhotoTagsSorted {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<String> tags = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            tags.add(sc.next());
        }
        if (tags.isEmpty()) {
            System.out.println("Теги: нет");
        } else {
            System.out.println("Теги: " + String.join(", ", tags));
        }
    }
}
