import java.util.Scanner;

public class SymbolCodeNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char symbol = scanner.next().charAt(0);
        int code = symbol;
        System.out.println("Код символа " + symbol + ": " + code);
    }
}
