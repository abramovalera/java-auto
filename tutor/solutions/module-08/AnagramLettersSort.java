import java.util.Arrays;
import java.util.Scanner;

public class AnagramLettersSort {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String first = sc.nextLine().toLowerCase().replace(" ", "");
        String second = sc.nextLine().toLowerCase().replace(" ", "");
        char[] a = first.toCharArray();
        char[] b = second.toCharArray();
        Arrays.sort(a);
        Arrays.sort(b);
        if (Arrays.equals(a, b)) {
            System.out.println("да");
        } else {
            System.out.println("нет");
        }
    }
}
