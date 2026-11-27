import java.util.Scanner;

public class SenderNameAndEmail {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int open = line.indexOf('<');
        String name;
        String email;
        if (open == -1) {
            name = line.trim();
            email = "нет";
        } else {
            int close = line.indexOf('>', open);
            name = line.substring(0, open).trim();
            email = line.substring(open + 1, close).trim();
        }
        if (name.isEmpty()) {
            name = "нет";
        }
        System.out.println("Имя: " + name);
        System.out.println("Почта: " + email);
    }
}
