import java.util.Scanner;

public class TriangleShapeKind {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        if (a >= b + c || b >= a + c || c >= a + b) {
            System.out.println("Треугольника нет");
        } else if (a == b && b == c) {
            System.out.println("Равносторонний");
        } else if (a == b || b == c || a == c) {
            System.out.println("Равнобедренный");
        } else {
            System.out.println("Разносторонний");
        }
    }
}
