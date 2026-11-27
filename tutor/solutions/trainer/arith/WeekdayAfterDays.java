import java.util.Scanner;

public class WeekdayAfterDays {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int today = scanner.nextInt();
        int days = scanner.nextInt();
        int result = (today - 1 + days) % 7 + 1;
        System.out.println(result);
    }
}
