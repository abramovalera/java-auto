import java.util.Scanner;

public class EggCartonPacking {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int eggs = scanner.nextInt();
        int cartons = eggs / 10;
        int left = eggs % 10;
        System.out.println("Коробок: " + cartons);
        System.out.println("Осталось яиц: " + left);
    }
}
