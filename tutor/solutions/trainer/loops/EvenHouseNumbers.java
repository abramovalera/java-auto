import java.util.Scanner;

public class EvenHouseNumbers {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int house = 2; house <= n; house += 2) {
            System.out.print(house + " ");
        }
        System.out.println();
    }
}
