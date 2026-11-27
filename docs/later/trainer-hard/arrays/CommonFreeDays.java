import java.util.Scanner;

public class CommonFreeDays {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean[] first = new boolean[32];
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            first[scanner.nextInt()] = true;
        }
        boolean[] second = new boolean[32];
        int m = scanner.nextInt();
        for (int i = 0; i < m; i++) {
            second[scanner.nextInt()] = true;
        }
        boolean found = false;
        for (int day = 1; day <= 31; day++) {
            if (first[day] && second[day]) {
                System.out.print(day + " ");
                found = true;
            }
        }
        if (found) {
            System.out.println();
        } else {
            System.out.println("Общих дней нет");
        }
    }
}
