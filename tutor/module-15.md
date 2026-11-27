# Модуль 15 «Файлы и ввод-вывод» — план проверки знаний

> Файл для наставника. Здесь ответы — ученику не показывать (см. CLAUDE.md).
> Эталонные решения задач — `tutor/solutions/module-15/`. Заготовка задачи «почини» — `starter/module-15/TestLogAppendFix.java`.

## Критерии зачёта модуля

1. Все 8 задач `m15-t01…m15-t08` зелёные на сайте (`/api/status`) **и** по каждой ученик ответил на вопрос о своём коде (например: «что будет в файле, если запустить программу дважды?», «зачем здесь try (…)?», «почему список файлов берёшь из папки, а не из ввода?»).
2. Закрыты все цели О1–О9 ниже: по каждой ученик ответил своими словами хотя бы на один вопрос.
3. Головоломки: из 8–10 заданных решено правильно **с объяснением** не меньше 80%. Ошибся → разобрали → через какое-то время дать похожую ещё раз.
4. Мастерская: по задаче m15-t08 (или задаче из раздела «Мастерская» ниже) ученик **до кода** проговорил план: вход/выход и примеры руками (включая пустой файл), роли переменных, псевдокод.
5. Практический навык в IDEA: запустил программу, которая пишет файл, **сам нашёл этот файл** в дереве проекта (корень `workspace`, Reload from Disk) и объяснил, почему он не в папке `module-15`.

Экзамен можно вести за несколько занятий. Отмечай закрытые цели в `progress.md`.

## Цели модуля и вопросы

Формат: вопрос → **суть правильного ответа** → типичные заблуждения.

### О1. Пути и рабочая папка
- «Чем абсолютный путь отличается от относительного? Пример каждого» → **абсолютный — от корня диска (`C:\…`, `/home/…`), работает откуда угодно; относительный — от рабочей папки программы.**
- «Программа из `module-15` пишет `Path.of("notes.txt")`. Где файл, если запустить из IDEA?» → **в рабочей папке — корне проекта `workspace`, не в `module-15`.** Где посмотреть: Run → Edit Configurations → Working directory; из кода — `Path.of("").toAbsolutePath()` или `System.getProperty("user.dir")`.
- «`Path.of("nope.txt")` — упадёт, если файла нет?» → **нет: Path — только адрес, диск не трогается.**
- «Что вернут `getFileName`, `getParent`, `resolve` для `build/screens/login.png`?» → **`login.png`, `build/screens`, `resolve("x")` приклеивает часть.** `Path.of("a.txt").getParent()` — `null`.
- Заблуждение: «относительный путь считается от папки, где лежит .java-файл».
- Заблуждение: «в Java обязательно писать `\\` на Windows» — прямой `/` работает везде.

### О2. Чтение текста
- «Три способа прочитать текстовый файл и когда какой?» → **`readString` — весь текст строкой; `readAllLines` — список строк; `lines` — ленивый стрим для больших файлов (закрывать).**
- «Чем результат `readString` отличается от `readAllLines` по переводам строк?» → **в строке `\n` остаются, в списке отрезаны.**
- «Пустая строка в конце файла — что будет в `readAllLines`?» → **лишний элемент `""`; при разборе CSV — падение на `c[1]`. Фильтровать `isBlank()`.**
- «Что такое кодировка и почему кракозябры?» → **файл — байты, кодировка — таблица байты→буквы; прочитали UTF-8 как Windows-1251 → «РџСЂРёРІРµС‚». `Files` по умолчанию UTF-8.**

### О3. IOException и обработка ошибок
- «Почему без `throws IOException` код с `Files.readString` не компилируется?» → **IOException — checked: метод объявлен с throws, компилятор требует поймать или объявить. Причина — внешний мир (нет файла, прав, диска), а не ошибка программиста.**
- «Два способа ответить компилятору?» → **`throws IOException` в заголовке или `try-catch`.** В тестах — throws (тест упадёт красным, а не «молча зелёный»).
- «`NoSuchFileException: data.csv` — что проверяешь первым?» → **где рабочая папка и где реально лежит файл (`toAbsolutePath`).**
- «Можно ли поймать `IOException` вокруг `println`?» → **нет: «exception IOException is never thrown in body of corresponding try statement».**
- «Почему `forEach(Files::delete)` не компилируется?» → **метод `accept` у `Consumer` не объявляет checked-исключений; из лямбды IOException не выпустить. Цикл или try-catch внутри лямбды.**

