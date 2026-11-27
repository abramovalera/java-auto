import java.util.Scanner;

public class CheerRepeatLines {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String cheer = scanner.nextLine();
        int n = scanner.nextInt();
        if (n == 0) {
            System.out.println("Тишина");
        }
        for (int i = 1; i <= n; i++) {
            System.out.println(i + ". " + cheer);
        }
    }
}
