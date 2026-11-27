import java.util.Scanner;

public class LeapYearsBetween {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int from = scanner.nextInt();
        int to = scanner.nextInt();
        int count = 0;
        for (int year = from; year <= to; year++) {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                count++;
            }
        }
        System.out.println(count);
    }
}
