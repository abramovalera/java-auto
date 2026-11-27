import java.util.Scanner;

public class RetryNumberInput {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int number = readInt(scanner);
        System.out.println("Принято: " + number);
        System.out.println("В два раза больше: " + number * 2);
    }

    static int readInt(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("«" + line + "» — не целое число, попробуй ещё раз");
            }
        }
    }
}
