import java.util.Scanner;

public class GameCoinsBalance {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int coins = scanner.nextInt();
        int chest = scanner.nextInt();
        int trap = scanner.nextInt();
        coins += chest;
        System.out.println("После сундука: " + coins);
        coins -= trap;
        System.out.println("После ловушки: " + coins);
        coins *= 2;
        System.out.println("С бустером x2: " + coins);
    }
}
