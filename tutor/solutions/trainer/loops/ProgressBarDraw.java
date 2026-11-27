import java.util.Scanner;

public class ProgressBarDraw {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int percent = scanner.nextInt();
        int filled = percent / 10;
        System.out.print("[");
        for (int i = 0; i < filled; i++) {
            System.out.print("#");
        }
        for (int i = filled; i < 10; i++) {
            System.out.print(".");
        }
        System.out.println("] " + percent + "%");
    }
}
