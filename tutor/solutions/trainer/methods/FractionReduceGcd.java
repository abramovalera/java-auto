import java.util.Scanner;

public class FractionReduceGcd {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int g = gcd(a, b);
        System.out.println(a + "/" + b + " = " + (a / g) + "/" + (b / g));
    }

    static int gcd(int a, int b) {
        while (b != 0) {
            int r = a % b;
            a = b;
            b = r;
        }
        return a;
    }
}
