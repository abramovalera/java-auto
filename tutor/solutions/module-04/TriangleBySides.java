import java.util.Scanner;

public class TriangleBySides {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        // 1) некорректные случаи — первыми; 2) от узкого (все равны) к широкому (хотя бы две равны)
        if (a <= 0 || b <= 0 || c <= 0 || a + b <= c || a + c <= b || b + c <= a) {
            System.out.println("Не существует");
        } else if (a == b && b == c) {
            System.out.println("Равносторонний");
        } else if (a == b || a == c || b == c) {
            System.out.println("Равнобедренный");
        } else {
            System.out.println("Разносторонний");
        }
    }
}
