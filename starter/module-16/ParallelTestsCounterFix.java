// Четыре «раннера» параллельно прогоняют по 250 000 тестов,
// и каждый пройденный тест увеличивает общий счётчик passed.
// Ожидается: «Пройдено тестов: 1000000». Но программа врёт —
// и каждый раз по-разному. Найди и почини ошибки.
public class ParallelTestsCounterFix {
    static int passed = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread[] runners = new Thread[4];
        for (int i = 0; i < runners.length; i++) {
            runners[i] = new Thread(() -> {
                for (int t = 0; t < 250_000; t++) {
                    passed++;
                }
            });
            runners[i].start();
        }
        runners[0].join();
        System.out.println("Раннеров: " + runners.length);
        System.out.println("Пройдено тестов: " + passed);
    }
}
