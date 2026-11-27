import java.util.Scanner;

public class SpeedCameraFine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int limit = scanner.nextInt();
        int speed = scanner.nextInt();
        int over = speed - limit;
        if (over <= 20) {
            System.out.println("Без штрафа");
        } else if (over <= 40) {
            System.out.println("Штраф: 500 ₽");
        } else if (over <= 60) {
            System.out.println("Штраф: 1000 ₽");
        } else {
            System.out.println("Штраф: 2500 ₽");
        }
    }
}
