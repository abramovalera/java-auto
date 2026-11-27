import java.util.Scanner;

public class ThreeWordsBackwards {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String first = scanner.next();
        String second = scanner.next();
        String third = scanner.next();
        System.out.println(third + " " + second + " " + first);
    }
}
