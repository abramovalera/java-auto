import java.util.Scanner;

public class LockCombinationsPower {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int symbols = scanner.nextInt();
        int wheels = scanner.nextInt();
        long combinations = 1;
        for (int i = 1; i <= wheels; i++) {
            combinations *= symbols;
        }
        System.out.println(combinations);
    }
}
