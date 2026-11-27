import java.util.Scanner;

public class FactorialLoop {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long factorial = 1;                // накопитель произведения стартует с 1, а не с 0
        for (int i = 2; i <= n; i++) {
            factorial *= i;
        }
        System.out.println(factorial);
    }
}
