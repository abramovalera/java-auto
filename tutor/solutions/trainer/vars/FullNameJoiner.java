import java.util.Scanner;

public class FullNameJoiner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String firstName = scanner.nextLine();
        String patronymic = scanner.nextLine();
        String lastName = scanner.nextLine();
        System.out.println(lastName + " " + firstName + " " + patronymic);
    }
}
