import java.util.Scanner;

public class LongJumpBestWorst {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        int best = Math.max(a, Math.max(b, c));
        int worst = Math.min(a, Math.min(b, c));
        System.out.println("Лучший: " + best);
        System.out.println("Худший: " + worst);
    }
}
