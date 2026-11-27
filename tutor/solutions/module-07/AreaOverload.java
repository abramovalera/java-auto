import java.util.Scanner;

public class AreaOverload {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double radius = scanner.nextDouble();
        double width = scanner.nextDouble();
        double height = scanner.nextDouble();
        System.out.println("Круг: " + area(radius));
        System.out.println("Прямоугольник: " + area(width, height));
    }

    static double area(double radius) {
        return Math.PI * radius * radius;
    }

    static double area(double width, double height) {
        return width * height;
    }
}
