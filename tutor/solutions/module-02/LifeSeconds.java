// Эталон задачи m02-t10. Ловушка: long seconds = years * 365 * 24 * 60 * 60;
// считается в int и переполняется ещё до записи в long (на 70 годах — минус).
// Решение: сделать long хотя бы первый множитель.
import java.util.Scanner;

public class LifeSeconds {
    public static void main(String[] args) {
        final int DAYS_IN_YEAR = 365;
        final int HOURS_IN_DAY = 24;
        final int MINUTES_IN_HOUR = 60;
        final int SECONDS_IN_MINUTE = 60;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Сколько тебе лет?");
        long years = scanner.nextInt();

        long seconds = years * DAYS_IN_YEAR * HOURS_IN_DAY * MINUTES_IN_HOUR * SECONDS_IN_MINUTE;
        System.out.println("Прожито секунд: " + seconds);
    }
}
