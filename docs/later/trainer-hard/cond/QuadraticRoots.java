import java.util.Scanner;

public class QuadraticRoots {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        int d = b * b - 4 * a * c;
        if (d < 0) {
            System.out.println("Корней нет");
        } else if (d == 0) {
            double x = -b / (2.0 * a);
            System.out.println("Один корень: " + Math.round(x * 100) / 100.0);
        } else {
            double x1 = (-b - Math.sqrt(d)) / (2.0 * a);
            double x2 = (-b + Math.sqrt(d)) / (2.0 * a);
            double small = Math.min(x1, x2);
            double big = Math.max(x1, x2);
            System.out.println("Два корня: " + Math.round(small * 100) / 100.0 + " и " + Math.round(big * 100) / 100.0);
        }
    }
}
