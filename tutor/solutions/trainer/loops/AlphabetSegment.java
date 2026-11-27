import java.util.Scanner;

public class AlphabetSegment {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char from = scanner.next().charAt(0);
        char to = scanner.next().charAt(0);
        if (from <= to) {
            for (char c = from; c <= to; c++) {
                System.out.print(c + " ");
            }
        } else {
            for (char c = from; c >= to; c--) {
                System.out.print(c + " ");
            }
        }
        System.out.println();
    }
}
