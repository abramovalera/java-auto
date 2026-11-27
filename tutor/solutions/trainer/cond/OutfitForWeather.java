import java.util.Scanner;

public class OutfitForWeather {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt();
        String rain = scanner.next();
        String outfit;
        if (t < 0) {
            outfit = "Пуховик";
        } else if (t < 15) {
            outfit = "Куртка";
        } else {
            outfit = "Футболка";
        }
        if (rain.equals("да")) {
            outfit = outfit + " и зонт";
        }
        System.out.println(outfit);
    }
}
