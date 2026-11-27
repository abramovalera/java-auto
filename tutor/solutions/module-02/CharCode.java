// Эталон задачи m02-t06
import java.util.Scanner;

public class CharCode {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введи символ:");
        char symbol = scanner.next().charAt(0);
        int code = symbol;
        char nextSymbol = (char) (code + 1);

        System.out.println("Символ: " + symbol);
        System.out.println("Код: " + code);
        System.out.println("Следующий: " + nextSymbol);
    }
}
