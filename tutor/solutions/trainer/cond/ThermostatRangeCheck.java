import java.util.Scanner;

public class ThermostatRangeCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int t = scanner.nextInt();
        int low = Math.min(a, b);
        int high = Math.max(a, b);
        if (t < low) {
            System.out.println("Холодно");
        } else if (t > high) {
            System.out.println("Жарко");
        } else {
            System.out.println("Комфортно");
        }
    }
}
