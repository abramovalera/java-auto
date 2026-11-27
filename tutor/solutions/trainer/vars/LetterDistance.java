import java.util.Scanner;

public class LetterDistance {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char first = scanner.next().charAt(0);
        char second = scanner.next().charAt(0);
        int distance = second - first;
        System.out.println("От " + first + " до " + second + ": " + distance);
    }
}
