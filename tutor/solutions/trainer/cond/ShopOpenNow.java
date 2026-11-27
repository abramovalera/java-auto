import java.util.Scanner;

public class ShopOpenNow {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int day = scanner.nextInt();
        int hour = scanner.nextInt();
        boolean open;
        switch (day) {
            case 1, 2, 3, 4, 5 -> open = hour >= 9 && hour < 21;
            case 6 -> open = hour >= 10 && hour < 18;
            default -> open = false;
        }
        System.out.println(open ? "Открыто" : "Закрыто");
    }
}
