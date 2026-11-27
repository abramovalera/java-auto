import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

// Задача m15-t03: почини журнал заказов.
// Программа читает число N, потом N строк — записи о заказах — и ведёт журнал orders.log:
// каждая строка дописывается в файл с номером: «1) чайник: оплачен».
// В конце она читает orders.log и печатает, сколько в нём строк, и сами строки.
// Журнал должен содержать записи только ЭТОГО запуска.
// Код писали в спешке: в нём три ошибки. Найди и исправь их, не переписывая программу заново.
public class OrderLogAppendFix {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        Path log = Path.of("orders.log");

        for (int i = 1; i <= n; i++) {
            String line = sc.nextLine();
            Files.writeString(log, i + ") " + line + "\n");
        }

        List<String> lines = Files.readAllLines(log);
        System.out.println("Строк в журнале: " + lines.size());
        for (String l : lines) {
            System.out.println(l);
        }
    }
}
