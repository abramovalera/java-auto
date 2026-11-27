import java.util.Scanner;

public class ThermometerSignMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        for (int i = 0; i < k; i++) {
            int t = scanner.nextInt();
            System.out.print(describe(t) + " ");
        }
        System.out.println();
    }

    static String describe(int t) {
        if (t < 0) {
            return "мороз";
        } else if (t == 0) {
            return "ноль";
        }
        return "тепло";
    }
}
