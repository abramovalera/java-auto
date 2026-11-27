import java.util.Scanner;

public class CaloriesLeftToday {
    public static void main(String[] args) {
        final int DAILY_NORM = 2000;
        Scanner scanner = new Scanner(System.in);
        int breakfast = scanner.nextInt();
        int lunch = scanner.nextInt();
        int dinner = scanner.nextInt();
        int eaten = breakfast + lunch + dinner;
        System.out.println("Съедено: " + eaten);
        System.out.println("Осталось до нормы: " + (DAILY_NORM - eaten));
    }
}
