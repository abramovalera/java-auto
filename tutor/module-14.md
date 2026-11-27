# Модуль 14 «Лямбды и Stream API» — план проверки знаний

> Файл для наставника. Здесь ответы — ученику не показывать (см. CLAUDE.md).
> Эталонные решения задач — `tutor/solutions/module-14/`.

## Критерии зачёта модуля

1. Все 12 задач `m14-t01…m14-t12` зелёные на сайте (`/api/status`) **и** по каждой ученик ответил на вопрос о своём коде (почему такая операция, что вернёт, что будет на пустом вводе).
2. Закрыты цели О1–О9 ниже: по каждой — ответ своими словами хотя бы на один вопрос.
3. Головоломки (на сайте их 17): из 8–10 заданных решено правильно **с объяснением** не меньше 80%. Обязательно хотя бы по одной на: ленивость, вертикальный порядок/limit, одноразовость потока, Optional.get, effectively final.
4. Практика в IDEA **с нуля, без подглядывания**:
   - написать стрим «отфильтровать → преобразовать → собрать» по списку record-ов (например, названия товаров в наличии дешевле X, по алфавиту);
   - ту же задачу — циклом, и объяснить, какой вариант читается лучше;
   - отсортировать список record-ов по двум полям (`comparing` + `reversed` + `thenComparing`).
5. Умеет объяснить, почему `allMatch` на пустом списке — опасная проверка.

Экзамен можно вести за несколько занятий. Отмечай закрытые цели в `progress.md`.

## Цели модуля и вопросы

Формат: вопрос → **суть правильного ответа** → типичные заблуждения.

### О1. Лямбда как короткая запись анонимного класса
- «Перепиши `new Comparator<String>() { public int compare(String a, String b) { return a.length() - b.length(); } }` лямбдой. Что ты выбросил и почему компилятор это разрешает?» → **`(a, b) -> a.length() - b.length()`; тип интерфейса и имя метода компилятор знает из того, что ждёт `sort`; метод в интерфейсе один; типы параметров выводятся.**
- «Когда нужны фигурные скобки и `return`?» → **когда тело — блок из нескольких инструкций; в выражении-теле return не пишут.**
- «Когда скобки вокруг параметров можно опустить?» → **только при одном параметре без явного типа.**
- Заблуждение: «лямбда — это отдельный тип данных». Нет: у лямбды всегда тип функционального интерфейса из контекста.
- Для любопытных: `this` внутри лямбды — внешний объект, внутри анонимного класса — сам анонимный объект; лямбда не создаёт `$1.class`.

### О2. Функциональный интерфейс, @FunctionalInterface, захват переменных
- «Что такое функциональный интерфейс?» → **ровно один абстрактный метод; default/static методы не считаются.**
- «Зачем `@FunctionalInterface`, если и без неё работает?» → **просит компилятор проверить, что метод один; страховка от второго абстрактного метода.**
- «Почему `list.forEach(x -> count++)` не компилируется?» → **захваченная локальная переменная должна быть final/effectively final: лямбда получает копию значения, менять её запрещено.**
- «Что такое effectively final?» → **значение присвоено один раз и больше не менялось — нигде, ни до, ни после лямбды.**
- Заблуждение: «effectively final — это только про изменения внутри лямбды». Нет: повторное присваивание снаружи тоже ломает.
- Заблуждение: «`Comparator` не функциональный, у него куча методов». Абстрактный — один `compare` (`equals` из Object не считается).

### О3. Встроенные интерфейсы java.util.function
- «Назови вход/выход и метод: Predicate, Function, Consumer, Supplier, BiFunction» → **T→boolean `test`; T→R `apply`; T→void `accept`; ()→T `get`; (T,U)→R `apply`.**
- «Какой интерфейс у `() -> new ArrayList<>()`? У `s -> System.out.println(s)`?» → **Supplier; Consumer.**
- «Что такое UnaryOperator и BinaryOperator?» → **Function<T,T> и BiFunction<T,T,T>.**
- «Что сделает `p1.and(p2)`, `p.negate()`, `f.andThen(g)`?»
- «Почему `Function<String, int>` нельзя?» → **в generics только ссылочные типы — Integer.**
- «Почему `List.of(...).removeIf(...)` падает?» → **List.of неизменяемый → UnsupportedOperationException.**
- «Напиши метод, который возвращает правило „цена меньше limit“» → **`static Predicate<Integer> cheaperThan(int limit) { return p -> p < limit; }`; тип возвращаемого значения — функциональный интерфейс, лямбда захватила параметр `limit` (он effectively final).** (урок 14.2)
- Лямбды без стримов — задача `m14-t12`: свой интерфейс, `removeIf`, `list.sort`, `forEach`.

