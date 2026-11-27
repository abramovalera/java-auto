import java.util.Scanner;

public class WordBackwards {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        System.out.println(new StringBuilder(text).reverse().toString());
    }
}
