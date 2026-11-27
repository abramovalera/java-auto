import java.util.Scanner;

public class BasketballPoints {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int twoPointers = scanner.nextInt();
        int threePointers = scanner.nextInt();
        int freeThrows = scanner.nextInt();
        int points = twoPointers * 2 + threePointers * 3 + freeThrows;
        System.out.println("Очков: " + points);
    }
}
