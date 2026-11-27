import java.util.Scanner;

public class SongNowPlaying {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var title = scanner.nextLine();
        var artist = scanner.nextLine();
        System.out.println("Сейчас играет: \"" + title + "\" — " + artist);
    }
}
