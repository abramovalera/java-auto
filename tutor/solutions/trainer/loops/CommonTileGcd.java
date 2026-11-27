import java.util.Scanner;

public class CommonTileGcd {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int width = scanner.nextInt();
        int length = scanner.nextInt();
        int a = width;
        int b = length;
        while (b != 0) {
            int r = a % b;
            a = b;
            b = r;
        }
        long tiles = (long) (width / a) * (length / a);
        System.out.println("Плитка: " + a + ", штук: " + tiles);
    }
}
