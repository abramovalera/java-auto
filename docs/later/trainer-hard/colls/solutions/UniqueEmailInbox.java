import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class UniqueEmailInbox {
    static String normalize(String email) {
        email = email.toLowerCase();
        int at = email.indexOf('@');
        String local = email.substring(0, at);
        String domain = email.substring(at);
        int plus = local.indexOf('+');
        if (plus >= 0) {
            local = local.substring(0, plus);
        }
        return local.replace(".", "") + domain;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<String> boxes = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            boxes.add(normalize(sc.next()));
        }
        System.out.println("Ящиков: " + boxes.size());
        for (String box : boxes) {
            System.out.println(box);
        }
    }
}
