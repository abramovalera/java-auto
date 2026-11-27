import java.util.Scanner;

public class PalindromeYearCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int year = scanner.nextInt();
        int d1 = year / 1000;
        int d2 = year / 100 % 10;
        int d3 = year / 10 % 10;
        int d4 = year % 10;
        boolean palindrome = d1 == d4 && d2 == d3;
        System.out.println(palindrome);
    }
}
