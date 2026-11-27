import java.util.Scanner;

public class ApartmentNumberInput {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        try {
            int number = Integer.parseInt(line);
            System.out.println("Номер принят: " + number);
        } catch (NumberFormatException e) {
            System.out.println("Это не число");
        }
    }
}
