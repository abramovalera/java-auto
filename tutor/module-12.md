# Модуль 12 «Исключения» — план проверки знаний

> Файл для наставника. Здесь ответы — ученику не показывать (см. CLAUDE.md).

Опора: metanit, глава 5 (try/catch/finally, классы исключений, throws и throw, свои исключения, try-с-ресурсами). assert — не проверяем. Главная прикладная цель модуля — **уверенно читать stack trace**: это ежедневный навык разработчика (сбой в программе и упавший тест на JUnit = исключение: NumberFormatException, NullPointerException, FileNotFoundException, AssertionError / AssertionFailedError).

## Критерии зачёта модуля

1. Все 8 задач `m12-t01…m12-t08` зелёные на сайте (`/api/status`) **и** по каждой ученик ответил на вопрос о своём коде (см. «Вопросы по задачам» ниже).
2. Закрыты цели О1–О8: по каждой ученик ответил своими словами хотя бы на один вопрос.
3. **Живой stack trace (обязательно).** Дать ученику незнакомый trace на 10–20 строк с Caused by (можно сгенерировать: запустить в IDEA код с завёрнутым исключением). Зачёт, если он сам, без подсказок: назвал тип и сообщение, нашёл корень (последний Caused by), нашёл первую строку своего кода, объяснил, откуда пришли плохие данные.
4. Головоломки: из 8–10 заданных решено правильно **с объяснением** не меньше 80%. Обязательно среди них: finally + return, порядок catch, checked без throws.
5. Практика с нуля в IDEA (5–10 минут): метод с проверкой аргумента через `throw new IllegalArgumentException(...)` с информативным сообщением + main, который ловит исключение и печатает `e.getMessage()`. Без подглядывания в уроки.

## Цели модуля и вопросы

Формат: вопрос → **суть правильного ответа** → типичные заблуждения.

### О1. Что такое исключение и как оно всплывает
- «Что происходит, когда в методе на глубине трёх вызовов делят на ноль?» → **создаётся объект ArithmeticException, метод прерывается, исключение передаётся вызывающему; если нигде нет подходящего catch — фреймы снимаются до main, JVM печатает stack trace и завершает программу (exit code 1).**
- «Чем исключение отличается от ошибки компиляции?» → **компиляции — до запуска, javac; исключение — во время работы и часто зависит от данных.**
- «Назови 4 частых исключения и причину каждого.» → ArithmeticException (деление целого на 0), ArrayIndexOutOfBoundsException (индекс вне массива), NullPointerException (метод у null), NumberFormatException (строка не число).
- Заблуждение: «метод, через который пролетело исключение, возвращает 0/null». Нет — он вообще ничего не возвращает, прерывается.
- Заблуждение: «после исключения выполнится остаток main». Нет, если не поймано.

### О2. Чтение stack trace (главная цель модуля)
- «Из чего состоит stack trace?» → **заголовок (поток, полный тип, сообщение), строки `at Класс.метод(Файл:строка)` от вершины стека (где упало) к дну (main), блоки `Caused by` с причинами, `... N more` — сокращение повторяющихся нижних фреймов.**
- «Сверху вниз или снизу вверх?» → **сверху — где сломалось; снизу — как туда попали (история вызовов).**
- «Алгоритм разбора?» → **1) первая строка целиком; 2) последний Caused by — корень; 3) первая строка `at` со своим классом (пропуская java.base/, jdk.internal, org.junit, com.fasterxml, org.springframework); 4) клик в IDEA; 5) откуда пришли данные.**
- «Trace начинается с `at java.base/java.lang.Integer.parseInt`. Ошибка в JDK?» → **нет, JDK сообщает, что ей дали плохие данные.**
- «Что значит `... 2 more`?» → **ещё 2 фрейма, совпадающих с концом trace выше.**
- Попросить разобрать упрощённый trace сервера магазина из урока 12.2 (Jackson + Spring): где наш код (OrderReader, OrderController), где библиотеки, что значит первая строка про `"2 шт"`.
- Заблуждение: «место, где упало, — всегда место ошибки». Часто виноват вызывающий, передавший плохие данные.

