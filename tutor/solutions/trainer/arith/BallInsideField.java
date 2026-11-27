import java.util.Scanner;

public class BallInsideField {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int width = scanner.nextInt();
        int length = scanner.nextInt();
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        boolean inside = x >= 0 && x <= width && y >= 0 && y <= length;
        System.out.println(inside);
    }
}
