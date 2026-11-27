import java.util.Scanner;

public class ElevatorDirection {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int current = scanner.nextInt();
        int target = scanner.nextInt();
        if (target > current) {
            System.out.println("Вверх");
        } else if (target < current) {
            System.out.println("Вниз");
        } else {
            System.out.println("Ты уже на этом этаже");
        }
    }
}
