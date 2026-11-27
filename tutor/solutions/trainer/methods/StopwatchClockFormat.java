import java.util.Scanner;

public class StopwatchClockFormat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int total = scanner.nextInt();
        int minutes = scanner.nextInt();
        int seconds = scanner.nextInt();
        System.out.println("Круг 1: " + formatTime(total));
        System.out.println("Круг 2: " + formatTime(minutes, seconds));
    }

    static String twoDigits(int n) {
        if (n < 10) {
            return "0" + n;
        }
        return "" + n;
    }

    static String formatTime(int totalSeconds) {
        int h = totalSeconds / 3600;
        int m = totalSeconds % 3600 / 60;
        int s = totalSeconds % 60;
        return h + ":" + twoDigits(m) + ":" + twoDigits(s);
    }

    static String formatTime(int minutes, int seconds) {
        return formatTime(minutes * 60 + seconds);
    }
}
