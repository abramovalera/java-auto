import java.util.Scanner;

public class FreeMuseumTicket {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int age = scanner.nextInt();
        boolean free = age < 7 || age >= 65;
        System.out.println(free);
    }
}
