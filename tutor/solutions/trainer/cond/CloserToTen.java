import java.util.Scanner;

public class CloserToTen {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int distA = Math.abs(a - 10);
        int distB = Math.abs(b - 10);
        if (distA < distB) {
            System.out.println(a);
        } else if (distB < distA) {
            System.out.println(b);
        } else {
            System.out.println("Одинаково");
        }
    }
}
