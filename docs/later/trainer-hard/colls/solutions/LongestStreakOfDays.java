import java.util.Scanner;
import java.util.TreeSet;

public class LongestStreakOfDays {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        TreeSet<Integer> days = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            days.add(sc.nextInt());
        }
        int bestLen = 0;
        int bestStart = 0;
        for (int day : days) {
            if (days.contains(day - 1)) {
                continue;
            }
            int len = 1;
            while (days.contains(day + len)) {
                len++;
            }
            if (len > bestLen) {
                bestLen = len;
                bestStart = day;
            }
        }
        System.out.println("Серия: " + bestLen);
        System.out.println("С " + bestStart + " по " + (bestStart + bestLen - 1) + " день");
    }
}
