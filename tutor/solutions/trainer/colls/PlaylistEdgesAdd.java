import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlaylistEdgesAdd {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<String> songs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            songs.add(sc.next());
        }
        songs.add(0, sc.next());
        songs.add(sc.next());
        for (int i = 0; i < songs.size(); i++) {
            System.out.println((i + 1) + ". " + songs.get(i));
        }
    }
}
