import java.util.Scanner;

public class NameCapitalized {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        String result = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        System.out.println(result);
    }
}
