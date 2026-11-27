/**
 * Радар Джавика в игре «Раннер». Его тебе передают в метод decide — спрашивай у него, что впереди.
 */
public class Radar {

    private final RunnerGame game;

    Radar(RunnerGame game) {
        this.game = game;
    }

    /** Сколько клеток до ближайшего препятствия впереди. Если впереди пусто (радар видит 12 клеток) — 99. */
    public int distance() {
        return game.distanceToNext();
    }

    /** Ближайшее препятствие — жук на земле? Его надо перепрыгнуть (JUMP). */
    public boolean isLow() {
        return game.nextIsLow();
    }

    /** Ближайшее препятствие — летучая ошибка на уровне головы? Под ней надо пригнуться (DUCK). */
    public boolean isHigh() {
        return !game.nextIsLow() && game.distanceToNext() != 99;
    }

    /** Скорость бега: на сколько клеток Джавик продвигается за один ход (1 или 2). */
    public int speed() {
        return game.speed();
    }
}
