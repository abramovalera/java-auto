import java.util.Scanner;

public class TaxiRideFare {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int pickup = scanner.nextInt();
        double pricePerKm = scanner.nextDouble();
        double distance = scanner.nextDouble();
        double total = pickup + pricePerKm * distance;
        System.out.println("К оплате: " + (int) total + " руб.");
    }
}