### О4. Comparator через comparing / thenComparing
- В уроке 14.2 сортировка по двум полям — только лямбдами, через переменную `Comparator<Student> byScore = Comparator.comparing(s -> s.score())`; ссылки на методы и ловушка `comparing(лямбда).reversed()` — в уроке 14.3.
- «Отсортируй игроков: по очкам по убыванию, при равенстве — по имени» → **`Comparator.comparing(Player::score).reversed().thenComparing(Player::name)`** (или `comparingInt`).
- «Почему `Comparator.comparing(p -> p.score()).reversed()` не компилируется?» → **из-за `.reversed()` компилятор не выводит тип `p`, считает Object; лечится ссылкой на метод или `(Player p) -> …`.**
- Заблуждение: «`reversed()` в конце цепочки переворачивает только последнее поле». Нет — `reversed()` переворачивает **весь** компаратор, построенный до него. `comparing(A).thenComparing(B).reversed()` перевернёт и A, и B.

### О5. Ссылки на методы
- «Четыре вида ссылок с примерами» → **статический `Integer::parseInt`; метод конкретного объекта `System.out::println`; метод параметра `String::length`/`Student::name`; конструктор `ArrayList::new`.**
- «Почему `String::length` работает, если `length` не статический?» → **параметр сам становится объектом, у которого вызывают метод.**
- «Можно ли ссылкой: `s -> s.substring(1)`, `x -> x * 2`, `(a, b) -> a.compareTo(b)`?» → **нет, нет, да (`String::compareTo`).**
- Заблуждение: «`System.out::println()` со скобками». Скобок нет — ссылка не вызывает метод.

### О6. Конвейер стрима, ленивость, порядок
- «Из каких частей состоит стрим?» → **источник → промежуточные (возвращают Stream) → одна терминальная.**
- «Что такое ленивость?» → **промежуточные операции только описывают; без терминальной ничего не выполняется.**
- «В каком порядке идут элементы?» → **по одному через всю цепочку («вертикально»); `limit`/`findFirst`/`anyMatch` останавливают раньше; `sorted` собирает все элементы.**
- «Почему `limit(3)` должен стоять после `sorted`?» → **иначе возьмём первые три из источника и отсортируем только их.**
- «Меняет ли `list.stream().map(...)` сам список?» → **нет, источник не трогается.**
- «Можно ли использовать поток дважды?» → **нет: IllegalStateException; нужен новый `list.stream()`.**
- «Как получить стрим из массива, из строки со словами, из диапазона?» → **`Arrays.stream`, `Arrays.stream(line.trim().split("\\s+"))`, `IntStream.range/rangeClosed`.**
- «Почему `Arrays.stream(new int[]{1, 2}).toList()` не компилируется?» → **для `int[]` получается `IntStream`, у него нет `toList()`; нужен `.boxed()` → `Stream<Integer>`.**
- «Разбери строку `Вика:90 Аня:75` в список record-ов» → **`split("\\s+")` → `map(s -> s.split(":"))` → `map(p -> new Player(p[0], Integer.parseInt(p[1])))` → `toList()`. Что будет на записи без двоеточия? `ArrayIndexOutOfBoundsException`.** (урок 14.4; нужно для задач t06, t07, t10)
- «Чем `map` отличается от `flatMap`?» → **map — один в один; flatMap — лямбда возвращает стрим, и все стримы сливаются в один (один во много: 0, 1, 2…). Пример: `carts.stream().flatMap(List::stream)`, слова из строк `flatMap(l -> Arrays.stream(l.split(" ")))`.** (урок 14.4, задача `m14-t11`)
- Заблуждение: «filter сначала обходит весь список, потом map». Нет.
- Заблуждение: «`"".split("\\s+")` даёт пустой массив». Даёт `[""]`.

