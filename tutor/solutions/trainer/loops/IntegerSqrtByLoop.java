import java.util.Scanner;

public class IntegerSqrtByLoop {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long chairs = scanner.nextInt();
        long side = 0;
        while ((side + 1) * (side + 1) <= chairs) {
            side++;
        }
        System.out.println("Сторона: " + side + ", лишних: " + (chairs - side * side));
    }
}
