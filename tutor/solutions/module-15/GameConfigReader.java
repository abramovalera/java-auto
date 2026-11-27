import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

// Эталон m15-t06: записать настройки игры в config.properties и прочитать значения по ключам через Properties.
public class GameConfigReader {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            lines.add(sc.nextLine());
        }
        Path file = Path.of("config.properties");
        Files.write(file, lines);

        Properties config = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            config.load(reader);
        }

        System.out.println("Настроек: " + config.size());
        int m = Integer.parseInt(sc.nextLine().trim());
        for (int i = 0; i < m; i++) {
            String key = sc.nextLine().trim();
            System.out.println(key + " = " + config.getProperty(key, "не задано"));
        }
    }
}
