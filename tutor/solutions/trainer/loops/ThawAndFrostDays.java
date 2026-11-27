import java.util.Scanner;

public class ThawAndFrostDays {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int warm = 0;
        int frosty = 0;
        for (int i = 0; i < n; i++) {
            int t = scanner.nextInt();
            if (t > 0) {
                warm++;
            } else if (t < 0) {
                frosty++;
            }
        }
        System.out.println("Тёплых: " + warm + ", морозных: " + frosty);
    }
}
