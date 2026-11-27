import java.util.Scanner;

public class FarmFieldArea {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int width = scanner.nextInt();
        int length = scanner.nextInt();
        long area = (long) width * length;
        System.out.println("Площадь: " + area + " кв. м");
        System.out.println("Гектаров: " + area / 10000);
    }
}
