import java.util.Scanner;

public class SecondsToHoursMinutes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int totalSeconds = scanner.nextInt();
        int hours = totalSeconds / 3600;
        int minutes = totalSeconds % 3600 / 60;
        int seconds = totalSeconds % 60;
        System.out.println(hours + " ч " + minutes + " мин " + seconds + " с");
    }
}
