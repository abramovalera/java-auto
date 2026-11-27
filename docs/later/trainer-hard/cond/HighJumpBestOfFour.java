import java.util.Scanner;

public class HighJumpBestOfFour {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        int d = scanner.nextInt();
        int best = a;
        int number = 1;
        if (b > best) {
            best = b;
            number = 2;
        }
        if (c > best) {
            best = c;
            number = 3;
        }
        if (d > best) {
            best = d;
            number = 4;
        }
        if (best == 0) {
            System.out.println("Ни одной удачной попытки");
        } else {
            System.out.println("Лучшая попытка: №" + number + ", " + best + " см");
        }
    }
}
