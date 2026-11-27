import java.util.Scanner;

public class WorkdayOrWeekend {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int day = scanner.nextInt();
        switch (day) {
            case 1, 2, 3, 4, 5 -> System.out.println("Рабочий день");
            case 6, 7 -> System.out.println("Выходной");
            default -> System.out.println("Нет такого дня");
        }
    }
}
