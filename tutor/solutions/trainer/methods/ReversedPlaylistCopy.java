import java.util.Arrays;
import java.util.Scanner;

public class ReversedPlaylistCopy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] tracks = new int[n];
        for (int i = 0; i < n; i++) {
            tracks[i] = scanner.nextInt();
        }
        int[] reversed = reverse(tracks);
        System.out.println("Было: " + Arrays.toString(tracks));
        System.out.println("Стало: " + Arrays.toString(reversed));
    }

    static int[] reverse(int[] tracks) {
        int[] result = new int[tracks.length];
        for (int i = 0; i < tracks.length; i++) {
            result[i] = tracks[tracks.length - 1 - i];
        }
        return result;
    }
}
