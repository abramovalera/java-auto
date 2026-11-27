import java.util.Scanner;

public class ScoreboardSymbols {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();
        String text = "" + number;
        System.out.println("Символов на табло: " + text.length());
    }
}
