import java.util.Scanner;

public class CinemaSeatBooking {
    static boolean[] taken = new boolean[10];

    static void book(int seat) throws CinemaSeatTakenException {
        if (taken[seat - 1]) {
            throw new CinemaSeatTakenException("место " + seat + " уже занято");
        }
        taken[seat - 1] = true;
    }

    static void cancel(int seat) throws CinemaSeatFreeException {
        if (!taken[seat - 1]) {
            throw new CinemaSeatFreeException("место " + seat + " и так свободно");
        }
        taken[seat - 1] = false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < n; i++) {
            String[] parts = scanner.nextLine().split(" ");
            String action = parts[0];
            String seatText = parts[1];
            try {
                if (!action.equals("book") && !action.equals("cancel")) {
                    throw new IllegalArgumentException("Не знаю команду " + action);
                }
                int seat = Integer.parseInt(seatText);
                if (action.equals("book")) {
                    book(seat);
                    System.out.println("Место " + seat + " забронировано");
                } else {
                    cancel(seat);
                    System.out.println("Бронь места " + seat + " снята");
                }
            } catch (NumberFormatException e) {
                System.out.println("Номер места — целое число");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Места " + seatText + " в зале нет");
            } catch (CinemaBookingException e) {
                System.out.println("Отказ: " + e.getMessage());
            }
        }
        int count = 0;
        for (boolean t : taken) {
            if (t) {
                count++;
            }
        }
        System.out.println("Занято мест: " + count);
    }
}

class CinemaBookingException extends Exception {
    CinemaBookingException(String message) {
        super(message);
    }
}

class CinemaSeatTakenException extends CinemaBookingException {
    CinemaSeatTakenException(String message) {
        super(message);
    }
}

class CinemaSeatFreeException extends CinemaBookingException {
    CinemaSeatFreeException(String message) {
        super(message);
    }
}
