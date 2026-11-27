import java.util.Scanner;

public class HornSoundRepeat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String sound = scanner.nextLine();
        int n = scanner.nextInt();
        String rest = ("-" + sound.toLowerCase()).repeat(n - 1);
        System.out.println(sound + rest);
    }
}
