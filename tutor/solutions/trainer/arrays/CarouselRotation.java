import java.util.Scanner;

public class CarouselRotation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] horses = new int[n];
        for (int i = 0; i < n; i++) {
            horses[i] = scanner.nextInt();
        }
        int k = scanner.nextInt();
        int[] rotated = new int[n];
        for (int i = 0; i < n; i++) {
            rotated[(i + k) % n] = horses[i];
        }
        for (int h : rotated) {
            System.out.print(h + " ");
        }
        System.out.println();
    }
}
