import java.util.Scanner;

public class MovieRuntimeSplit {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int totalMinutes = scanner.nextInt();
        int hours = totalMinutes / 60;
        int minutes = totalMinutes - hours * 60;
        System.out.println(hours + " ч " + minutes + " мин");
    }
}
