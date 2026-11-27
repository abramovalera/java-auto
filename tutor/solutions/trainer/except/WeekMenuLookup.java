import java.util.Scanner;

public class WeekMenuLookup {
    public static void main(String[] args) {
        String[] menu = {"борщ", "плов", "пельмени", "уха", "лагман", "пицца", "блины"};
        Scanner scanner = new Scanner(System.in);
        int day = scanner.nextInt();
        try {
            System.out.println("В меню: " + menu[day - 1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Такого дня нет");
        }
    }
}
