import java.util.Scanner;

public class RestaurantGuestCount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();
        try {
            int guests = Integer.parseInt(line);
            System.out.println("Гостей: " + guests + ", готовим столик");
        } catch (NumberFormatException e) {
            System.out.println("Не понял, сколько вас");
        }
    }
}
