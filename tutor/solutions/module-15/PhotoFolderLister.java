import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

// Эталон m15-t05: создать папку с файлами и вывести их имена по алфавиту.
public class PhotoFolderLister {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        Path dir = Path.of(sc.nextLine().trim());
        int n = Integer.parseInt(sc.nextLine().trim());

        Files.createDirectories(dir);
        for (int i = 0; i < n; i++) {
            String name = sc.nextLine().trim();
            Files.writeString(dir.resolve(name), "фото " + name);
        }

        List<String> names;
        try (Stream<Path> files = Files.list(dir)) {
            names = files
                    .filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString())
                    .sorted()
                    .toList();
        }

        System.out.println("Папка: " + dir.getFileName());
        System.out.println("Файлов: " + names.size());
        names.forEach(System.out::println);
    }
}