### О4. Запись
- «Что делает `writeString` с существующим файлом?» → **стирает и пишет заново (по умолчанию CREATE + TRUNCATE_EXISTING + WRITE).**
- «Как дописать в конец?» → **`StandardOpenOption.CREATE, StandardOpenOption.APPEND`. Только APPEND на несуществующем файле — `NoSuchFileException`.**
- «Программа с APPEND запущена 3 раза — что в файле?» → **строки всех трёх запусков. Нужен лог одного запуска — очистить в начале обычной записью.**
- «Пишем в `reports/smoke/summary.txt`, папок нет — что будет и как починить?» → **`NoSuchFileException`; `Files.createDirectories(file.getParent())`.** Разница `createDirectory`/`createDirectories`.
- «Чем `Files.write(путь, список)` отличается от `writeString`?» → **write ставит системный перевод строки после каждого элемента; writeString пишет ровно то, что дали.**

### О5. Потоки ввода-вывода
- «Что такое поток (stream) в вводе-выводе? Нарисуй» → **труба, по которой байты текут по одному: InputStream — к программе, OutputStream — от программы. Источник любой: файл, сеть, клавиатура.**
- «Чем InputStream отличается от Reader?» → **байты против символов. Мост — InputStreamReader с кодировкой.**
- «`System.in` и `System.out` — это что?» → **InputStream и PrintStream (OutputStream).**
- «Зачем буфер?» → **меньше обращений к диску: черпаем вёдрами, а не каплями. `BufferedReader.readLine()`.**
- «Что возвращают `read()` и `readLine()` в конце?» → **−1 и `null`.** Почему `read()` возвращает `int`: нужен 257-й сигнал «конец».
- Заблуждение: «stream из модуля 14 и InputStream — одно и то же». Нет: совпадает слово, но Stream API — конвейер обработки элементов, а InputStream — источник байтов.

### О6. Закрытие ресурсов и try-with-resources
- «Зачем закрывать потоки? Три причины» → **файл занят (на Windows не удалить), лимит открытых файлов, данные из буфера записи не попадут на диск (файл пустой).**
- «Какой объект можно объявить в `try (…)`?» → **реализующий `AutoCloseable`. `String` нельзя.**
- «В каком порядке закрываются ресурсы? До или после catch?» → **в обратном порядке объявления; до catch и finally.**
- «Надо ли закрывать `Scanner(System.in)`?» → **нет: закроется сама клавиатура для программы. Правило — закрывай то, что сам открыл.**
- «Почему `Files.lines` нужно закрывать, а `readAllLines` — нет?» → **readAllLines закрывает файл сам до возврата; стрим держит файл открытым, пока жив.**

### О7. Папки
- «`Files.list` против `Files.walk`?» → **один уровень против всего дерева; walk включает и саму стартовую папку. Оба — стримы, закрывать; порядок не гарантирован → sorted().**
- «Скопировать `report.html` в папку `archive`. Что передаёшь вторым аргументом?» → **полный путь нового файла `archive.resolve(report.getFileName())`, не папку.**
- «Второй запуск `Files.copy` без опций?» → **FileAlreadyExistsException; REPLACE_EXISTING.**
- «Как удалить непустую папку?» → **walk → sorted(reverseOrder) → собрать список в try → удалять циклом.** `delete` vs `deleteIfExists`; удаление без корзины.
- «Зачем временные файлы?» → **`createTempFile/createTempDirectory` — уникальные имена, не мусорим в проекте; JUnit `@TempDir` (модуль 17).**

### О8. CSV
- «Как разобрать строку CSV в объект?» → **split(",") → trim каждой ячейки → parse чисел → new Record(...). Заголовок пропустить, пустые строки отфильтровать.**
- «`"a,b,,".split(",").length`?» → **2; все ячейки — `split(",", -1)` → 4.**
- «Почему простой split не справится с `"Иванов, Иван",admin`?» → **запятая внутри кавычек; нужна библиотека или `@CsvFileSource`.**
- «Русский Excel и CSV?» → **разделитель `;`.**

