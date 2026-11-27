import java.util.Scanner;

public class TwoDigitCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int abs = Math.abs(n);
        System.out.println(abs >= 10 && abs <= 99);
    }
}
