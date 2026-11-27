import java.util.Scanner;

public class RoundFlowerbedArea {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double r = scanner.nextDouble();
        double area = Math.PI * r * r;
        System.out.println("Площадь: " + Math.round(area * 100) / 100.0);
    }
}
