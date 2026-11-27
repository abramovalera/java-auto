# Техзадание автору модуля курса «Java с нуля»

Ты пишешь **один модуль** курса. Курс — сайт в Docker (`http://localhost:7070`, уже запущен), ученик — полный новичок (брат автора курса), цель — крепкая Java как фундамент для **автотестов** (JUnit, AssertJ, Selenide). Общайся с файлами по-русски, тексты уроков — по-русски.

Сначала прочитай:
1. `AUTHORING.md` — правила курса (цель, проверки знаний, головоломки, «Мастерская алгоритмов», стиль). **Обязательно.**
2. Эталоны уроков нового формата: `site/lessons/m01/01-what-is-java.html`, `02-how-java-works.html`, `04-first-program-idea.html` — копируй их структуру, не изобретай свою.
3. `site/js/widgets.js` — какие интерактивы есть (кратко описаны ниже), `site/css/style.css` — какие CSS-классы есть.
4. Формат задач — в `AUTHORING.md` и примеры в `tasks/m01/*.task`. План проверки знаний — пример `tutor/module-01.md`.

## Что сдать (только в своих папках!)

Пусть модуль — `mXX` (две цифры), папка кода ученика — `module-XX`.

| Что | Куда |
|---|---|
| 5–8 уроков нового формата | `site/lessons/mXX/NN-slug.html`, id уроков `mXX-01`, `mXX-02`, … |
| Головоломки «как на собеседовании» (12–15 штук) | `site/lessons/mXX/NN-puzzles.html`, id `mXX-pz` |
| Итоговый урок модуля | `site/lessons/mXX/NN-summary.html`, id `mXX-sum` (последний в списке) |
| 6–10 задач с автопроверкой | `tasks/mXX/NN-slug.task`, id `mXX-t01`… |
| Эталонные решения задач | `tutor/solutions/module-XX/ИмяКласса.java` |
| Заготовки (если есть задача «почини файл») | `starter/module-XX/ИмяКласса.java` |
| План экзамена для наставника | `tutor/module-XX.md` |
| Манифест модуля | `site/lessons/mXX/_manifest.json` |

**Нельзя трогать** чужие и общие файлы: `site/js/*`, `site/css/*`, `site/index.html`, `site/tasks.html`, `server/*`, `CLAUDE.md`, `AUTHORING.md`, `README.md`, другие модули. Нужен новый виджет или CSS — **не делай**, обойдись имеющимися и напиши об этом в отчёте. Не пользуйся встроенным браузером (он один на всех) — визуальную проверку сделает координатор.

### Манифест `_manifest.json`
```json
{ "id": "m02", "num": "2", "title": "Переменные и типы данных",
  "summary": "Одна строка для карты курса.",
  "lessons": [ { "id": "m02-01", "title": "Короткое название", "url": "/lessons/m02/01-variables.html" }, … ,
               { "id": "m02-pz", "title": "Головоломки: что выведется?", "url": "/lessons/m02/08-puzzles.html" },
               { "id": "m02-sum", "title": "Итоги модуля и экзамен", "url": "/lessons/m02/09-summary.html" } ],
  "taskCount": 8 }
```
Страница задач в меню встанет автоматически перед итоговым уроком.

## Урок нового формата — обязательная структура

```html
<!doctype html>
<html lang="ru">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Название урока · Java с нуля</title>
  <link rel="stylesheet" href="/css/style.css">
  <script src="/js/course.js"></script>
  <script src="/js/app.js" defer></script>
</head>
<body>
<main class="content" data-lesson="mXX-NN">
  <header class="lesson-head" data-mood="happy|think|wow|oops|cool">
    <div class="kicker">Модуль X · Урок N</div>
    <h1>…</h1>
    <p class="lead">…</p>
    <div class="meta"><span>⏱ 15 минут</span><span>…</span></div>
  </header>
  <section class="step"> <h2>…</h2> … </section>   <!-- 5–9 шагов; весь урок виден сразу, кнопок «Продолжить» нет -->
  …
  <section class="step"> <h2>Проверь себя</h2> (быстрая проверка check) + callout.ai + div.summary </section>
</main>
</body>
</html>
```
Кнопку «Завершить урок», навигацию, меню, номера шагов добавляет `app.js` — не пиши их.

**Со 2-го модуля:** первый шаг урока (кроме первого урока модуля) — «Повторение»: `check` на 2–3 вопроса по прошлым урокам/модулям.