### О3. try/catch
- «Что выполнится в try после строки с исключением?» → **ничего, остаток try пропускается; после catch выполнение идёт дальше после try/catch.**
- «Почему catch (RuntimeException) перед catch (NumberFormatException) — ошибка компиляции?» → **общий ловит всех потомков, второй недостижим: `has already been caught`. Порядок — от частного к общему.**
- «Что такое multi-catch и его ограничение?» → **`catch (A | B e)` — один обработчик; A и B не должны быть родителем и потомком.**
- «Почему для ввода с повтором лучше nextLine + parseInt, чем nextInt?» → **nextInt на «abc» бросает InputMismatchException и оставляет «abc» в буфере → бесконечный цикл, если не «доесть» его `scanner.next()`.**
- «Что даёт e.getMessage(), println(e), e.printStackTrace()?»

### О4. finally и try-с-ресурсами
- «Когда выполняется finally?» → **всегда: без исключения, с пойманным, с непойманным, после return/break. Не выполнится только при System.exit, падении JVM, бесконечном цикле, убийстве процесса.**
- «try { return x; } finally { x = 2; } — что вернётся?» → **старое x: значение return вычислено и отложено до finally.**
- «try { return 1; } finally { return 2; }» → **2; return в finally перебивает и проглатывает исключения — так не пишут.**
- «Зачем finally при работе с ресурсами?» → **закрыть файл/соединение с базой даже при сбое, иначе ресурс «утечёт» (в тестах на JUnit для этого — @AfterEach).**
- «Что такое try-with-resources и что можно положить в скобки?» → **ресурс закрывается сам; только AutoCloseable; закрывается до catch/finally.**
- Заблуждение: «finally не выполнится, если в try есть return».

### О5. Иерархия, checked/unchecked, throws
- «Нарисуй дерево» → **Throwable → Error (OutOfMemoryError, StackOverflowError, AssertionError) и Exception → RuntimeException (Arithmetic, NPE, IndexOutOfBounds, IllegalArgument → NumberFormat, IllegalState, ClassCast) + checked (IOException, InterruptedException).**
- «Чем checked отличается от unchecked?» → **checked (Exception, не RuntimeException) компилятор заставляет поймать или объявить в throws; это внешние обстоятельства. Unchecked — ошибки в коде, их чинят, а не ловят.**
- «Почему `Thread.sleep(1000)` в main без ничего не компилируется?» → **InterruptedException — checked: unreported exception … must be caught or declared to be thrown.**
- «throw vs throws?» → **throw — в теле, бросает сейчас; throws — в сигнатуре, предупреждает.**
- «Поймает ли catch (Exception e) упавший assert/AssertionError?» → **нет, это Error.**
- Заблуждение: «unchecked тоже надо объявлять в throws» (можно, но не нужно).

### О6. throw и свои исключения
- «Зачем бросать исключение самому?» → **fail fast: метод не должен молча принимать плохие данные; ошибка видна сразу и с понятным сообщением.**
- «IllegalArgumentException или IllegalStateException?» → **плохой аргумент / объект не в том состоянии.**
- «Как сделать своё исключение?» → **`class XxxException extends Exception/RuntimeException`, конструктор с `super(message)`, при необходимости поля и конструктор `(String message, Throwable cause)` → `super(message, cause)`.**
- «Когда своё checked, когда unchecked?» → **checked — ожидаемая бизнес-ситуация, на которую вызывающий обязан реагировать; unchecked — ошибка использования/сбой, обработать нечем.**
- «Какое сообщение хорошее?» → **что не так + какое значение пришло + где.**

### О7. Хорошие практики
- «Почему пустой catch — зло?» → **проблема исчезает молча, всплывает потом в другом месте, причина потеряна.**
- «Почему не стоит везде ловить Exception?» → **ловит и баги (NPE), маскирует их. Уместно только на верхнем уровне с логом.**
- «Как не потерять причину при заворачивании?» → **передать e вторым аргументом: new X("контекст", e).**
- «Когда if лучше, чем try/catch?» → **если проблему легко проверить заранее (индекс массива, null).**

### О8. Исключения в тестах на JUnit
- «Что для JUnit значит "тест упал"?» → **из тестового метода вылетело исключение (AssertionError — провал проверки, другое — ошибка).**
- «Что такое вечнозелёный тест и откуда он берётся?» → **try/catch в тесте проглатывает исключение, JUnit его не видит → тест всегда зелёный и врёт.**
- «Для чего assertThrows?» → **проверить, что код бросил исключение нужного типа (и затем проверить сообщение). Подробно — модуль 17; лямбда — модуль 14.**

## Вопросы по задачам (после зелёной проверки)

