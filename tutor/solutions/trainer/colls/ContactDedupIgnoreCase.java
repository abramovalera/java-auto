import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ContactDedupIgnoreCase {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<String> seen = new HashSet<>();
        List<String> contacts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            if (seen.add(name.toLowerCase())) {
                contacts.add(name);
            }
        }
        for (String name : contacts) {
            System.out.println(name);
        }
        System.out.println("Удалено дублей: " + (n - contacts.size()));
    }
}