### Компоненты (только классы из style.css)
- Реплика маскота Джавика: `<div class="say" data-mood="think"><p>…</p></div>` — для живых пояснений, 1–3 на урок.
- Плашки: `<div class="callout analogy|tip|warn|try|ai|fact"><span class="callout-title">…</span><p>…</p></div>`. `callout.ai` — конкретная фраза, которую ученик может написать наставнику.
- Код: `<pre data-file="Main.java"><code class="java">…</code></pre>` (экранируй `&lt; &gt; &amp;`). Код нельзя скопировать — это специально.
- Вывод программы: `<pre class="terminal"><span class="out">…</span></pre>`.
- Построчный разбор: `<div class="annot"><div class="annot-row"><code>строка</code><div><p>пояснение</p></div></div>…</div>`.
- Схема: `<figure class="fig"><svg class="wide" viewBox=…>…</svg><figcaption>…</figcaption></figure>` — **цвета только классами `d-*`** (`d-box d-soft d-accent d-teal d-blue d-violet d-green d-red d-yellow d-code d-line d-dash d-arrow d-text d-title d-small d-mono d-mono-ink d-c-*`), маркер стрелки `<marker id="arrow">…<path class="d-head"/>`. Минимум 1 схема на урок. Текст не должен вылезать за рамки прямоугольников.
- «Предскажи»: `<details class="predict"><summary>Вопрос</summary><p>Ответ</p></details>` — по ходу текста.
- Термины: `<dl class="terms"><dt>…</dt><dd>…</dd></dl>`; карточки `.cards > .card` (`.emoji`, `h4`, `p`); шаги `ol.steps`; таблица `.table-wrap > table`; итог `div.summary` с `<h2>Коротко</h2>`.
- Для любопытных (необязательная глубина): `<details class="curious"><summary>🔎 Для любопытных: …</summary>…</details>`.

### Интерактивы (виджеты) — данные в `<script type="application/json">` внутри div
**`check` — Быстрая проверка (главный инструмент, 1–2 на урок).** Типы заданий:
```json
{ "title": "…", "items": [
  { "type": "single", "q": "Вопрос", "code": "необязательный Java-код", "options": ["A","B","C"], "answer": 1, "why": "пояснение" },
  { "type": "multi", "q": "Отметь все…", "options": ["…","…","…"], "answer": [0,2], "why": "…" },
  { "type": "tf", "q": "Верно или нет?", "statements": [ { "s": "утверждение", "a": true }, { "s": "…", "a": false, "why": "почему неверно" } ] },
  { "type": "match", "q": "Соедини", "left": ["термин1","термин2"], "right": ["смысл1","смысл2"], "why": "…" },

  { "type": "predict", "q": "Что выведет?", "code": "System.out.println(1 + 2 + \"3\");", "answer": "33", "why": "…" },
  { "type": "bug", "q": "Найди строку с ошибкой", "code": "строка1\nстрока2\nстрока3", "answer": [2], "why": "…" },
  { "type": "order", "q": "Расставь по порядку", "lines": ["…","…"], "code": true, "why": "…" }
] }
```
`right` в match — в том же порядке, что `left` (перемешает виджет). **Тип `blank` (вписать пропуск в код) запрещён** — автор курса: «код пишется только в IDE». `order` — один перемешанный список, ученик перетаскивает строки. `predict`: ответ сравнивается построчно без хвостовых пробелов. Разметка: `<div data-widget="check" id="check-mXX-NN"><script type="application/json">{…}</script></div>`. **id уникальны в пределах урока.** Перемешивай типы, 4–7 заданий. Не делай правильный ответ самым длинным.

**`trace` — Трассировщик** (циклы, массивы, методы, память). Код + шаги:
```json
{ "title": "…", "intro": "…", "code": "int sum = 0;\nfor (int i = 1; i <= 3; i++) {\n    sum += i;\n}\nSystem.out.println(sum);",
  "steps": [ { "line": 1, "vars": { "sum": "0" }, "note": "…" },
             { "line": 3, "vars": { "sum": "1", "i": "1" }, "ask": ["sum"], "note": "…" },
             { "line": 5, "vars": { "sum": "6" }, "out": "6\n" } ] }
```
`ask` — ученик сам вписывает значение (трассировочная таблица). Память/ссылки: вместо `vars` — `"stack": [ { "frame": "main", "vars": { "a": "5", "cat": "→#1" } } ], "heap": [ { "id": "#1", "label": "Кот", "fields": { "имя": "\"Мурзик\"" } } ]` — значение `"→#1"` рисуется стрелкой к объекту. Несколько фреймов в `stack` = стек вызовов методов (последний — верхний). Подпись кадра можно писать с аргументами: `"frame": "factorial(n=3)"`, а возвращаемое значение — полем `"returns": "6"`. В `ask` значение ищется в `vars` шага, а если их нет — в верхнем кадре стека; поле объекта в куче спрашивается так: `"ask": ["#1.имя"]`.

