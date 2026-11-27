import java.util.Scanner;

public class CandyBoxFill {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int candies = scanner.nextInt();
        int boxes = candies / 6;
        int rest = candies % 6;
        if (rest == 0) {
            System.out.println("Полных коробок: " + boxes);
        } else {
            System.out.println("Полных коробок: " + boxes + ", лишних конфет: " + rest);
        }
    }
}
