import java.util.Scanner;

public class PerfectSquaresUpTo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = 1;
        while (k * k <= n) {
            System.out.print(k * k + " ");
            k++;
        }
        System.out.println();
    }
}
