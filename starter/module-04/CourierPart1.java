public class CourierPart1 {

    // Шпаргалка по командам — README.md в этой же папке: открой его второй вкладкой рядом с кодом
    // (правой кнопкой по вкладке README.md → Split Right).

    // Игра вызывает этот метод на КАЖДОМ ходу и передаёт робота.
    // Спроси у робота, что вокруг, и верни ОДНУ команду:
    //   "MOVE"  — проехать клетку вперёд
    //   "LEFT"  — повернуться налево        "RIGHT" — повернуться направо
    //   "TAKE"  — взять посылку             "DROP"  — отдать посылку
    //
    // Датчики (true / false): robot.wallAhead(), robot.wallLeft(), robot.wallRight(),
    //   robot.parcelHere(), robot.carrying(), robot.atTarget(), robot.atDepot()
    // Наведи курсор на любой метод робота — IntelliJ IDEA покажет его описание.
    // Табличка на развилке: robot.sign() — "LEFT", "RIGHT", "AHEAD" или "" (таблички нет).
    public static String decide(CourierRobot robot) {
        return "MOVE";
    }
}
