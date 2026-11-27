// Мини-отель: общий класс номера и два номера-наследника.
// Эталон починки. Ошибки заготовки:
// 1) FixStandardRoom: в конструкторе нет super(number, "Стандарт") — у родителя нет конструктора без параметров;
// 2) FixStandardRoom: @Override над opne() — опечатка, такого метода у родителя нет; нужно open();
// 3) FixSuiteRoom: поле type у родителя private — берём через getType() (или делаем protected);
// 4) FixSuiteRoom.open(): забыт super.open() — номер «не открывался» (логическая ошибка).
public class HotelRoomsFix {
    public static void main(String[] args) {
        FixRoom[] rooms = {
            new FixStandardRoom(101),
            new FixSuiteRoom(305, 3)
        };
        for (int i = 0; i < rooms.length; i++) {
            rooms[i].open();
            System.out.println("---");
        }
    }
}

class FixRoom {
    private final int number;
    private final String type;

    FixRoom(int number, String type) {
        this.number = number;
        this.type = type;
    }

    String getType() {
        return type;
    }

    void open() {
        System.out.println("Открываю номер " + number);
        System.out.println("Тип номера: " + type);
    }
}

class FixStandardRoom extends FixRoom {
    FixStandardRoom(int number) {
        super(number, "Стандарт");
    }

    @Override
    void open() {
        super.open();
        System.out.println("Меняю полотенца");
    }
}

class FixSuiteRoom extends FixRoom {
    private final int guests;

    FixSuiteRoom(int number, int guests) {
        super(number, "Люкс");
        this.guests = guests;
    }

    @Override
    void open() {
        super.open();
        System.out.println("Гостей в номере: " + guests + " (" + getType() + ")");
    }
}
