import java.util.Scanner;

public class PackingBoxesCount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        int boxes = n / k;
        if (n % k != 0) {
            boxes++;
        }
        System.out.println(boxes);
    }
}
