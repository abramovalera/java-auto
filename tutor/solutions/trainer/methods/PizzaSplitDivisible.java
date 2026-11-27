import java.util.Scanner;

public class PizzaSplitDivisible {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int slices = scanner.nextInt();
        int friends = scanner.nextInt();
        System.out.println(canSplitEvenly(slices, friends));
    }

    static boolean canSplitEvenly(int slices, int friends) {
        return slices % friends == 0;
    }
}
