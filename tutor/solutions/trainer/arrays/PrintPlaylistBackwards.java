import java.util.Scanner;

public class PrintPlaylistBackwards {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] songs = new int[n];
        for (int i = 0; i < n; i++) {
            songs[i] = scanner.nextInt();
        }
        for (int i = n - 1; i >= 0; i--) {
            System.out.print(songs[i] + " ");
        }
        System.out.println();
    }
}
