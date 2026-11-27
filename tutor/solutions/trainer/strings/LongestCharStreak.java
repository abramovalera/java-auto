import java.util.Scanner;

public class LongestCharStreak {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String days = scanner.nextLine();
        char bestChar = days.charAt(0);
        int best = 1;
        int current = 1;
        for (int i = 1; i < days.length(); i++) {
            if (days.charAt(i) == days.charAt(i - 1)) {
                current++;
            } else {
                current = 1;
            }
            if (current > best) {
                best = current;
                bestChar = days.charAt(i);
            }
        }
        System.out.println(bestChar + " " + best);
    }
}