- **t01 SafeDivisionCalc:** «Почему "Калькулятор работает дальше" печатается и при делении на ноль?» «Что будет с делением 7.0 / 0 для double?» (★ на опережение: Infinity, исключения нет).
- **t02 TryCatchFinallyOrder:** «Почему finally напечатался до "main: результат"?» «Что если в finally написать return -1?»
- **t03 RetryNumberInput:** «Как выходим из while (true)?» «Почему "99999999999" — тоже не число?» «Что будет с nextInt вместо nextLine?»
- **t04 AgeValidationGate:** «Почему catch (NumberFormatException) стоит выше catch (IllegalArgumentException)? Что будет, если поменять?» (не скомпилируется). «Почему validateAge сам ничего не печатает?»
- **t05 KeyValueLineParser:** «Как ты разобрал "a=b=c"?» «Почему одна обработка ошибок в main, а не println в каждом if?» Показать план (Мастерская): примеры → роли → псевдокод.
- **t06 PiggyBankTerminal:** «Почему своё исключение checked? Что сказал компилятор, пока не было throws?» «Зачем поле shortfall?» «Почему проверка суммы ≤ 0 — IllegalArgumentException, а не своё исключение?»
- **t07 SensorReadingsFix:** «Какие две ошибки были и как ты их нашёл?» «Зачем оставлять catch (RuntimeException)?»
- **t08 OrderLineLoader:** «Как выглядел бы stack trace, если бы OrderLineRejectedException не поймали?» (Caused by: NumberFormatException). «Почему для количества ≤ 0 причины нет?»

## Банк головоломок

Давай по одной, сначала ответ и «почему», потом разбор. ★★★ — на опережение, предупреждать.

### Что выведется?
1. ```java
   static String test() {
       try { return "try"; } finally { System.out.println("finally"); }
   }
   // main: System.out.println(test());
   ```
   → `finally`, затем `try`.
2. ```java
   static int count() { int x = 5; try { return x; } finally { x = x * 10; } }
   ```
   → `5` (значение return отложено до finally).
3. `try { return 1; } finally { return 2; }` → `2` (return в finally перебивает; так не пишут).
4. ```java
   System.out.println("1");
   try { System.out.println("2"); int[] a = {1, 2}; System.out.println(a[2]); System.out.println("3"); }
   catch (ArrayIndexOutOfBoundsException e) { System.out.println("4"); }
   finally { System.out.println("5"); }
   System.out.println("6");
   ```
   → `1 2 4 5 6` (по строкам).
5. Исключение в finally:
   ```java
   try { try { throw new IllegalStateException("из try"); }
         finally { throw new RuntimeException("из finally"); } }
   catch (RuntimeException e) { System.out.println(e.getMessage()); }
   ```
   → `из finally` (исходное исключение потеряно).
6. `try { Integer.parseInt("12a"); } catch (IllegalArgumentException e) { System.out.println("IAE: " + e.getMessage()); }` → `IAE: For input string: "12a"` (NFE — потомок IAE).
7. ```java
   static void go() { try { System.out.println("A"); int x = 1 / 0; } finally { System.out.println("B"); } }
   // main: try { go(); System.out.println("C"); } catch (ArithmeticException e) { System.out.println("D"); } System.out.println("E");
   ```
   → `A B D E`.
8. `try { System.out.println("A"); int x = 5 / 0; System.out.println("B"); } finally { System.out.println("C"); } System.out.println("D");` (без catch) → `A`, `C`, затем stack trace ArithmeticException; `D` не печатается.
9. ```java
   try { System.out.println(10 / 0); }
   catch (ArithmeticException e) { System.out.println(e.getMessage()); }
   ```
   → `/ by zero`.
10. `try { throw new IllegalStateException("x"); } catch (RuntimeException e) { System.out.println(e.getClass().getSimpleName()); }` → `IllegalStateException` (переменная типа родителя, объект — потомок).
11. ★★★ `System.out.println(7.0 / 0);` → `Infinity`. Исключения нет: ArithmeticException только для целочисленного деления.

