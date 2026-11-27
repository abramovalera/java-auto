import java.util.Scanner;

public class SameLoginIgnoreCase {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String saved = scanner.nextLine().strip();
        String typed = scanner.nextLine().strip();
        System.out.println(saved.equalsIgnoreCase(typed));
    }
}
