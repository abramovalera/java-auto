# Модуль 15 «Файлы и ввод-вывод» — план проверки знаний

> Файл для наставника. Здесь ответы — ученику не показывать (см. CLAUDE.md).
> Эталонные решения задач — `tutor/solutions/module-15/`. Заготовка задачи «почини» — `starter/module-15/OrderLogAppendFix.java`.

## Критерии зачёта модуля

1. Все 9 задач `m15-t01…m15-t09` зелёные на сайте (`/api/status`) **и** по каждой ученик ответил на вопрос о своём коде (например: «что будет в файле, если запустить программу дважды?», «зачем здесь try (…)?», «почему список файлов берёшь из папки, а не из ввода?», «почему в t09 один писатель, а не APPEND на каждой строке?»).
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
- «Что вернут `getFileName`, `getParent`, `resolve` для `photos/summer/sea.png`?» → **`sea.png`, `photos/summer`, `resolve("x")` приклеивает часть.** `Path.of("a.txt").getParent()` — `null`.
- Заблуждение: «относительный путь считается от папки, где лежит .java-файл».
- Заблуждение: «в Java обязательно писать `\\` на Windows» — прямой `/` работает везде.

### О2. Чтение текста
- «Три способа прочитать текстовый файл и когда какой?» → **`readString` — весь текст строкой; `readAllLines` — список строк; `lines` — ленивый стрим для больших файлов (закрывать).**
- «Чем результат `readString` отличается от `readAllLines` по переводам строк?» → **в строке `\n` остаются, в списке отрезаны.**
- «Пустая строка в конце файла — что будет в `readAllLines`?» → **лишний элемент `""`; при разборе CSV — падение на `c[1]`. Фильтровать `isBlank()`.**
- «Что такое кодировка и почему кракозябры?» → **файл — байты, кодировка — таблица байты→буквы; прочитали UTF-8 как Windows-1251 → «РџСЂРёРІРµС‚». `Files` по умолчанию UTF-8.**

### О3. IOException и обработка ошибок
- «Почему без `throws IOException` код с `Files.readString` не компилируется?» → **IOException — checked: метод объявлен с throws, компилятор требует поймать или объявить. Причина — внешний мир (нет файла, прав, диска), а не ошибка программиста.**
- «Два способа ответить компилятору?» → **`throws IOException` в заголовке или `try-catch`.** Без файла программе нечего делать — честнее throws (упадёт с понятным stack trace); catch — когда знаешь, что делать без файла (сообщение пользователю, значения по умолчанию). Плохо — поймать, напечатать «ой» и работать дальше.
- «`NoSuchFileException: data.csv` — что проверяешь первым?» → **где рабочая папка и где реально лежит файл (`toAbsolutePath`).**
- «Можно ли поймать `IOException` вокруг `println`?» → **нет: «exception IOException is never thrown in body of corresponding try statement».**
- «Почему `forEach(Files::delete)` не компилируется?» → **метод `accept` у `Consumer` не объявляет checked-исключений; из лямбды IOException не выпустить. Цикл или try-catch внутри лямбды.**

### О4. Запись
- «Что делает `writeString` с существующим файлом?» → **стирает и пишет заново (по умолчанию CREATE + TRUNCATE_EXISTING + WRITE).**
- «Как дописать в конец?» → **`StandardOpenOption.CREATE, StandardOpenOption.APPEND`. Только APPEND на несуществующем файле — `NoSuchFileException`.**
- «Программа с APPEND запущена 3 раза — что в файле?» → **строки всех трёх запусков. Нужен лог одного запуска — очистить в начале обычной записью.**
- «Пишем в `reports/shop/summary.txt`, папок нет — что будет и как починить?» → **`NoSuchFileException`; `Files.createDirectories(file.getParent())`.** Разница `createDirectory`/`createDirectories`.
- «Чем `Files.write(путь, список)` отличается от `writeString`?» → **write ставит системный перевод строки после каждого элемента; writeString пишет ровно то, что дали.**
- «В цикле надо записать 10 000 строк. Как?» → **один `Files.newBufferedWriter` в try-с-ресурсами, в цикле `write` + `newLine()` (или `PrintWriter.println`). `writeString` с APPEND на каждой строке 10 000 раз открывает и закрывает файл.**
- «`out.write("a"); out.write("b"); out.newLine(); out.write("c");` — что в файле?» → **две строки `ab` и `c`: write перевод строки не ставит.**

