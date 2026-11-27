import java.util.Scanner;

public class LongestWeatherStreak {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String[] days = new String[n];
        for (int i = 0; i < n; i++) {
            days[i] = scanner.next();
        }
        String bestWeather = days[0];
        int bestLength = 1;
        int current = 1;
        for (int i = 1; i < n; i++) {
            if (days[i].equals(days[i - 1])) {
                current++;
            } else {
                current = 1;
            }
            if (current > bestLength) {
                bestLength = current;
                bestWeather = days[i];
            }
        }
        System.out.println(bestWeather + " x" + bestLength);
    }
}
