import java.util.Scanner;

public class SpeakerSettingsDefaults {
    static int readLevel(String text, int defaultValue) {
        try {
            int value = Integer.parseInt(text);
            if (value < 0 || value > 100) {
                return defaultValue;
            }
            return value;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int volume = readLevel(scanner.nextLine(), 50);
        int brightness = readLevel(scanner.nextLine(), 80);
        System.out.println("Громкость: " + volume + ", яркость: " + brightness);
    }
}
