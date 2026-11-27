import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GuestListLookup {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<String> guests = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            guests.add(sc.next());
        }
        String name = sc.next();
        if (guests.contains(name)) {
            System.out.println("Проходите");
        } else {
            System.out.println("Вас нет в списке");
        }
    }
}
