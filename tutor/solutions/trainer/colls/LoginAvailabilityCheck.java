import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class LoginAvailabilityCheck {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<String> taken = new HashSet<>();
        for (int i = 0; i < n; i++) {
            taken.add(sc.next().toLowerCase());
        }
        String wanted = sc.next().toLowerCase();
        if (taken.contains(wanted)) {
            System.out.println("Логин занят");
        } else {
            System.out.println("Логин свободен");
        }
    }
}
