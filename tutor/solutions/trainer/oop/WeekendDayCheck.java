import java.util.Scanner;

public class WeekendDayCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.next();
        CalendarDay day = CalendarDay.valueOf(word.toUpperCase());
        System.out.println("День №" + (day.ordinal() + 1) + ": " + day);
        if (day.isWeekend()) {
            System.out.println("Выходной!");
        } else {
            System.out.println("Рабочий день");
        }
    }
}

enum CalendarDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    boolean isWeekend() {
        return this == SATURDAY || this == SUNDAY;
    }
}
