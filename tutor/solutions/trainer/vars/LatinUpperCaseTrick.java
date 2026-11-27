import java.util.Scanner;

public class LatinUpperCaseTrick {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char letter = scanner.next().charAt(0);
        char upper = (char) (letter - 32);
        System.out.println(letter + " -> " + upper);
    }
}
