import java.util.Scanner;

public class PizzaPartyDivision {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int friends = scanner.nextInt();
        int slicesEach = scanner.nextInt();

        final int SLICES_IN_PIZZA = 8;
        int slicesNeeded = friends * slicesEach;
        // Деление с округлением ВВЕРХ без if: добавляем (8 - 1) перед делением
        int pizzas = (slicesNeeded + SLICES_IN_PIZZA - 1) / SLICES_IN_PIZZA;
        int leftover = pizzas * SLICES_IN_PIZZA - slicesNeeded;

        System.out.println("Пицц: " + pizzas);
        System.out.println("Останется кусков: " + leftover);
    }
}