### О5. Потоки ввода-вывода
- «Что в Java называют словом „поток“?» → **три разные вещи: стрим (Stream API, модуль 14 — конвейер filter/map), поток ввода-вывода (InputStream/Reader — труба к файлу), поток выполнения (Thread, модуль 16 — исполнитель).** Заблуждение: «Files.lines возвращает InputStream» — нет, `Stream<String>`.
- «Что такое поток (stream) в вводе-выводе? Нарисуй» → **труба, по которой байты текут по одному: InputStream — к программе, OutputStream — от программы. Источник любой: файл, сеть, клавиатура.**
- «Чем InputStream отличается от Reader?» → **байты против символов: …Stream — байты, …Reader/…Writer — символы.** ★ Мост InputStreamReader и цепочки-обёртки (декоратор) — в уроке «для любопытных», на зачёт не требовать.
- «`new FileWriter("log.txt", true)` — что значит true?» → **дописывать в конец (как CREATE + APPEND); без него файл перезаписывается.**
- «Чем `FileNotFoundException` отличается от `NoSuchFileException`?» → **первое бросают старые классы (`FileReader`, `FileWriter`, `FileInputStream`), второе — `Files`; оба наследники IOException.** FileWriter бросает его и когда нет папки.
- «Scanner или BufferedReader?» → **Scanner разбирает числа и слова (`nextInt`), удобен для клавиатуры и маленьких файлов, медленнее; BufferedReader быстро читает строки целиком (`readLine` → `null` в конце), числа — `Integer.parseInt` самому.**
- «`System.in` и `System.out` — это что?» → **InputStream и PrintStream (OutputStream).**
- «Зачем буфер?» → **меньше обращений к диску: черпаем вёдрами, а не каплями. `BufferedReader.readLine()`.**
- «Что возвращают `read()` и `readLine()` в конце?» → **−1 и `null`.** Почему `read()` возвращает `int`: нужен 257-й сигнал «конец».
- Заблуждение: «stream из модуля 14 и InputStream — одно и то же». Нет: совпадает слово, но Stream API — конвейер обработки элементов, а InputStream — источник байтов.

### О6. Закрытие ресурсов и try-with-resources
- «Зачем закрывать потоки? Три причины» → **файл занят (на Windows не удалить), лимит открытых файлов, данные из буфера записи не попадут на диск (файл пустой).**
- «Какой объект можно объявить в `try (…)`?» → **реализующий `AutoCloseable` (есть метод `close()`). `String` нельзя.** Свой класс с AutoCloseable (Tap) — «для любопытных», писать его не требовать.
- «В каком порядке закрываются ресурсы? До или после catch?» → **в обратном порядке объявления; до catch и finally.**
- «Надо ли закрывать `Scanner(System.in)`?» → **нет: закроется сама клавиатура для программы. Правило — закрывай то, что сам открыл.**
- «Почему `Files.lines` нужно закрывать, а `readAllLines` — нет?» → **readAllLines закрывает файл сам до возврата; стрим держит файл открытым, пока жив.**

### О7. Папки
- «`Files.list` против `Files.walk`?» → **один уровень против всего дерева; walk включает и саму стартовую папку. Оба — стримы, закрывать; порядок не гарантирован → sorted().**
- «Скопировать `report.html` в папку `archive`. Что передаёшь вторым аргументом?» → **полный путь нового файла `archive.resolve(report.getFileName())`, не папку.**
- «Второй запуск `Files.copy` без опций?» → **FileAlreadyExistsException; REPLACE_EXISTING.**
- «Что возвращает `Files.mismatch(a, b)`?» → **`-1`, если файлы одинаковые, иначе номер первого отличающегося байта (с 0) — не строки и не буквы.** Номер строки — через `readAllLines` и сравнение списков.
- «Как удалить непустую папку?» → **walk → sorted(reverseOrder) → собрать список в try → удалять циклом.** `delete` vs `deleteIfExists`; удаление без корзины.
- «Зачем временные файлы?» → **`createTempFile/createTempDirectory` — уникальные имена, не мусорим в проекте.**

### О8. CSV
- «Как разобрать строку CSV в объект?» → **split(",") → trim каждой ячейки → parse чисел → new Record(...). Заголовок пропустить, пустые строки отфильтровать.**
- «`"a,b,,".split(",").length`?» → **2; все ячейки — `split(",", -1)` → 4.**
- «Почему простой split не справится с `"Иванов, Иван",admin`?» → **запятая внутри кавычек; нужна библиотека для CSV.**
- «Русский Excel и CSV?» → **разделитель `;`.**

