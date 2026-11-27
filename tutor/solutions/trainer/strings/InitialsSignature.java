import java.util.Scanner;

public class InitialsSignature {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] parts = scanner.nextLine().strip().split("\\s+");
        String surname = parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1).toLowerCase();
        String nameInitial = parts[1].substring(0, 1).toUpperCase();
        String patronymicInitial = parts[2].substring(0, 1).toUpperCase();
        System.out.println(surname + " " + nameInitial + ". " + patronymicInitial + ".");
    }
}
