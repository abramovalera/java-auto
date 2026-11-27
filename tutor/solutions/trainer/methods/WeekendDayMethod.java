import java.util.Scanner;

public class WeekendDayMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        for (int i = 0; i < k; i++) {
            int day = scanner.nextInt();
            if (isWeekend(day)) {
                System.out.println("День " + day + ": выходной");
            } else {
                System.out.println("День " + day + ": рабочий");
            }
        }
    }

    static boolean isWeekend(int day) {
        return day == 6 || day == 7;
    }
}