### О9. Properties и конфиг программы
- «Зачем адрес сервера, язык, громкость выносят в файл?» → **поменять настройку без изменения и перекомпиляции кода; одна и та же программа работает с разными настройками.**
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
7. В `config.properties`: `language=ru` и ниже `language = en`. `getProperty("language") + " " + size()` → `en 1`.
8. В папке `data` файл `a.csv` и подпапка `old` с `b.csv`. `Files.list(data).count()` и `Files.walk(data).count()` → `2` и `4`.
9. `System.out.println(Path.of("data/../config/./app.properties").normalize());` → `config/app.properties` (на Windows — с `\`).
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
    for (String order : orders) {
        Files.writeString(log, order + "\n");
    }
    ```
    → две: нет `createDirectories(log.getParent())`; каждая запись перезаписывает. Лучше всего — один `BufferedWriter` на весь цикл (`write` + `newLine`) или один `Files.write(log, orders)`; CREATE + APPEND на каждой строке работает, но открывает файл заново каждый раз.
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
32. ★ Файла `log.txt` нет. Цикл из двух итераций: `try (FileWriter w = new FileWriter("log.txt", true)) { w.write("run " + i + "\n"); }` → в файле `run 1`, `run 2`. Без `true` → только `run 2`.
33. `new FileReader("nope.txt")`, файла нет → `FileNotFoundException: nope.txt (Не удается найти указанный файл)`; в stack trace `<init>` — конструктор.
34. `try (BufferedWriter out = Files.newBufferedWriter(p)) { for (String s : List.of("a", "b")) out.write(s); } System.out.println(Files.readAllLines(p));` → `[ab]` — нет `newLine()`.
35. В `a.txt` — `кот`, в `b.txt` — `кит` (UTF-8). `Files.mismatch(a, b)` → `3`: байты, а не буквы (к = 2 байта, у «о» и «и» первый байт общий).
36. «Какой это поток?» `Files.lines(p)`, `System.in`, `new Thread(...)`, `Files.newBufferedReader(p)` → стрим (Stream API); поток ввода-вывода; поток выполнения; поток ввода-вывода (Reader).

## Мастерская (по шаблону: вход/выход → руками → роли → псевдокод → код → трассировка)

1. **m15-t08** «Сводка по заказам за день» — основная задача Мастерской модуля. Требуй план до кода: какие роли (PAID/CANCELLED/PENDING — счётчики, `paidSum` — накопитель только для PAID, `maxItem`/`maxPrice` — «максимум» с запоминанием имени, `cancelledItems` — накопитель-список, потом сортировка), граничные случаи (0 заказов, никто не заплатил, одинаковые цены — берём первый, разный регистр статуса и пробелы).
2. **Устно:** «В `orders.csv` колонки `id,product,price,qty`. Найди товар с максимальной выручкой price × qty». Роли: шагающая `i`, временные `cells/price/qty`, «максимум» + «запомнить имя». Граничный: файл только с заголовком.
3. **Устно:** «В логе сервера строки вида `2026-11-27 10:15:03 ERROR payment: timeout`. Посчитай ошибки по каждой части программы (payment, cart…)». Ожидаем: `Files.lines` в try, filter по `ERROR`, выделить имя (split по пробелу, убрать `:`), `Map<String, Integer>` или `groupingBy + counting` (модуль 14).

## Дополнительные задачи для быстрых

1. **Дедупликация списка клиентов.** Прочитать `users.csv`, убрать повторяющиеся логины (оставить первое вхождение), записать `users-unique.csv` с тем же заголовком, вывести, сколько строк удалено. (LinkedHashSet / LinkedHashMap.)
2. **Ротация логов.** Если `run.log` больше N строк — переименовать его в `run.1.log` (старый `run.1.log` → `run.2.log`, `run.2.log` удалить), начать новый `run.log`. Тренирует `move`, `REPLACE_EXISTING`, `deleteIfExists`.
3. **Конфиг с переопределением.** Читать `config.properties`, но если задано системное свойство (`-Dserver.url=…` в Run Configuration → VM options), брать его: `System.getProperty(key, config.getProperty(key))`. Показать, как запустить ту же программу с другим сервером без правки файла.
4. **Поиск по проекту.** `Files.walk` от корня `workspace`: найти все `.java`-файлы, вывести топ-5 самых длинных по числу строк. (walk + filter + map к паре «имя — число строк» + sorted + limit; помнить про закрытие стрима и checked-исключение внутри map — обернуть в метод с try-catch → `UncheckedIOException`.)
5. ★★★ **Копия папки.** Скопировать всё дерево `photos` в `photos-backup` с сохранением подпапок: `walk` + `relativize` + `resolve` + `createDirectories` + `copy`.
