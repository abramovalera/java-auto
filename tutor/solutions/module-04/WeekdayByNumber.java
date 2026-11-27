import java.util.Scanner;

public class WeekdayByNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int day = scanner.nextInt();
        String name = switch (day) {
            case 1 -> "Понедельник";
            case 2 -> "Вторник";
            case 3 -> "Среда";
            case 4 -> "Четверг";
            case 5 -> "Пятница";
            case 6 -> "Суббота";
            case 7 -> "Воскресенье";
            default -> "";
        };
        if (name.isEmpty()) {          // можно и day < 1 || day > 7
            System.out.println("Нет такого дня");
        } else {
            System.out.println(name);
            switch (day) {
                case 6, 7 -> System.out.println("Выходной");
                default -> System.out.println("Рабочий день");
            }
        }
    }
}
