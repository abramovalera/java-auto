import java.util.Scanner;

public class ExcursionBusesNeeded {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int pupils = scanner.nextInt();
        int seats = scanner.nextInt();
        int buses = (pupils + seats - 1) / seats;
        System.out.println("Автобусов: " + buses);
    }
}
