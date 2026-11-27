import java.util.Scanner;

public class FeverCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double temperature = scanner.nextDouble();
        if (temperature > 37.0) {
            System.out.println("Жар");
        } else {
            System.out.println("Норма");
        }
    }
}
