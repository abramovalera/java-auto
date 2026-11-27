import java.util.Scanner;

public class ShapeAreaOverload {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String shape = scanner.next();
        if (shape.equals("квадрат")) {
            System.out.println("Площадь: " + area(scanner.nextInt()));
        } else if (shape.equals("прямоугольник")) {
            int width = scanner.nextInt();
            int height = scanner.nextInt();
            System.out.println("Площадь: " + area(width, height));
        } else {
            double s = area(scanner.nextDouble());
            System.out.println("Площадь: " + Math.round(s * 100) / 100.0);
        }
    }

    static int area(int side) {
        return side * side;
    }

    static int area(int width, int height) {
        return width * height;
    }

    static double area(double radius) {
        return Math.PI * radius * radius;
    }
}
