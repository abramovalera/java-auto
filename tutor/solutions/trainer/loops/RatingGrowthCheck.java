import java.util.Scanner;

public class RatingGrowthCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int previous = scanner.nextInt();
        int dropMonth = 0;
        for (int month = 2; month <= n; month++) {
            int rating = scanner.nextInt();
            if (rating < previous) {
                dropMonth = month;
                break;
            }
            previous = rating;
        }
        if (dropMonth == 0) {
            System.out.println("Без спадов");
        } else {
            System.out.println("Спад в месяце " + dropMonth);
        }
    }
}
