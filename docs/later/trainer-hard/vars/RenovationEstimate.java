import java.util.Scanner;

public class RenovationEstimate {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double length1 = scanner.nextDouble();
        double width1 = scanner.nextDouble();
        double length2 = scanner.nextDouble();
        double width2 = scanner.nextDouble();
        int pricePerMeter = scanner.nextInt();
        double area = length1 * width1 + length2 * width2;
        int cost = (int) (area * pricePerMeter);
        System.out.println("Общая площадь: " + area + " кв. м");
        System.out.println("Ремонт обойдётся в " + cost + " руб.");
    }
}
