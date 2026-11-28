public class CourierPart2 {

    // Шпаргалка по командам — README.md в этой же папке: открой его второй вкладкой рядом с кодом
    // (правой кнопкой по вкладке README.md → Split Right).

    // Игра вызывает этот метод ОДИН раз. Командуй Джавиком сам — каждая команда это один ход:
    //   robot.move()       — проехать клетку вперёд
    //   robot.turnLeft()   — повернуться налево     robot.turnRight() — направо
    //   robot.take()       — взять посылку          robot.drop()      — отдать посылку
    //
    // Датчики (true / false): robot.wallAhead(), robot.wallLeft(), robot.wallRight(),
    //   robot.parcelHere(), robot.carrying(), robot.atTarget(), robot.atDepot()
    // Наведи курсор на любой метод робота — IntelliJ IDEA покажет его описание.
    public static void run(CourierRobot robot) {
        robot.move();
    }
}
