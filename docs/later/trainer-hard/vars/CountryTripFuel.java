import java.util.Scanner;

public class CountryTripFuel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int distance = scanner.nextInt();
        double litersPer100 = scanner.nextDouble();
        double pricePerLiter = scanner.nextDouble();
        int totalKm = distance * 2;
        double liters = totalKm * litersPer100 / 100;
        int cost = (int) (liters * pricePerLiter);
        System.out.println("Туда и обратно: " + totalKm + " км");
        System.out.println("Бензин обойдётся в " + cost + " руб.");
    }
}
