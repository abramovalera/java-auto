import java.util.Scanner;

public class CharKindDetector {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char c = scanner.next().charAt(0);
        if (c >= '0' && c <= '9') {
            System.out.println("Цифра");
        } else if (c >= 'A' && c <= 'Z') {
            System.out.println("Заглавная латинская буква");
        } else if (c >= 'a' && c <= 'z') {
            System.out.println("Строчная латинская буква");
        } else {
            System.out.println("Другой символ");
        }
    }
}
