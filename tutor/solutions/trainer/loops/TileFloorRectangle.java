import java.util.Scanner;

public class TileFloorRectangle {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                System.out.print("#");
            }
            System.out.println();
        }
    }
}
