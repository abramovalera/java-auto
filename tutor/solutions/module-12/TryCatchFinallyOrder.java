import java.util.Scanner;

public class TryCatchFinallyOrder {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        int result = parseOrZero(text);
        System.out.println("main: результат " + result);
    }

    static int parseOrZero(String text) {
        try {
            System.out.println("try: начало");
            int number = Integer.parseInt(text);
            System.out.println("try: число " + number);
            return number;
        } catch (NumberFormatException e) {
            System.out.println("catch: «" + text + "» не число");
            return 0;
        } finally {
            System.out.println("finally");
        }
    }
}
