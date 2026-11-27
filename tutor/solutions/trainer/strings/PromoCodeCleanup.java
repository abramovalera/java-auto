import java.util.Scanner;

public class PromoCodeCleanup {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String code = scanner.nextLine().strip().toUpperCase();
        if (code.isEmpty()) {
            System.out.println("Промокод не введён");
        } else {
            System.out.println("Промокод: " + code);
        }
    }
}
