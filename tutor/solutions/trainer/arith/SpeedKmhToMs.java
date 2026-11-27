import java.util.Scanner;

public class SpeedKmhToMs {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int kmh = scanner.nextInt();
        double ms = kmh / 3.6;
        System.out.println("Скорость: " + Math.round(ms * 100) / 100.0 + " м/с");
    }
}
