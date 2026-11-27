import java.util.Scanner;

public class OverloadedMaxScores {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        int z = scanner.nextInt();
        System.out.println("Лучший из двух: " + max(a, b));
        System.out.println("Лучший из трёх: " + max(x, y, z));
    }

    static int max(int a, int b) {
        if (a > b) {
            return a;
        }
        return b;
    }

    static int max(int a, int b, int c) {
        return max(max(a, b), c);
    }
}
