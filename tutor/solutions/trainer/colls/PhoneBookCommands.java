import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class PhoneBookCommands {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, String> book = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            String command = sc.next();
            String name = sc.next();
            if (command.equals("add")) {
                String old = book.put(name, sc.next());
                System.out.println((old == null ? "Добавлен: " : "Обновлён: ") + name);
            } else if (command.equals("find")) {
                String phone = book.get(name);
                System.out.println(phone == null ? "Не найден: " + name : phone);
            } else if (command.equals("remove")) {
                String old = book.remove(name);
                System.out.println((old == null ? "Не найден: " : "Удалён: ") + name);
            }
        }
        if (book.isEmpty()) {
            System.out.println("Книжка пуста");
            return;
        }
        System.out.println("Книжка:");
        for (Map.Entry<String, String> e : book.entrySet()) {
            System.out.println(e.getKey() + " " + e.getValue());
        }
    }
}