**`parsons`** — собрать строки по порядку: `<div data-widget="parsons" id="…" data-code data-title="…" data-success="…"><script type="text/plain">строка1\nстрока2…</script></div>` (с `data-code` — моноширинный код с отступами).

**`buckets`** — разложить карточки по корзинам (как «Java или не Java?» в уроке 1.1). Используй редко.

**`ide` — «скрин» IntelliJ IDEA. ПРАВИЛО: если в тексте говорим о чём-то, что ученик увидит на экране (окно, меню, диалог, ошибка компиляции, исключение и stack trace, консоль, отладчик, Git), — рядом ОБЯЗАТЕЛЬНО такой скрин с выносками-номерами.** Новичок никогда не видел «ошибку компиляции» или «stack trace» — словами это не объяснить, нужно показать, как это выглядит и куда смотреть. Это картинка, собранная из данных (тёмная тема IDEA, новый интерфейс). Образцы: `site/lessons/m01/04-first-program-idea.html` (меню, popup, консоль) и `site/lessons/m01/08-errors.html` (ошибки, Build, исключение).
```html
<div data-widget="ide" id="shot-уникальный-id"><script type="application/json">{ … }</script></div>
```
Поля (все необязательные):
- `code` — текст файла (`\n` между строками, отступы пробелами); `file` — имя вкладки; `tabs` — несколько вкладок; `caretLine` — строка с курсором.
- `tree` — дерево проекта: `[{ "name": "module-01", "type": "folder|src|class|test|file|lib|out", "depth": 0, "open": true, "selected": true, "note": "серый текст" }]`; `false` — без дерева (так чаще всего, чтобы влез код); по умолчанию — workspace/module-01/FixMe.
- `gutterRun: [1,2]` — зелёные ▶ на полях; `breakpoints: [4]` — красные точки останова; `execLine: 4` — синяя строка, где стоит отладчик, `inlineVals: "x = 5   y = 7"` — серые значения справа от неё.
- **Ошибки:** `errors: [{ "line": 3, "text": "Sytem" | "col": 44, "len": 1, "msg": "Cannot resolve symbol 'Sytem'", "tip": true, "fix": "Insert ';'" | false }]` — красная волна под куском строки; `tip: true` — всплывашка с текстом ошибки, как при наведении мыши (одна на скрин). `warnings: […]` — то же жёлтым (предупреждение). Справа вверху сам появится счётчик ❗N / ⚠N; `"problems": true` без ошибок — зелёная ✓. `errorLines`/`dimLines: [1]` — подчеркнуть строку целиком / сделать серой (неиспользуемый импорт).
  Тексты ошибок в редакторе IDEA пиши как в настоящей IDEA (англ.): `Cannot resolve symbol 'x'`, `';' expected`, `Incompatible types. Found: 'java.lang.String', required: 'int'`, `Missing return statement`, `Variable 'x' might not have been initialized`, `Unhandled exception: java.io.IOException`, `Cannot assign a value to final variable 'x'`, `Non-static method 'f()' cannot be referenced from a static context`, `Unreachable statement` и т. п.
  На одной строке можно несколько ошибок/предупреждений — цели `err-N`, `err-N-2`, `err-N-3`… (по порядку в массиве). `unused: [{ "line": 3, "text": "helper" }]` — серое имя (не используется), в счётчике как предупреждение. `vals: { "3": "n: 1", "5": "sum: 6" }` — серые значения справа от нескольких строк (как в отладчике).
