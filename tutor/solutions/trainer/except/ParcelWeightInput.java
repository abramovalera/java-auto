import java.util.Scanner;

public class ParcelWeightInput {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        try {
            double weight = Double.parseDouble(line);
            System.out.println("Вес посылки: " + weight + " кг");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: вес вводится числом через точку");
        }
    }
}
