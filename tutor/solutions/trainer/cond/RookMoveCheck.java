import java.util.Scanner;

public class RookMoveCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int c1 = scanner.nextInt();
        int r1 = scanner.nextInt();
        int c2 = scanner.nextInt();
        int r2 = scanner.nextInt();
        boolean sameCell = c1 == c2 && r1 == r2;
        if ((c1 == c2 || r1 == r2) && !sameCell) {
            System.out.println("Может");
        } else {
            System.out.println("Не может");
        }
    }
}
