import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TreasureGemsCount {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String gems = sc.nextLine();
        String stones = sc.nextLine();
        Set<Character> gemTypes = new HashSet<>();
        for (int i = 0; i < gems.length(); i++) {
            gemTypes.add(gems.charAt(i));
        }
        int count = 0;
        for (int i = 0; i < stones.length(); i++) {
            if (gemTypes.contains(stones.charAt(i))) {
                count++;
            }
        }
        System.out.println("Драгоценных камней: " + count);
    }
}
