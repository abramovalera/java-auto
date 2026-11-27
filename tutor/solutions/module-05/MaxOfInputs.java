import java.util.Scanner;

public class MaxOfInputs {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int max = scanner.nextInt();           // рекордсмен — первое число, а не 0
        for (int i = 2; i <= n; i++) {         // остальные n - 1 чисел
            int x = scanner.nextInt();
            if (x > max) {
                max = x;
            }
        }
        System.out.println(max);
    }
}