### О7. Терминальные операции
- «Что возвращают: toList, count, anyMatch, findFirst, forEach, max?» → **List, long, boolean, Optional, void, Optional.**
- «`toList()` vs `collect(Collectors.toList())`?» → **первый неизменяемый (Java 16+), второй — изменяемый (на практике ArrayList).**
- «Почему у `Stream<Integer>` нет `sum()`?» → **Stream не знает, что внутри числа; нужен `mapToInt` → IntStream.**
- «Что вернёт `average()` и почему не double?» → **OptionalDouble: у пустого стрима среднего нет.**
- «`allMatch` на пустом стриме?» → **true (нарушителей нет). В проверках сначала убедиться, что список не пуст.**
- «Объясни `reduce(0, (acc, x) -> acc + x)`. Какое identity для произведения? Что будет с `reduce(10, Integer::sum)` на 1,2,3?» → **накопитель; 1; 16.**
- «reduce или collect?» → **reduce сворачивает в одно значение; collect складывает в контейнер (список, Map, строку). Список через reduce не собирают.**
- «findFirst или findAny?» → **findFirst — первый по порядку; findAny — любой подходящий (в параллельном стриме — какой найдётся быстрее).**
- «Почему `stream.forEach(x -> list.add(x))` компилируется, а `count++` — нет? Чем он плох?» → **переменной `list` ничего не присваивают, меняется объект; это побочный эффект: результат размазан, в parallelStream ломается; правильно — `toList()`/`collect`. forEach — только для последнего действия.** (урок 14.5)

### О8. Optional
- «Зачем Optional, если есть null?» → **тип явно говорит «может не быть», заставляет обработать пустоту; NPE не прилетает издалека.**
- «Как правильно достать значение?» → **orElse / orElseGet / orElseThrow / ifPresent / ifPresentOrElse / map + orElse.**
- «Что не так с `opt.get()`?» → **на пустом — NoSuchElementException; это тот же NPE.**
- «`orElse(make())` vs `orElseGet(() -> make())`?» → **orElse вычисляет аргумент всегда, orElseGet — только при пустом.**
- «`Optional.of(null)`?» → **NPE; для null — `ofNullable`.**
- «Где Optional использовать не принято?» → **поля, параметры методов, обёртки над коллекциями.**
- Склад: «найди товар по названию или упади с понятным сообщением» → **`filter(...).findFirst().orElseThrow(() -> new IllegalStateException("..."))`.**

### О9. Collectors и Мастерская «цикл или стрим»
- «joining с одним и тремя аргументами?» → **разделитель; разделитель, префикс, суффикс; работает только со строками.**
- «Что вернёт `groupingBy(Student::grade)`? А с `counting()`? С `mapping(...)`?» → **Map<K, List<T>>; Map<K, Long>; Map<K, List<имён>>.**
- «Как сделать порядок ключей предсказуемым?» → **`groupingBy(key, TreeMap::new, downstream)`; HashMap порядок не обещает.**
- «partitioningBy vs groupingBy по boolean?» → **partitioningBy всегда создаёт оба ключа.**
- «Когда падает toMap и как починить?» → **одинаковые ключи → IllegalStateException: Duplicate key; третий аргумент — функция слияния.**
- «Как посчитать частоту слов?» → **`groupingBy(w -> w, TreeMap::new, counting())`, предварительно toLowerCase.**
- «Когда цикл понятнее стрима?» → **индексы/соседи, несколько меняющихся переменных, сложный break, изменение на месте.**
- «Как отлаживать стрим?» → **peek, разбить на шаги с промежуточными списками, Trace Current Stream Chain в отладчике IDEA. peek не для логики: в `list.stream().peek(...).count()` он может не вызваться.**

## Банк головоломок

Давай по одной. Сначала ответ и «почему», потом разбор. ★★★ — честно предупреждай, что это сверх программы.

### Что выведется?
1. ```java
   Stream.of(1, 2, 3).peek(System.out::println).map(x -> x * 2);
   System.out.println("конец");
   ```
   → только `конец` (нет терминальной операции).
2. ```java
   Stream.of("a", "b").filter(x -> { System.out.println("filter " + x); return true; })
         .forEach(x -> System.out.println("forEach " + x));
   ```
   → `filter a`, `forEach a`, `filter b`, `forEach b` (вертикально).
