import java.util.Scanner;

public class PalindromePhraseCheck {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String w = clean(sc.nextLine());
        if (isPalindrome(w)) {
            System.out.println("да");
        } else {
            System.out.println("нет");
        }
    }

    static String clean(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    static boolean isPalindrome(String w) {
        int left = 0;
        int right = w.length() - 1;
        while (left < right) {
            if (w.charAt(left) != w.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
