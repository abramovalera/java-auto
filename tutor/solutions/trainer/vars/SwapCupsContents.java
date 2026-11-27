import java.util.Scanner;

public class SwapCupsContents {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String left = scanner.nextLine();
        String right = scanner.nextLine();
        System.out.println("Было: слева " + left + ", справа " + right);
        String temp = left;
        left = right;
        right = temp;
        System.out.println("Стало: слева " + left + ", справа " + right);
    }
}
