import java.util.Scanner;

public class LineUpWaysFactorial {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        if (n > 20) {
            System.out.println("Слишком много");
        } else {
            long ways = 1;
            for (int i = 2; i <= n; i++) {
                ways *= i;
            }
            System.out.println(ways);
        }
    }
}
