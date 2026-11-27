import java.util.Scanner;

public class PowerRecursionMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int base = scanner.nextInt();
        int exp = scanner.nextInt();
        System.out.println(base + "^" + exp + " = " + power(base, exp));
    }

    static long power(int base, int exp) {
        if (exp == 0) {
            return 1;
        }
        return base * power(base, exp - 1);
    }
}
