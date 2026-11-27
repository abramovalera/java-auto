import java.util.Scanner;

public class AtmPinAttempts {
    static boolean checkPin(String correct, String entered) {
        if (entered.length() != 4) {
            throw new IllegalArgumentException("PIN — это 4 цифры");
        }
        for (int i = 0; i < entered.length(); i++) {
            if (!Character.isDigit(entered.charAt(i))) {
                throw new IllegalArgumentException("PIN — это 4 цифры");
            }
        }
        return entered.equals(correct);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String correct = scanner.nextLine();
        int wrong = 0;
        try {
            while (scanner.hasNextLine()) {
                String entered = scanner.nextLine();
                try {
                    if (checkPin(correct, entered)) {
                        System.out.println("PIN верный, добро пожаловать");
                        return;
                    }
                    wrong++;
                    if (wrong == 3) {
                        throw new AtmCardBlockedException("Карта заблокирована: 3 неверных PIN");
                    }
                    System.out.println("Неверный PIN, осталось попыток: " + (3 - wrong));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("Сеанс завершён");
        } catch (AtmCardBlockedException e) {
            System.out.println(e.getMessage());
        }
    }
}

class AtmCardBlockedException extends Exception {
    AtmCardBlockedException(String message) {
        super(message);
    }
}
