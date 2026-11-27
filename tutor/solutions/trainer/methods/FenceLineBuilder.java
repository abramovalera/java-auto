import java.util.Scanner;

public class FenceLineBuilder {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char symbol = scanner.next().charAt(0);
        int length = scanner.nextInt();
        System.out.println("|" + buildLine(symbol, length) + "|");
    }

    static String buildLine(char symbol, int length) {
        String line = "";
        for (int i = 0; i < length; i++) {
            line = line + symbol;
        }
        return line;
    }
}
