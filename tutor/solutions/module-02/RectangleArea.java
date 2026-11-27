// Эталон задачи m02-t03
import java.util.Scanner;

public class RectangleArea {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ширина:");
        int width = scanner.nextInt();
        System.out.println("Длина:");
        int length = scanner.nextInt();

        int area = width * length;
        int perimeter = 2 * (width + length);

        System.out.println("Площадь: " + area);
        System.out.println("Периметр: " + perimeter);
    }
}
