import java.util.Scanner;

public class ThreeDayAverageTemp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t1 = scanner.nextInt();
        int t2 = scanner.nextInt();
        int t3 = scanner.nextInt();
        double average = (t1 + t2 + t3) / 3.0;
        System.out.println("Средняя: " + Math.round(average * 10) / 10.0);
    }
}
