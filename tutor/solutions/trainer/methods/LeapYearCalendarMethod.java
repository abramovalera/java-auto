import java.util.Scanner;

public class LeapYearCalendarMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        for (int i = 0; i < k; i++) {
            int year = scanner.nextInt();
            int days = isLeapYear(year) ? 366 : 365;
            System.out.println(year + ": " + days + " дней");
        }
    }

    static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
}
