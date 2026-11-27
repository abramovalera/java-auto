import java.util.Scanner;

public class LoginGateCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String login = scanner.nextLine();
        String password = scanner.nextLine();
        if (login.equals("admin")) {
            if (password.equals("qwerty123")) {
                System.out.println("Добро пожаловать, admin!");
            } else {
                System.out.println("Неверный пароль");
            }
        } else if (login.equals("guest")) {
            if (password.equals("guest")) {
                System.out.println("Добро пожаловать, guest!");
            } else {
                System.out.println("Неверный пароль");
            }
        } else {
            System.out.println("Нет такого пользователя");
        }
    }
}