- `build: { "file": "Hello.java", "errors": [{ "msg": "';' expected", "line": 3, "col": 44 }] }` — панель **Build** после неудачного запуска (дерево ошибок + вывод `путь:строка:столбец` / `java: …`). `errors: []` — «Build completed successfully».
- `console: { "tool": "Run"|"Debug", "title": "Main", "lines": [{ "t": "cmd|out|in|err|exit", "s": "текст" }], "cursor": true, "running": true }` — консоль. `cmd` — серая строка запуска java.exe, `in` — зелёный ввод с клавиатуры (цели `input`, `input-2`…; `"inline": true` — на той же строке, что предыдущий `print`: { "t": "out", "s": "Возраст: " }, { "t": "in", "s": "25", "inline": true }), `err` — красный (исключения); в строках `err` вида `\tat Main.main(Main.java:5)` ссылка `(Main.java:5)` сама станет синей (цели `link-1`, `link-2`…). Stack trace — каждая строка `at …` отдельным `err`.
- `debug: { "title": "Main", "frames": ["factorial:5, Main", "main:9, Main"], "sel": 0, "vars": [{ "name": "n", "value": "3" }, { "name": "cat", "type": "Cat@512", "value": "", "open": true }, { "name": "name", "value": "\"Мурзик\"", "depth": 1 }] }` — панель отладчика (кадры стека и переменные, кнопки Step Over F8 / Step Into F7 / Resume F9).
- `tests: { "title": "CalcTest", "tree": [{ "name": "CalcTest", "status": "fail", "time": "42 ms" }, { "name": "adds()", "status": "pass|fail|skip", "depth": 1, "sel": true }], "out": [{ "t": "err", "s": "org.opentest4j.AssertionFailedError: …" }, "обычная строка"], "summary": "необязательно" }` — результаты запуска тестов JUnit (итог «Tests failed: 1, passed: 1 of 2 tests» посчитается сам). Цели: `tests test-tree test-N test-summary test-out link-N`.
- `menu: { "at": "цель", "items": ["New ›", "-", "Copy"], "hl": "New", "sub": { "items": [...], "hl": "Java Class" } }` — контекстное меню (`-` — разделитель, ` ›` — подменю, `"Run\tCtrl+Shift+F10"` — через `\t` сочетание клавиш серым справа; `hl` — текст пункта без сочетания); `popup: { "title", "input", "list", "hl" }`; `dialog: { "title", "side": [...], "sideHl", "rows": [{ "label", "value", "type": "input|select|text", "hl": true }], "buttons", "primary", "note" }`; `welcome: "Open"` — стартовый экран IDEA.
- `weak: [{line, text, msg, tip}]` — слабое предупреждение (серый фон, точечное подчёркивание: «можно заменить на лямбду», «можно упростить»). Строка состояния: цели `status-path`, `status-pos`, `status-enc` (UTF-8). Путь в панели Build берётся из `statusPath` (по умолчанию module-01) — в других модулях задавай `statusPath: "module-13"`.
- `firstLine: 1165` — фрагмент файла: номера строк начинаются с этого числа (цели и `line` в errors всё равно считаются от 1); `codeHeight: 260` — ограничить высоту кода прокруткой; `popup.icon: "ⓜ"` — значок у пунктов popup (по умолчанию Ⓒ — класс).
- `selLines: [7, 8]` — выделенные строки (синий фон, как при выделении мышью); `gutterIcons: { "7": "⬆", "12": "⬇" }` — значок на полях (⬆ переопределяет метод родителя, ⬇ — есть наследники/реализации), цель `gicon-N`; `menu.title` — заголовок меню («Surround With», «Generate»).
- `banner: { "text": "Project JDK is not defined", "action": "Setup SDK" }` — жёлтая плашка над редактором (цели `banner`, `banner-action`). В `dialog` пункты левого списка — цели `side-<текст>`. Меню рисуется поверх диалога (выпадающий список в диалоге = `menu` с `at: "field-<label>"`).
- `runConfig: "Main"` — конфигурация запуска сверху; `project`, `status`, `statusPath` — строка состояния.
- **`marks`** — выноски: `[{ "n": 1, "target": "цель", "text": "HTML-пояснение", "side": "left|top|bottom", "box": true }]`. Номер ставится у цели, под окном — легенда с текстами. 2–5 выносок на скрин, каждая объясняет, **на что смотреть и что это значит**. `side: "top"` — когда номер справа закрыл бы код.
  Цели: `run-btn debug-btn config project-btn run-tool tree tree-<имя> editor tab problems line-N gutter-N err-N tip bp-N console output output-2 … error error-2 … link-1 … exit cmd input stop build build-result build-file build-err-N build-out debug frames frame-N vars var-<имя> step-over step-into resume menu submenu popup dialog field-<label> btn-<кнопка> welcome-<New Project|Open|Clone Repository>`.
- `caption` — подпись под скрином.

