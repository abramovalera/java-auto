import java.util.Scanner;

public class BinaryStringToNumber {
    static int binaryToInt(String bits) {
        int result = 0;
        for (int i = 0; i < bits.length(); i++) {
            result = result * 2 + (bits.charAt(i) - '0');
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String bits = scanner.nextLine();
        System.out.println(binaryToInt(bits));
    }
}
