import java.util.Scanner;

public class SilverMedalScore {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] scores = new int[n];
        for (int i = 0; i < n; i++) {
            scores[i] = scanner.nextInt();
        }
        int max = scores[0];
        for (int s : scores) {
            if (s > max) {
                max = s;
            }
        }
        boolean found = false;
        int second = 0;
        for (int s : scores) {
            if (s < max && (!found || s > second)) {
                second = s;
                found = true;
            }
        }
        if (found) {
            System.out.println("Серебро: " + second);
        } else {
            System.out.println("Серебра нет");
        }
    }
}
