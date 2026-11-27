import java.util.Scanner;

public class TaxiFareMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int km = scanner.nextInt();
        System.out.println("Поездка " + km + " км: " + fare(km) + " ₽");
    }

    static int fare(int km) {
        if (km <= 3) {
            return 150;
        }
        return 150 + (km - 3) * 40;
    }
}
