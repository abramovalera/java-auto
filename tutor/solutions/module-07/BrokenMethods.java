public class BrokenMethods {
    public static void main(String[] args) {
        printLine(20);
        int total = multiply(250, 3);
        printTotal(total);
        System.out.println("Со скидкой: " + applyDiscount(total));
        printLine(20);
    }

    static void printLine(int length) {
        for (int i = 0; i < length; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    static int multiply(int a, int b) {
        return a * b;
    }

    static void printTotal(int sum) {
        System.out.println("Итого: " + sum);
    }

    static int applyDiscount(int sum) {
        if (sum > 500) {
            return sum - sum / 10;
        }
        return sum;
    }
}
