import java.util.Scanner;

public class FormDateValidator {
    static final String[] MONTHS = {"января", "февраля", "марта", "апреля", "мая", "июня",
            "июля", "августа", "сентября", "октября", "ноября", "декабря"};

    static boolean isLeap(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    static String parseDate(String text) {
        String[] parts = text.split("\\.");
        if (parts.length != 3 || parts[0].length() != 2 || parts[1].length() != 2 || parts[2].length() != 4) {
            throw new IllegalArgumentException("Формат даты: дд.мм.гггг");
        }
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Месяца " + month + " нет");
        }
        int[] days = {31, isLeap(year) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (day < 1 || day > days[month - 1]) {
            throw new IllegalArgumentException("В этом месяце нет числа " + day);
        }
        return day + " " + MONTHS[month - 1] + " " + year;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < n; i++) {
            String text = scanner.nextLine();
            try {
                System.out.println("Дата верная: " + parseDate(text));
            } catch (NumberFormatException e) {
                System.out.println("В дате только цифры и точки");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