### Скомпилируется ли?
12. `catch (Exception e) {…} catch (ArithmeticException e) {…}` → **нет**: `exception ArithmeticException has already been caught`.
13. `static void load() { throw new Exception("нет файла"); }` → **нет**: `unreported exception Exception; must be caught or declared to be thrown`. Починка: `throws Exception` или catch.
14. `public static void main(String[] args) { Thread.sleep(1000); }` → **нет**: InterruptedException — checked.
15. `catch (NumberFormatException | IllegalArgumentException e)` → **нет**: `Alternatives in a multi-catch statement cannot be related by subclassing`.
16. `try { System.out.println("x"); } catch (InterruptedException e) {}` → **нет**: `exception InterruptedException is never thrown in body of corresponding try statement`. А с `catch (ArithmeticException e)` — **да** (для unchecked проверки нет).
17. `int count; try { count = Integer.parseInt(t); } catch (NumberFormatException e) {} System.out.println(count);` → **нет**: `variable count might not have been initialized`.
18. `try { System.out.println("A"); } System.out.println("B");` → **нет**: `'try' without 'catch', 'finally' or resource declarations`.
19. `throw new IllegalStateException("x"); System.out.println("y");` в одном блоке → **нет**: `unreachable statement`.
20. `void f() throws IllegalArgumentException { }` → **да** (throws для unchecked разрешён, но не обязателен).

### Скомпилируется, но что при запуске?
21. `String role = null; if (role.equals("admin")) {…}` → NPE: `Cannot invoke "String.equals(Object)" because "role" is null`. Безопасно: `"admin".equals(role)`.
22. `try { throw new AssertionError("провал"); } catch (Exception e) { System.out.println("поймали"); }` → программа падает с AssertionError (это Error, не Exception).
23. `static void countDown(int n) { countDown(n - 1); }` → StackOverflowError, trace из ~1000 одинаковых строк.
24. Цикл с `scanner.nextInt()` в try и `catch (InputMismatchException e) { System.out.println("ещё раз"); }` без `scanner.next()` → на вводе «abc» бесконечно печатает «ещё раз».
25. `Integer.parseInt(" 15")` → NumberFormatException: `For input string: " 15"` (нужен trim).

### Найди ошибку / почему ошибка
26. Тест:
    ```java
    @Test void readsTimeout() {
        try { assertEquals(10, readTimeout("timeout=10s")); }
        catch (Exception e) { System.out.println("ой"); }
    }
    ```
    → вечнозелёный тест: исключение из readTimeout проглочено, JUnit его не видит.
27. `catch (NumberFormatException e) { throw new IllegalStateException("плохая настройка"); }` → потеряна причина: нет `, e` → в trace не будет Caused by.
28. `catch (NumberFormatException e) { }` → пустой catch: ошибка молча проглочена.
29. Ресурс закрывается в try, а не в finally: `Connection c = open(); c.save(order); c.close();` → при падении save() соединение не закроется. Починка: finally или try-с-ресурсами.

## Задачи «Мастерской» (оценивается план, а не только код)

- **m12-t03 «Ввод числа с повтором»**: попросить сначала таблицу «ввод → что делать» (включая "", "3.5", " 7 ", "99999999999"), роли переменных (line — временная, выход через return), псевдокод — потом код.
- **m12-t05 «Разбор ключ=значение»**: попросить руками разобрать `a=b=c`, `=5`, `hello`, `wait=`; порядок проверок; где ловить (один catch в main, throw в parseLine).
- Дополнительная устная Мастерская: «Как бы ты искал строку, из-за которой упала программа, в trace на 80 строк?» — ожидается алгоритм из урока 12.2.

## Дополнительные задачи для быстрых

1. **Безопасный индекс.** Метод `static int safeGet(int[] arr, int index, int defaultValue)` двумя способами: через if и через try/catch. Обсудить, какой лучше и почему (правило 4: что легко проверить — проверяй if-ом).
2. **Цепочка из трёх уровней.** Классы `Repository` → `Service` → `Controller`: нижний бросает NumberFormatException, средний заворачивает в `DataLoadException(msg, e)`, верхний — в `RequestFailedException(msg, e)`. В main не ловить — прочитать trace с двумя Caused by и найти корень.
3. **Закрытие в правильном порядке.** Два своих ресурса `implements AutoCloseable` в одном try-с-ресурсами: `try (A a = new A(); B b = new B())`. Предсказать порядок закрытия (обратный: B, потом A) и проверить запуском.
4. **Retry с лимитом.** Ввод числа, но не больше 3 попыток; после третьей неудачи — `throw new IllegalStateException("3 неудачные попытки, последняя: «…»")`. Связь с реальными программами: повторные запросы к серверу, который иногда не отвечает.
5. **Свой checked для валидации пароля.** `WeakPasswordException extends Exception` с полем «какое правило нарушено»; проверка длины, цифры, буквы. Хорошие сообщения.
