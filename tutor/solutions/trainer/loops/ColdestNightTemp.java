import java.util.Scanner;

public class ColdestNightTemp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int coldest = scanner.nextInt();
        for (int i = 1; i < n; i++) {
            int t = scanner.nextInt();
            if (t < coldest) {
                coldest = t;
            }
        }
        System.out.println(coldest);
    }
}
