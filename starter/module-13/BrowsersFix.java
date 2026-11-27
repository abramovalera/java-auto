import java.util.List;
import java.util.Scanner;

public class BrowsersFix {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> browsers = List.of("chrome", "firefox");
        while (true) {
            String name = scanner.next();
            if (name.equals("END")) {
                break;
            }
            browsers.add(name);
        }
        System.out.println(browsers);
    }
}