### О9. Properties и конфиг стенда
- «Зачем адрес стенда выносят в файл?» → **сменить стенд без изменения и перекомпиляции кода; CI запускает те же тесты на разных стендах.**
- «Формат properties: комментарии, пробелы, повтор ключа?» → **`#`/`!` — комментарии; пробелы вокруг `=` отбрасываются; повтор ключа — побеждает последний.**
- «`getProperty("x")` и `getProperty("x", "d")` для отсутствующего ключа?» → **`null` и `"d"`.**
- «Таймаут в properties — как получить int?» → **всё — строки: `Integer.parseInt(getProperty(...))`.**
- ★ «Почему `load(InputStream)` портит кириллицу?» → **читает в ISO-8859-1; нужен Reader в UTF-8.**

## Банк головоломок

Давай по одной. Сначала ответ и «почему», потом разбор. Если не сказано иное: `main` с `throws IOException`, импорты есть, запуск из IDEA (рабочая папка — корень проекта).

### Что выведется / что окажется в файле?
1. ```java
   Path p = Path.of("a.txt");
   Files.writeString(p, "one\n");
   Files.writeString(p, "two\n");
   System.out.println(Files.readAllLines(p));
   ```
   → `[two]` — вторая запись перезаписала первую.
2. То же, но вторая запись с `StandardOpenOption.APPEND` → `[one, two]`.
3. ```java
   Files.write(p, List.of("anna", "boris"));
   Files.writeString(p, "vera", StandardOpenOption.APPEND);
   System.out.println(Files.readAllLines(p));
   ```
   → `[anna, boris, vera]` — `Files.write` поставил перевод строки после boris.
4. ```java
   Files.writeString(p, "anna");
   Files.writeString(p, "boris", StandardOpenOption.APPEND);
   System.out.println(Files.readAllLines(p));
   ```
   → `[annaboris]` — writeString перевод строки сам не ставит.
