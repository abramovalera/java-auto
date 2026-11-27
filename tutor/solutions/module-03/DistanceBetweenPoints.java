import java.util.Scanner;

public class DistanceBetweenPoints {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int x1 = scanner.nextInt();
        int y1 = scanner.nextInt();
        int x2 = scanner.nextInt();
        int y2 = scanner.nextInt();

        int dx = x2 - x1;
        int dy = y2 - y1;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double rounded = Math.round(distance * 100) / 100.0;

        System.out.println("Расстояние: " + rounded);
    }
}
