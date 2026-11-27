public class DebugShoppingCart {
    public static void main(String[] args) {
        String[] items = {"Хлеб", "Молоко", "Сыр", "Яблоки"};
        int[] prices = {45, 89, 320, 150};
        int[] quantities = {2, 1, 1, 3};

        int total = 0;
        for (int i = 0; i < items.length; i++) {
            int lineTotal = lineTotal(prices[i], quantities[i]);
            System.out.println(items[i] + ": " + quantities[i] + " x " + prices[i] + " = " + lineTotal);
            total += lineTotal;
        }
        int discount = discount(total);
        System.out.println("Итого: " + total);
        System.out.println("Скидка 10%: " + discount);
        System.out.println("К оплате: " + (total - discount));
    }

    static int lineTotal(int price, int quantity) {
        return price * quantity;
    }

    // скидка 10%, копейки отбрасываем
    static int discount(int total) {
        return total * 10 / 100;
    }
}
