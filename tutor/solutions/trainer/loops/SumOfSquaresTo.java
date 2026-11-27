import java.util.Scanner;

public class SumOfSquaresTo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long total = 0;
        for (int layer = 1; layer <= n; layer++) {
            total += (long) layer * layer;
        }
        System.out.println(total);
    }
}
