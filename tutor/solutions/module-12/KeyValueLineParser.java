import java.util.Scanner;

public class KeyValueLineParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = Integer.parseInt(scanner.nextLine().trim());
        int good = 0;
        int bad = 0;
        for (int i = 1; i <= count; i++) {
            String line = scanner.nextLine();
            try {
                parseLine(line);
                good++;
            } catch (IllegalArgumentException e) {
                System.out.println("Строка " + i + ": " + e.getMessage());
                bad++;
            }
        }
        System.out.println("Итого: верных " + good + ", с ошибками " + bad);
    }

    static void parseLine(String line) {
        int eq = line.indexOf('=');
        if (eq == -1) {
            throw new IllegalArgumentException("нет знака =");
        }
        String key = line.substring(0, eq).trim();
        String valueText = line.substring(eq + 1).trim();
        if (key.isEmpty()) {
            throw new IllegalArgumentException("пустой ключ");
        }
        int value;
        try {
            value = Integer.parseInt(valueText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("значение «" + valueText + "» не число");
        }
        System.out.println(key + " -> " + value);
    }
}