3. `Stream.of(1,2,3,4,5).peek(x -> System.out.println("взял " + x)).filter(x -> x > 1).limit(2).toList()` + печать результата → `взял 1`, `взял 2`, `взял 3`, `[2, 3]`.
4. `Stream.of(2, 1).peek(x -> System.out.println("peek " + x)).sorted().forEach(x -> System.out.println("out " + x))` → `peek 2`, `peek 1`, `out 1`, `out 2` (sorted ждёт всех).
5. `Stream.of(1, 2, 3).reduce(10, Integer::sum)` → `16`.
6. `List.of().stream().allMatch(x -> false)` → `true`; `anyMatch` → `false`.
7. `Optional.of("A").orElse(make())`, где make печатает «make» → `make`, потом `A`. С `orElseGet` — только `A`.
8. `Stream.of(3, 1, 2).sorted().findFirst()` печатаем → `Optional[1]`.
9. `IntStream.range(1, 5).sum()` → `10` (5 не входит).
10. `Stream.of(3, 1, 3, 2, 1).distinct().sorted().toList()` → `[1, 2, 3]`.
11. `Function<Integer,Integer> twice = x -> x * 2, plus3 = x -> x + 3;` `twice.andThen(plus3).apply(5)` → `13`; `twice.compose(plus3).apply(5)` → `16` (★ compose не проходили — только если ученик быстрый).
12. `Stream.of("b", "A", "a").sorted().toList()` → `[A, a, b]` (заглавные раньше строчных по кодам символов).
13. `Stream.of(1,2,3,4,5).collect(Collectors.partitioningBy(x -> x % 2 == 0))` → `{false=[1, 3, 5], true=[2, 4]}`.
14. ★★★ `List.of(1, 2, 3).stream().peek(System.out::println).count()` → только `3`! Java 9+ знает размер источника и не гонит элементы через peek. Мораль: peek — не для логики.

### Скомпилируется ли?
15. `int count = 0; list.forEach(s -> count++);` → **нет**: effectively final.
16. `String p = "Hi "; p = "Hello "; list.forEach(s -> System.out.println(p + s));` → **нет**: p присвоена дважды.
17. `int[] count = {0}; list.forEach(x -> count[0]++);` → **да** (ссылка на массив не меняется, меняется его содержимое). Обсудить, почему это «хак» (побочный эффект, как `forEach(x -> list.add(x))` из урока 14.5), а правильно — `count()`.
17а. `List<Integer> r = IntStream.range(0, 3).toList();` → **нет**: у IntStream нет toList, нужен `.boxed()`.
18. `players.sort(Comparator.comparing(p -> p.score()).reversed());` → **нет**: cannot find symbol score() — p выведен как Object.
19. `interface Calc { int apply(int a, int b); int reset(); }  Calc c = (a, b) -> a + b;` → **нет**: not a functional interface. Если `reset` сделать `default` — да.
20. `int n = list.stream().count();` → **нет**: long → int, possible lossy conversion.
21. `names.forEach(System.out::println());` → **нет**: у ссылки на метод нет скобок.
22. `Comparator<String> c = (a, b) -> { a.length() - b.length() };` → **нет**: в блоке нужен `return` и `;`.
23. `List.of(1, 2, 3).stream().sum()` → **нет**: у Stream<Integer> нет sum, нужен mapToInt.
24. `Map<Integer, Long> m = …groupingBy(String::length, TreeMap::new, counting())` — **да**. А то же выражение прямо внутри `System.out.println(...)` — **нет**: компилятор не выводит тип R (любопытный случай из проверки модуля; рассказывать, только если ученик сам наткнётся).

### Скомпилируется, но что при запуске?
25. `Stream<String> s = list.stream(); s.count(); s.count();` → `IllegalStateException: stream has already been operated upon or closed`.
26. `List.of("Аня").stream().filter(n -> n.startsWith("Я")).findFirst().get()` → `NoSuchElementException: No value present`.
27. `Stream.of(1, 2).toList().add(3)` → `UnsupportedOperationException`. С `collect(Collectors.toList())` — работает.
28. `Stream.of("a", "b", "a").collect(Collectors.toMap(s -> s, s -> 1))` → `IllegalStateException: Duplicate key a`. Починка: `toMap(s -> s, s -> 1, Integer::sum)` → `{a=2, b=1}`.
29. `List.of(1, 2).removeIf(x -> x == 1)` → `UnsupportedOperationException`.
30. `Optional.of(null)` → `NullPointerException`.
31. ★★★ `Stream.of(new int[]{1, 2, 3}).count()` → `1` (массив примитивов — один объект). Правильно: `Arrays.stream(arr)` / `IntStream.of(...)`.
32а. `Stream.of("a b", "c").map(s -> s.split(" ")).count()` → `2`; с `flatMap(s -> Arrays.stream(s.split(" ")))` → `3`.
32. `Arrays.stream("".split("\\s+")).map(Integer::parseInt).toList()` → `NumberFormatException: For input string: ""` — массив `[""]`, а не пустой. Починка: `.filter(s -> !s.isEmpty())`.

