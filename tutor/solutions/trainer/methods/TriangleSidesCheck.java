import java.util.Scanner;

public class TriangleSidesCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        if (isTriangle(a, b, c)) {
            System.out.println("Треугольник построить можно");
        } else {
            System.out.println("Треугольник построить нельзя");
        }
    }

    static boolean isTriangle(int a, int b, int c) {
        return a < b + c && b < a + c && c < a + b;
    }
}
