import java.util.Scanner;

public class OrderLineLoader {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = Integer.parseInt(scanner.nextLine().trim());
        int total = 0;
        int rejected = 0;
        for (int i = 1; i <= count; i++) {
            String line = scanner.nextLine();
            try {
                total += parseQuantity(line, i);
            } catch (OrderLineRejectedException e) {
                rejected++;
                System.out.println("Ошибка: " + e.getMessage());
                Throwable cause = e.getCause();
                if (cause != null) {
                    System.out.println("  причина: " + cause.getClass().getSimpleName() + ": " + cause.getMessage());
                }
            }
        }
        System.out.println("Всего товаров: " + total + ", отклонено строк: " + rejected);
    }

    static int parseQuantity(String line, int lineNumber) {
        int separator = line.indexOf(';');
        String quantityText = line.substring(separator + 1).trim();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            throw new OrderLineRejectedException("строка " + lineNumber + ": плохое количество", e);
        }
        if (quantity <= 0) {
            throw new OrderLineRejectedException("строка " + lineNumber + ": количество должно быть больше нуля");
        }
        return quantity;
    }
}

class OrderLineRejectedException extends RuntimeException {
    OrderLineRejectedException(String message) {
        super(message);
    }

    OrderLineRejectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