### Найди ошибку (логическую)
33. ```java
    List<String> top = products.stream()
            .limit(3)
            .sorted(Comparator.comparingInt(Product::price).reversed())
            .map(Product::title)
            .toList();
    ```
    → limit до sorted: берёт первые три из источника, а не три самых дорогих.
34. ```java
    List<String> names = words.stream().map(String::toLowerCase).limit(5).distinct().toList();
    ```
    → «5 разных слов» не получится: distinct после limit может оставить меньше пяти.
35. `boolean ok = prices.stream().allMatch(p -> p > 0);` в проверке, что цены в каталоге корректны → если каталог не загрузился и список пуст, проверка **пройдёт**. Нужно сначала проверить, что список не пустой.
36. `Comparator.comparing(Player::name).thenComparing(Player::score).reversed()` при задаче «по имени А→Я, при равенстве по очкам по убыванию» → reversed перевернул весь компаратор, имена пошли Я→А. Правильно: `comparing(Player::name).thenComparing(Comparator.comparing(Player::score).reversed())` или `thenComparing(Player::score, Comparator.reverseOrder())`.

## Мастерская (по шаблону: пойми → руками → роли → псевдокод → код → трассировка)

Задача на сайте — `m14-t09` «Частота символов». На разборе спроси:
- «Что на входе и на выходе? Что с регистром и пробелами?»
- «Как бы считал на бумаге?» → выписываю буквы и ставлю палочки — это группировка по символу + подсчёт.
- «Какие роли были бы у переменных в решении циклом?» → шагающая (индекс/символ), накопитель — Map<символ, счётчик>.
- «Покажи цикл, а потом стрим. Где в стриме спрятался накопитель?» → в `groupingBy(..., counting())`.
- «Почему TreeMap?» → детерминированный порядок по алфавиту.

Ещё задачи для Мастерской (ученик пишет код сам; сначала псевдокод вслух):
1. «Кто не сдал» (как «Неоплаченные заказы» в уроке 14.8, но другими данными): ученики, не сдавшие зачёт, от самого низкого балла к высокому; если таких нет — «Все сдали». Решить циклом и стримом, сравнить.
2. «Самое частое слово» в строке: `groupingBy` + `counting` → `entrySet().stream().max(Map.Entry.comparingByValue())`. При равенстве — по алфавиту (обсудить, как это сделать).
3. «Пагинация»: список из 23 строк, страницы по 10. Вывести страницу N через `skip`/`limit`; граничные случаи: N = 3 (3 строки), N = 4 (пусто).

## Дополнительные задачи (если ученик быстрый)

Код ученик пишет сам; проверяй глазами или попроси вставить вывод.
- «Фильтр логов»: из списка строк лога оставить строки с `ERROR`, убрать префикс даты (`substring`), вывести без повторов.
- «Корзина»: record `CartLine(String title, int price, int qty)` — общая сумма (`mapToInt(l -> l.price() * l.qty()).sum()`), самый дорогой товар (`max` + `orElseThrow`), есть ли товар с количеством 0 (`anyMatch`).
- «Свой функциональный интерфейс»: `@FunctionalInterface interface Check<T> { boolean ok(T value); }` + метод `static <T> List<T> keep(List<T> list, Check<T> rule)` — написать сначала циклом, потом понять, что это `filter`.
- «Студенты по группам»: `Map<String, Double>` — средний балл в каждой группе (`groupingBy(Student::group, TreeMap::new, averagingInt(Student::score))`) — ★ averagingInt не проходили, дать подсказку.
- «Слова из абзацев»: несколько строк текста → все слова в нижнем регистре без повторов, по алфавиту (`flatMap` + `map(String::toLowerCase)` + `distinct` + `sorted`).
