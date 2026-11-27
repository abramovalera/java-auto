import java.util.Scanner;

public class LeapYearIfElse {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int year = scanner.nextInt();
        boolean leap;                       // роль «флаг»
        if (year % 400 == 0) {              // самое узкое исключение — первым
            leap = true;
        } else if (year % 100 == 0) {
            leap = false;
        } else if (year % 4 == 0) {
            leap = true;
        } else {
            leap = false;
        }
        if (leap) {
            System.out.println("Год " + year + " високосный, в нём 366 дней");
        } else {
            System.out.println("Год " + year + " не високосный, в нём 365 дней");
        }
    }
}
