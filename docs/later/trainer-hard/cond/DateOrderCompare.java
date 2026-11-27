import java.util.Scanner;

public class DateOrderCompare {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int d1 = scanner.nextInt();
        int m1 = scanner.nextInt();
        int y1 = scanner.nextInt();
        int d2 = scanner.nextInt();
        int m2 = scanner.nextInt();
        int y2 = scanner.nextInt();
        int result;
        if (y1 != y2) {
            result = y1 < y2 ? 1 : 2;
        } else if (m1 != m2) {
            result = m1 < m2 ? 1 : 2;
        } else if (d1 != d2) {
            result = d1 < d2 ? 1 : 2;
        } else {
            result = 0;
        }
        if (result == 1) {
            System.out.println("Первая дата раньше");
        } else if (result == 2) {
            System.out.println("Вторая дата раньше");
        } else {
            System.out.println("Даты совпадают");
        }
    }
}
