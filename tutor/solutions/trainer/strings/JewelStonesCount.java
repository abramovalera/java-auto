import java.util.Scanner;

public class JewelStonesCount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String jewels = scanner.nextLine();
        String stones = scanner.nextLine();
        int count = 0;
        for (int i = 0; i < stones.length(); i++) {
            if (jewels.indexOf(stones.charAt(i)) != -1) {
                count++;
            }
        }
        System.out.println(count);
    }
}
