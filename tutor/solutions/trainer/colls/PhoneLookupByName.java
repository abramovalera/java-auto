import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PhoneLookupByName {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, String> phones = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            String phone = sc.next();
            phones.put(name, phone);
        }
        String query = sc.next();
        if (phones.containsKey(query)) {
            System.out.println(phones.get(query));
        } else {
            System.out.println("Контакт не найден");
        }
    }
}
