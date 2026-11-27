import java.util.Scanner;

public class NicknameLengthCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nick = scanner.nextLine();
        System.out.println(nick.length());
    }
}
