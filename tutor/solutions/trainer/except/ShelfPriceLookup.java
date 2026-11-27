import java.util.Scanner;

public class ShelfPriceLookup {
    public static void main(String[] args) {
        int[] prices = {120, 85, 340, 55, 210};
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        try {
            int shelf = Integer.parseInt(line);
            System.out.println("Цена: " + prices[shelf - 1] + " руб.");
        } catch (NumberFormatException e) {
            System.out.println("Номер полки — это число");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Такой полки нет");
        }
    }
}
