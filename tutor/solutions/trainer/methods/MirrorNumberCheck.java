import java.util.Scanner;

public class MirrorNumberCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        String result = "";
        for (int i = 0; i < k; i++) {
            int n = scanner.nextInt();
            if (isPalindrome(n)) {
                result = result + n + " ";
            }
        }
        if (result.length() == 0) {
            System.out.println("нет");
        } else {
            System.out.println(result);
        }
    }

    static boolean isPalindrome(int n) {
        if (n < 0) {
            return false;
        }
        long reversed = 0;
        int rest = n;
        while (rest > 0) {
            reversed = reversed * 10 + rest % 10;
            rest = rest / 10;
        }
        return reversed == n;
    }
}
