public class CourierPart1 {

    // Шпаргалка по командам — README.md в этой же папке: открой его второй вкладкой рядом с кодом
    // (правой кнопкой по вкладке README.md → Split Right).

    // Игра вызывает этот метод на КАЖДОМ ходу и передаёт робота.
    // Спроси у робота, что вокруг, и дай ему ОДНУ команду:
    //   robot.move()       — проехать клетку вперёд
    //   robot.turnLeft()   — повернуться налево     robot.turnRight() — направо
    //   robot.take()       — взять посылку          robot.drop()      — отдать посылку
    //
    // Датчики (true / false): robot.wallAhead(), robot.wallLeft(), robot.wallRight(),
    //   robot.parcelHere(), robot.carrying(), robot.atTarget(), robot.atDepot()
    // Наведи курсор на любой метод робота — IntelliJ IDEA покажет его описание.
    // Табличка на развилке: robot.sign() — "LEFT", "RIGHT", "AHEAD" или "" (таблички нет).
    public static void step(CourierRobot robot) {
        robot.move();
    }
}
