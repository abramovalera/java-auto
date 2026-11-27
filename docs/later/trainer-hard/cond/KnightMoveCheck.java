import java.util.Scanner;

public class KnightMoveCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String from = scanner.next();
        String to = scanner.next();
        int dx = Math.abs((to.charAt(0) - 'a') - (from.charAt(0) - 'a'));
        int dy = Math.abs((to.charAt(1) - '0') - (from.charAt(1) - '0'));
        if (dx == 1 && dy == 2 || dx == 2 && dy == 1) {
            System.out.println("Можно");
        } else {
            System.out.println("Нельзя");
        }
    }
}
