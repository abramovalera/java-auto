import java.util.Scanner;

public class InitialsFromFullName {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] parts = sc.nextLine().trim().split("\\s+");
        String surname = parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1).toLowerCase();
        char name = Character.toUpperCase(parts[1].charAt(0));
        char patronymic = Character.toUpperCase(parts[2].charAt(0));
        System.out.println(surname + " " + name + ". " + patronymic + ".");
    }
}
