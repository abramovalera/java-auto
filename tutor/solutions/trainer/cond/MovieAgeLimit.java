import java.util.Scanner;

public class MovieAgeLimit {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int age = scanner.nextInt();
        boolean allowed = age >= 16;
        System.out.println(allowed);
    }
}
