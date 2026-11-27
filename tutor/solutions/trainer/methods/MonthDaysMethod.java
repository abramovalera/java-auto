import java.util.Scanner;

public class MonthDaysMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int year = scanner.nextInt();
        int total = 0;
        for (int month = 1; month <= 12; month++) {
            int days = daysInMonth(month, year);
            System.out.print(days + " ");
            total += days;
        }
        System.out.println();
        System.out.println("Всего: " + total);
    }

    static boolean isLeap(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    static int daysInMonth(int month, int year) {
        switch (month) {
            case 2:
                return isLeap(year) ? 29 : 28;
            case 4, 6, 9, 11:
                return 30;
            default:
                return 31;
        }
    }
}
