// Мини-отель: общий класс номера и два номера-наследника.
// Файл писали в спешке: он не компилируется, а после починки печатает не всё.
// Найди и исправь ошибки, не переписывая программу заново.
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
    }

    @Override
    void opne() {
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
        System.out.println("Гостей в номере: " + guests + " (" + type + ")");
    }
}
