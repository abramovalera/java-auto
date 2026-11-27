import java.util.Scanner;

public class BookReadingDays {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int pages = scanner.nextInt();
        int perDay = scanner.nextInt();
        int read = 0;
        int days = 0;
        while (read < pages) {
            read += perDay;
            perDay++;
            days++;
        }
        System.out.println(days);
    }
}