**`div.cheat` — «📝 Выпиши себе».** В каждом модуле, где появляются новые горячие клавиши или команды (IDEA, отладчик, Git, Maven), — блок-шпаргалка в начале урока, с прямым призывом выписать руками: `<div class="cheat"><h3>Выпиши себе: …</h3><p>…</p><div class="table-wrap"><table><tr><th>Что нажать</th><th>Что делает</th><th>Когда</th></tr>…</table></div></div>`. Клавиши — `<kbd>`. Для Git — команда, что делает и зачем (push, pull, commit, rebase…). Образец — урок 1.4.

**Мини-игры не на каждом шагу**: игра/песочница только если показывает механизм, который иначе не увидеть. Не больше одного «игрового» виджета на урок; `check` и `trace` — не игры, их можно больше.

## Содержание и стиль
- На «ты», дружелюбно, для абсолютного новичка. Каждое новое слово объяснять при первом появлении. 1–2 аналогии из жизни на сложную идею (разные: кухня, транспорт, игры, почта, склад). Без воды: короткие абзацы.
- Каждый урок: зачем это нужно (пример из жизни/работы, в т.ч. из автотестов, где уместно) → идея + аналогия → код → схема → «предскажи» → проверка.
- Глубина: «хорошо и местами глубоко», но без лишних узких тем (см. цель в `AUTHORING.md`). Тонкости, которые спрашивают на собеседованиях, — обязательно (в головоломках и «для любопытных»).
- Терминал не использовать (решение автора курса). Всё — в IntelliJ IDEA. Ввод с клавиатуры — через консоль Run в IDEA.
- «Мастерская алгоритмов» (с модуля 4): урок-мастерская по шаблону из `AUTHORING.md` (понять вход/выход → руками на примерах → роли переменных → псевдокод → код → трассировка `trace` с `ask`).
- Не забегать вперёд: в задачах и головоломках — только пройденное к этому модулю (список модулей — `site/js/course.js`); изредка «★★★ на опережение» с пометкой.

## Задачи (`tasks/mXX/*.task`)
- 6–10 задач по нарастающей (★ → ★★★). Минимум одна с вводом (`@@ input`) начиная с модуля 2, минимум одна «Мастерская» (логика) начиная с модуля 4, одна «почини код» (заготовка в `starter/module-XX/`) — по желанию.
- **Имена классов уникальны во всём курсе** (у ученика все папки — Sources Root в одном проекте IDEA). Проверь: `grep -rh "## class:" tasks/` — не повторяй.
- Путь файла: `## file: module-XX/ИмяКласса.java`, ссылка на урок в описании.
- Несколько тестов на задачу с разными входами, включая граничные (0, пустая строка, отрицательные…).
- Режим JUnit (только модули 17–18): `## mode: junit` — ученик пишет тесты, сервер запускает JUnit 5 + AssertJ.
- Задача без тестов (`@@ test` нет) = проверяет наставник (статус «у наставника»), использовать только когда автопроверка невозможна.

### Обязательно проверь каждую задачу на сервере
```bash
mkdir -p workspace/module-XX
cp tutor/solutions/module-XX/Имя.java workspace/module-XX/
curl -s -X POST http://localhost:7070/api/check/mXX-tNN      # должно быть "status":"passed"
# потом испорти решение (неверный вывод) и убедись, что "failed"
rm workspace/module-XX/Имя.java                              # обязательно убери эталон из workspace!
```
В конце в `workspace/module-XX/` не должно остаться эталонов (заготовки из `starter/` сервер подложит сам).

## План экзамена `tutor/module-XX.md`
Как `tutor/module-01.md`: критерии зачёта; цели модуля О1…Он с вопросами «вопрос → **суть ответа** → типичные заблуждения»; банк головоломок 20+ (что выведется / скомпилируется ли / найди ошибку / почему ошибка / что при запуске); задачи «Мастерской» (если модуль ≥ 4); доп. задачи для быстрых.

## Самопроверка перед сдачей
1. Все JSON в `<script type="application/json">` валидны: `node -e` с `JSON.parse` по каждому блоку.
2. Каждая страница отдаётся сервером: `curl -s -o /dev/null -w "%{http_code}" http://localhost:7070/lessons/mXX/….html` → 200.
3. Все задачи: passed на эталоне, failed на испорченном; workspace очищен от эталонов.
4. `_manifest.json` валиден, id уроков совпадают с `data-lesson` в файлах.
5. В SVG нет жёстко заданных цветов (кроме emoji), классы `d-*`.

## Отчёт (последнее сообщение)
Коротко: список файлов; темы уроков; какие виджеты где; задачи (id, класс, что проверяют, результат passed/failed-проверки); что не получилось или какой виджет/CSS хотелось бы добавить.
