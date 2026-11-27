import java.util.Scanner;

public class ArraySumRecursion {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] operations = new int[n];
        for (int i = 0; i < n; i++) {
            operations[i] = scanner.nextInt();
        }
        System.out.println("Итог за месяц: " + sum(operations, 0));
    }

    static int sum(int[] arr, int index) {
        if (index == arr.length) {
            return 0;
        }
        return arr[index] + sum(arr, index + 1);
    }
}
