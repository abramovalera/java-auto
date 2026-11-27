public class BrokenMethods {
    public static void main(String[] args) {
        printLine(20);
        int total = multiply(250, 3);
        printTotal(total);
        System.out.println("Со скидкой: " + applyDiscount(total));
        printLine();
    }

    static void printLine(int length) {
        for (int i = 0; i < length; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    static multiply(int a, int b) {
        return a * b;
    }

    static void printTotal(int sum) {
        System.out.println("Итого: " + total);
    }

    static int applyDiscount(int sum) {
        if (sum > 500) {
            return sum - sum / 10;
        }
    }
}
