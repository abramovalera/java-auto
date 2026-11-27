import java.util.Scanner;

public class PointQuadrantName {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        if (x == 0 && y == 0) {
            System.out.println("Начало координат");
        } else if (y == 0) {
            System.out.println("На оси X");
        } else if (x == 0) {
            System.out.println("На оси Y");
        } else if (x > 0 && y > 0) {
            System.out.println("Первая четверть");
        } else if (x < 0 && y > 0) {
            System.out.println("Вторая четверть");
        } else if (x < 0) {
            System.out.println("Третья четверть");
        } else {
            System.out.println("Четвёртая четверть");
        }
    }
}
