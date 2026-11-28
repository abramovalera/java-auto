import java.util.List;

/**
 * Джавик — робот-курьер из игры курса «Джавик-курьер».
 *
 * <p>Этот файл нужен, чтобы IntelliJ IDEA знала, какие команды и датчики есть у робота,
 * и не подчёркивала твой код красным. Наведи курсор на любой метод — IDEA покажет его описание.
 *
 * <p><b>Здесь ничего не меняй.</b> Настоящий Джавик живёт на сайте курса:
 * ты пишешь свой класс (например, {@code CourierPart1}) в папке модуля, сохраняешь
 * и жмёшь на странице задачи «▶ Проверить». Сайт сам запустит твой код на нескольких картах
 * и покажет поездку по шагам.
 *
 * <h2>Карта</h2>
 * Поле из клеток: стены, дорога, 📦 посылки, 🏠 адреса доставки, 🏁 склад (отсюда Джавик стартует).
 * Джавик всегда смотрит в одну из четырёх сторон. «Впереди», «слева» и «справа» — относительно того,
 * куда он смотрит сейчас.
 *
 * <h2>Что считается ошибкой</h2>
 * <ul>
 *   <li>{@code move()} в стену — Джавик врезался, поездка окончена;</li>
 *   <li>{@code take()} там, где нет посылки, или {@code drop()} не на адресе;</li>
 *   <li>кончились ходы — у каждой карты есть лимит, обычно это значит, что Джавик ездит по кругу.</li>
 * </ul>
 */
public class CourierRobot {

    private static final String RUN_ON_SITE =
            "Джавика запускает сайт курса: сохрани файл и нажми «▶ Проверить» на странице задачи.";

    // ======================== датчики: «что вокруг?» ========================

    /** Впереди стена? {@code true} — ехать вперёд нельзя, {@code false} — можно. */
    public boolean wallAhead() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /** Слева (относительно взгляда Джавика) стена? */
    public boolean wallLeft() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /** Справа (относительно взгляда Джавика) стена? */
    public boolean wallRight() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /** На клетке, где стоит Джавик, лежит посылка? */
    public boolean parcelHere() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /** Джавик сейчас везёт хотя бы одну посылку? */
    public boolean carrying() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /** Джавик стоит на адресе доставки 🏠? */
    public boolean atTarget() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /** Джавик стоит на складе 🏁 (там, откуда начал)? */
    public boolean atDepot() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /**
     * Табличка на развилке, где стоит Джавик: {@code "LEFT"} — сверни налево ({@code turnLeft()}),
     * {@code "RIGHT"} — направо ({@code turnRight()}), {@code "AHEAD"} — езжай прямо ({@code move()}).
     * Если таблички нет, вернёт пустую строку {@code ""}.
     * Табличку видно, пока Джавик на этой клетке не повернулся.
     * Строки сравнивай через {@code equals} или в {@code switch}, а не через {@code ==}.
     */
    public String sign() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    // ======================== команды: «что сделать?» ========================

    /** Проехать одну клетку вперёд. Если впереди стена — Джавик врежется. Проверяй {@link #wallAhead()}. */
    public void move() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /** Повернуться налево на месте (на 90°). Клетка не меняется. */
    public void turnLeft() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /** Повернуться направо на месте (на 90°). Клетка не меняется. */
    public void turnRight() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /** Взять посылку с клетки, где стоит Джавик. Если посылки нет — ошибка. */
    public void take() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /** Отдать одну посылку. Можно только на адресе доставки 🏠 и только если Джавик что-то везёт. */
    public void drop() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    // ======================== район и заказы (части 4–5) ========================

    /**
     * Номера домов на улице (часть 4, «Квартал»): на сколько клеток от склада стоит каждый дом.
     * Например, {@code [3, 7, 10]}. Дома — ниши слева от дороги. На других картах — пустой массив.
     */
    public int[] houses() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /** Заказы на начало смены (часть 5): адреса в порядке поступления, например {@code [Кафе, Почта, Кафе]}. */
    public List<String> orders() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /**
     * Новые заказы, которые пришли с прошлого вызова (часть 5). Новые заказы приходят по ходу смены —
     * после доставок. Если ничего не пришло — пустой список.
     */
    public List<String> newOrders() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /** Название дома на клетке, где стоит Джавик (часть 5). Если это не дом — пустая строка. */
    public String houseName() { throw new UnsupportedOperationException(RUN_ON_SITE); }

    /**
     * Автопилот (часть 5): Джавик сам доедет до дома с этим названием по самому короткому пути.
     * На некоторых уровнях число вызовов ограничено: к каждому дому — один заезд.
     */
    public void goTo(String address) { throw new UnsupportedOperationException(RUN_ON_SITE); }
}
