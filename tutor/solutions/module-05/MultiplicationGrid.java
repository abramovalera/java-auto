import java.util.Scanner;

public class MultiplicationGrid {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.print("\t");                    // пустой угол
        for (int col = 1; col <= n; col++) {       // строка-заголовок
            System.out.print(col + "\t");
        }
        System.out.println();
        for (int row = 1; row <= n; row++) {
            System.out.print(row + "\t");          // номер строки слева
            for (int col = 1; col <= n; col++) {
                System.out.print(row * col + "\t");
            }
            System.out.println();
        }
    }
}
