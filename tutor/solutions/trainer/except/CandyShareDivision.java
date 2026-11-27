import java.util.Scanner;

public class CandyShareDivision {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int candies = scanner.nextInt();
        int kids = scanner.nextInt();
        try {
            int each = candies / kids;
            int rest = candies % kids;
            System.out.println("Каждому по " + each + ", остаток " + rest);
        } catch (ArithmeticException e) {
            System.out.println("Детей нет — конфеты остаются в коробке");
        }
    }
}
