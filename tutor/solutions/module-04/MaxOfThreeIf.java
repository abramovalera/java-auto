import java.util.Scanner;

public class MaxOfThreeIf {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        int max = a;                        // рекордсмен стартует с реального числа, не с 0
        if (b > max) {
            max = b;
        }
        if (c > max) {
            max = c;
        }
        System.out.println("Максимум: " + max);
    }
}
