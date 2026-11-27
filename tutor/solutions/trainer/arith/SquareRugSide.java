import java.util.Scanner;

public class SquareRugSide {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double area = scanner.nextDouble();
        double side = Math.sqrt(area);
        System.out.println("Сторона: " + Math.round(side * 100) / 100.0 + " м");
    }
}
