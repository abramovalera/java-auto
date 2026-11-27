import java.util.Scanner;

public class TemperatureRoundingTrio {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double t = scanner.nextDouble();
        System.out.println("Вниз: " + (int) Math.floor(t));
        System.out.println("Вверх: " + (int) Math.ceil(t));
        System.out.println("По правилам: " + Math.round(t));
    }
}