5. `System.out.println("anna,qwerty,,".split(",").length);` → `2` (хвостовые пустые отбрасываются).
6. Класс `Res` печатает «open X»/«close X». `try (Res a = new Res("A"); Res b = new Res("B")) { println("body"); throw new RuntimeException(); } catch (RuntimeException e) { println("catch"); } finally { println("finally"); }` → `open A, open B, body, close B, close A, catch, finally`.
7. В `config.properties`: `browser=chrome` и ниже `browser = edge`. `getProperty("browser") + " " + size()` → `edge 1`.
8. В папке `data` файл `a.csv` и подпапка `old` с `b.csv`. `Files.list(data).count()` и `Files.walk(data).count()` → `2` и `4`.
9. `System.out.println(Path.of("testdata/../config/./app.properties").normalize());` → `config/app.properties` (на Windows — с `\`).
10. `System.out.println(Path.of("report.txt").getParent());` → `null`.
11. ★ В файле `hi.txt` текст `Hi!`. Цикл `while ((b = in.read()) != -1) print(b + " ")` → `72 105 33`.
12. ★★ `String s = Files.readString(p); System.out.println(s.length());` — в файле `ab` и перевод строки, файл создан на Windows в Блокноте → `4` (`\r\n` — два символа); на Linux — `3`.

### Скомпилируется ли?
13. `main` без throws: `String t = Files.readString(Path.of("x.txt"));` → **нет**: unreported exception IOException.
14. `try (String name = "a.txt") { … }` → **нет**: String не AutoCloseable.
15. `try { System.out.println("hi"); } catch (IOException e) { }` → **нет**: IOException is never thrown in body of try.
16. `try (Stream<Path> s = Files.walk(dir)) { s.forEach(Files::delete); }` → **нет**: incompatible thrown types IOException in functional expression.
17. `list.forEach(line -> Files.writeString(log, line, StandardOpenOption.APPEND));` → **нет** по той же причине (лямбда, checked-исключение).
18. ★★ `text.split("\s+")` (один обратный слэш) → **да, с Java 15** — `\s` стал escape-последовательностью «пробел»! Компилируется, но делит только по пробелам, а не по `\n` и табам. Классическая ловушка: в Java-строке регулярное `\s` пишется `"\\s+"`.

### Скомпилируется, но что будет при запуске?
19. `Files.readAllLines(Path.of("users.csv"))`, файла нет → `NoSuchFileException: users.csv`.
20. Файл `data.txt` лежит в `module-15`, код `Files.readString(Path.of("data.txt"))`, запуск из IDEA → `NoSuchFileException` (ищет в корне проекта).
21. `Files.writeString(Path.of("run.log"), "x", StandardOpenOption.APPEND);` — файла нет → `NoSuchFileException` (нет CREATE).
22. ```java
    BufferedWriter w = Files.newBufferedWriter(Path.of("r.txt"));
    w.write("OK");
    ```
    без close → программа завершится без ошибок, **файл пустой**.
23. `Files.createDirectories(Path.of("notes.txt").getParent());` → `NullPointerException` (getParent — null).
24. `Files.copy(report, archiveDir)` — `archiveDir` существующая папка → `FileAlreadyExistsException` (вторым аргументом нужен путь файла).
25. `Files.delete(Path.of("reports"))`, в папке есть файлы → `DirectoryNotEmptyException`.
26. ★ `Scanner a = new Scanner(System.in); a.close(); Scanner b = new Scanner(System.in); b.nextLine();` → `NoSuchElementException`: System.in уже закрыт.
27. CSV с заголовком `name,score`, цикл с `i = 0`, `Integer.parseInt(cells[1])` → `NumberFormatException: For input string: "score"`.
28. CSV с пустой последней строкой, `line.split(",")[1]` → `ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1`.

### Найди ошибку
29. ```java
    Path log = Path.of("logs", "run.log");      // папки logs нет
    for (String test : tests) {
        Files.writeString(log, test + "\n");
    }
    ```
    → две: нет `createDirectories(log.getParent())`; каждая запись перезаписывает (нужно CREATE + APPEND и очистка перед циклом, или один `Files.write(log, tests)`).
30. ```java
    try (Stream<String> lines = Files.lines(p)) { }
    long n = lines.count();
    ```
    → `lines` не видна вне try (область видимости), а даже если вынести — стрим уже закрыт: `IllegalStateException`.
31. ```java
    Properties c = new Properties();
    c.load(Files.newBufferedReader(Path.of("config.properties")));
    int t = c.getProperty("timeout");
    ```
    → reader не закрыт (нужен try-with-resources); `getProperty` возвращает String — нужен `Integer.parseInt`.

## Мастерская (по шаблону: вход/выход → руками → роли → псевдокод → код → трассировка)

1. **m15-t08** «Сводка по прогону» — основная задача Мастерской модуля. Требуй план до кода: какие счётчики (PASSED/FAILED/SKIPPED — счётчики, `time` — накопитель, `slowest`/`slowestMs` — «максимум» с запоминанием имени, `failedNames` — накопитель-список), граничные случаи (0 тестов, все прошли, ничья по времени, разный регистр статуса).
2. **Устно:** «В `orders.csv` колонки `id,product,price,qty`. Найди товар с максимальной выручкой price × qty». Роли: шагающая `i`, временные `cells/price/qty`, «максимум» + «запомнить имя». Граничный: файл только с заголовком.
3. **Устно:** «В логе строки вида `2026-11-27 10:15:03 ERROR LoginTest: timeout`. Посчитай ошибки по каждому тесту». Ожидаем: `Files.lines` в try, filter по `ERROR`, выделить имя теста (split по пробелу, убрать `:`), `Map<String, Integer>` или `groupingBy + counting` (модуль 14).

## Дополнительные задачи для быстрых

1. **Дедупликация тестовых данных.** Прочитать `users.csv`, убрать повторяющиеся логины (оставить первое вхождение), записать `users-unique.csv` с тем же заголовком, вывести, сколько строк удалено. (LinkedHashSet / LinkedHashMap.)
2. **Ротация логов.** Если `run.log` больше N строк — переименовать его в `run.1.log` (старый `run.1.log` → `run.2.log`, `run.2.log` удалить), начать новый `run.log`. Тренирует `move`, `REPLACE_EXISTING`, `deleteIfExists`.
3. **Конфиг с переопределением.** Читать `config.properties`, но если задано системное свойство (`-Dbase.url=…` в Run Configuration → VM options), брать его: `System.getProperty(key, config.getProperty(key))`. Показать, как CI меняет стенд без правки файла.
4. **Поиск по проекту.** `Files.walk` от корня `workspace`: найти все `.java`-файлы, вывести топ-5 самых длинных по числу строк. (walk + filter + map к паре «имя — число строк» + sorted + limit; помнить про закрытие стрима и checked-исключение внутри map — обернуть в метод с try-catch → `UncheckedIOException`.)
5. ★★★ **Копия папки.** Скопировать всё дерево `testdata` в `testdata-backup` с сохранением подпапок: `walk` + `relativize` + `resolve` + `createDirectories` + `copy`.
