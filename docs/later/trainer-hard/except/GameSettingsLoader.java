import java.util.Scanner;

public class GameSettingsLoader {
    static int volume = 50;
    static String difficulty = "normal";
    static int fps = 60;

    static int parseInRange(String key, String value, int min, int max) {
        int number;
        try {
            number = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(key + ": нужно целое число");
        }
        if (number < min || number > max) {
            throw new IllegalArgumentException(key + ": можно от " + min + " до " + max);
        }
        return number;
    }

    static void apply(String line) throws GameSettingUnknownException {
        int eq = line.indexOf("=");
        if (eq < 0) {
            throw new IllegalArgumentException("нет знака =");
        }
        String key = line.substring(0, eq).trim();
        String value = line.substring(eq + 1).trim();
        switch (key) {
            case "volume":
                volume = parseInRange(key, value, 0, 100);
                break;
            case "fps":
                fps = parseInRange(key, value, 30, 240);
                break;
            case "difficulty":
                if (!value.equals("easy") && !value.equals("normal") && !value.equals("hard")) {
                    throw new IllegalArgumentException("difficulty: можно easy, normal или hard");
                }
                difficulty = value;
                break;
            default:
                throw new GameSettingUnknownException("неизвестная настройка " + key);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        int errors = 0;
        for (int i = 1; i <= n; i++) {
            String line = scanner.nextLine();
            try {
                apply(line);
            } catch (IllegalArgumentException | GameSettingUnknownException e) {
                System.out.println("Строка " + i + ": " + e.getMessage());
                errors++;
            }
        }
        System.out.println("volume=" + volume);
        System.out.println("difficulty=" + difficulty);
        System.out.println("fps=" + fps);
        System.out.println("Ошибок: " + errors);
    }
}

class GameSettingUnknownException extends Exception {
    GameSettingUnknownException(String message) {
        super(message);
    }
}
