import java.util.Scanner;

public class BallInsideFieldZone {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double x = scanner.nextDouble();
        double y = scanner.nextDouble();
        if (x >= 0 && x <= 100 && y >= 0 && y <= 60) {
            System.out.println("В поле");
        } else {
            System.out.println("Аут");
        }
    }
}
