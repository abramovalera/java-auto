import java.util.Scanner;

public class KopecksToRubles {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int priceKop = scanner.nextInt();
        int count = scanner.nextInt();
        int total = priceKop * count;
        System.out.println(total / 100 + " руб. " + total % 100 + " коп.");
    }
}
