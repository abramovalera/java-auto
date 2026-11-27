import java.util.Scanner;

public class StarTree {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int height = scanner.nextInt();
        for (int row = 1; row <= height; row++) {
            for (int s = 1; s <= height - row; s++) {      // пробелы: height - row
                System.out.print(" ");
            }
            for (int star = 1; star <= 2 * row - 1; star++) { // звёздочки: 2 * row - 1
                System.out.print("*");
            }
            System.out.println();
        }
        for (int s = 1; s <= height - 1; s++) {            // ствол — под вершиной
            System.out.print(" ");
        }
        System.out.println("|");
    }
}
