import java.util.Scanner;

public class HelperThreadsJoin {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        int[] limits = new int[3];
        for (int i = 0; i < 3; i++) {
            limits[i] = sc.nextInt();
        }

        long[] results = new long[3];        // у каждого помощника своя ячейка — гонки нет
        Thread[] helpers = new Thread[3];
        for (int i = 0; i < 3; i++) {
            int index = i;                   // лямбде нужна «фактически final» переменная
            helpers[i] = new Thread(() -> {
                long sum = 0;
                for (int k = 1; k <= limits[index]; k++) {
                    sum += k;
                }
                results[index] = sum;
            });
            helpers[i].start();
        }

        for (Thread helper : helpers) {
            helper.join();                   // главный поток ждёт каждого помощника
        }

        long total = 0;
        for (int i = 0; i < 3; i++) {
            System.out.println("Помощник " + (i + 1) + " посчитал: " + results[i]);
            total += results[i];
        }
        System.out.println("Итого: " + total);
    }
}
