import java.util.Scanner;

public class SecretLetterPick {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        int position = scanner.nextInt();
        try {
            System.out.println("Буква: " + word.charAt(position - 1));
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("В слове нет буквы с номером " + position);
        }
    }
}
