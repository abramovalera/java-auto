import java.util.Scanner;

public class ImageFileCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine().toLowerCase();
        if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            System.out.println("картинка");
        } else {
            System.out.println("не картинка");
        }
    }
}
