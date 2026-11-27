import java.util.Scanner;

public class CardDigitFrequency {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String number = scanner.next();
        int[] counts = new int[10];
        for (char c : number.toCharArray()) {
            counts[c - '0']++;
        }
        for (int d = 0; d < 10; d++) {
            if (counts[d] > 0) {
                System.out.println(d + ": " + counts[d]);
            }
        }
    }
}
