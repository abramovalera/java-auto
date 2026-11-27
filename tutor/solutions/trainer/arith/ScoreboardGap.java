import java.util.Scanner;

public class ScoreboardGap {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int home = scanner.nextInt();
        int guests = scanner.nextInt();
        System.out.println("Разница в счёте: " + Math.abs(home - guests));
    }
}
