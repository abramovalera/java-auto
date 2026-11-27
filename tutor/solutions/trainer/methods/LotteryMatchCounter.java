import java.util.Scanner;

public class LotteryMatchCounter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] winning = new int[6];
        for (int i = 0; i < 6; i++) {
            winning[i] = scanner.nextInt();
        }
        int matches = 0;
        for (int i = 0; i < 6; i++) {
            if (contains(winning, scanner.nextInt())) {
                matches++;
            }
        }
        System.out.println("Совпало: " + matches);
        if (matches == 6) {
            System.out.println("Джекпот!");
        }
    }

    static boolean contains(int[] numbers, int x) {
        for (int number : numbers) {
            if (number == x) {
                return true;
            }
        }
        return false;
    }
}
