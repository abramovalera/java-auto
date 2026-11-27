import java.util.Scanner;

public class DefangedAddress {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String address = scanner.nextLine();
        System.out.println(address.replace(".", "[.]"));
    }
}
