import java.util.Scanner;

public class WaterStateByTemp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt();
        if (t <= 0) {
            System.out.println("Лёд");
        } else if (t < 100) {
            System.out.println("Вода");
        } else {
            System.out.println("Пар");
        }
    }
}
