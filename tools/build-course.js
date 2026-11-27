// Собирает site/js/course.js из манифестов модулей site/lessons/mXX/_manifest.json.
// Запуск из корня курса: node tools/build-course.js
// Модуль без манифеста показывается как «скоро». Проверяет: файл урока есть, data-lesson совпадает, id не повторяются.
const fs = require("fs"), path = require("path");
const root = path.resolve(__dirname, "..");
const BASE = [
  ["m00", "0", "Вводная", "Как устроен курс, как учиться вместе с ИИ-наставником и что такое программирование."],
  ["m01", "1", "Знакомство с Java", "Что такое Java, JVM и байт-код, IntelliJ IDEA, первая программа, вывод и ошибки."],
  ["m02", "2", "Переменные и типы данных", "Где программа хранит данные: int, double, boolean, char, String. Стек и куча. Ввод через Scanner."],
  ["m03", "3", "Операторы и выражения", "Арифметика, остаток, ++, сравнения, логика И/ИЛИ/НЕ, приоритет, Math."],
  ["m04", "4", "Условия: if и switch", "Учим программу принимать решения. Игра «Пилот»: корабль на твоём коде."],
  ["m05", "5", "Циклы", "for, while, do-while, break и continue. Мастерская: роли переменных, цифры числа."],
  ["m06", "6", "Массивы", "Много значений в одной переменной, обход, поиск, разворот, двумерные массивы."],
  ["m07", "7", "Методы", "Параметры, return, перегрузка, стек вызовов, рекурсия."],
  ["m08", "8", "Строки", "String и его методы, equals против ==, StringBuilder, задачи на строки."],
  ["m09", "9", "Классы и объекты", "Поля, конструкторы, static, пакеты. Что под капотом у System.out.println."],
  ["m10", "10", "ООП: три кита", "Инкапсуляция, наследование, полиморфизм. Object, equals и hashCode."],
  ["m11", "11", "Интерфейсы, абстрактные классы, enum, record", "Контракты и шаблоны поведения, перечисления и классы-данные."],
  ["m12", "12", "Исключения", "try-catch-finally, чтение stack trace, свои исключения."],
  ["m13", "13", "Коллекции и generics", "ArrayList, HashMap, HashSet, обобщённые типы, сортировка."],
  ["m14", "14", "Лямбды и Stream API", "Лямбды, ссылки на методы, filter/map/collect, Optional."],
  ["m15", "15", "Файлы и ввод-вывод", "Читаем и пишем файлы, try-with-resources, папки."],
  ["m16", "16", "Многопоточность: основы", "Потоки, синхронизация, ExecutorService — на уровне собеседования."],
  ["m17", "17", "Инструменты разработчика", "Git, Gradle, отладчик, тесты на JUnit 5."],
  ["m18", "18", "Мост в автотесты", "AssertJ, Page Object, локаторы, первые UI-тесты на Selenide. Итоговый проект."]
];
const problems = [], seenIds = new Set();
// "hidden": true в манифесте модуля или у урока — спрятать из курса, не удаляя файлы (например, модуль 18 про автотесты:
// он отложен до отдельного курса). Спрятанное не попадает в course.js, карту курса, шпаргалку и счётчики.
const modules = BASE.map(([id, num, title, summary]) => {
  const mf = path.join(root, "site", "lessons", id, "_manifest.json");
  if (!fs.existsSync(mf)) return { id, num, title, status: "soon", summary };
  let m;
  try { m = JSON.parse(fs.readFileSync(mf, "utf8")); } catch (e) { problems.push(`${id}: _manifest.json не читается: ${e.message}`); return { id, num, title, status: "soon", summary }; }
  if (m.hidden) return null;
  m.lessons = m.lessons.filter((l) => !l.hidden);
  for (const l of m.lessons) {
    if (seenIds.has(l.id)) problems.push(`${id}: повтор id урока ${l.id}`);
    seenIds.add(l.id);
    const file = path.join(root, "site", l.url);
    if (!fs.existsSync(file)) { problems.push(`${id}: нет файла ${l.url}`); continue; }
    const html = fs.readFileSync(file, "utf8");
    const dl = (html.match(/data-lesson="([^"]+)"/) || [])[1];
    if (dl !== l.id) problems.push(`${id}: ${l.url} — data-lesson="${dl}", а в манифесте ${l.id}`);
  }
  const tdir = path.join(root, "tasks", id);
  const taskFiles = fs.existsSync(tdir) ? fs.readdirSync(tdir).filter((f) => f.endsWith(".task")).length : 0;
  if (m.taskCount !== undefined && m.taskCount !== taskFiles) problems.push(`${id}: taskCount ${m.taskCount}, а файлов задач ${taskFiles} — беру ${taskFiles}`);
  const out = { id, num: m.num || num, title: m.title || title, status: "open", summary: m.summary || summary, lessons: m.lessons };
  if (taskFiles) { out.tasks = `/tasks.html?module=${id}`; out.taskCount = taskFiles; }
  return out;
}).filter(Boolean);
// «Тренинг код»: темы — папки tasks/tr-<тема> с файлом _topic.json { title, after: "m03", order, summary, icon }
const training = fs.readdirSync(path.join(root, "tasks")).filter((d) => /^tr-[a-z]+$/.test(d)).map((id) => {
  const dir = path.join(root, "tasks", id);
  const meta = fs.existsSync(path.join(dir, "_topic.json")) ? JSON.parse(fs.readFileSync(path.join(dir, "_topic.json"), "utf8")) : {};
  if (!meta.title) problems.push(`${id}: нет _topic.json с title`);
  const files = fs.readdirSync(dir).filter((f) => f.endsWith(".task"));
  const levels = { 1: 0, 2: 0, 3: 0 };
  for (const f of files) { const lv = (fs.readFileSync(path.join(dir, f), "utf8").match(/^## level:\s*(\d)/m) || [])[1] || "1"; levels[lv] = (levels[lv] || 0) + 1; }
  return { id, folder: "trainer/" + id.slice(3), title: meta.title || id, icon: meta.icon || "🏋️", after: meta.after || null, order: meta.order || 99, summary: meta.summary || "", taskCount: files.length, levels };
}).sort((a, b) => a.order - b.order);
const js = `// АВТОГЕНЕРАЦИЯ: не правь руками — запусти node tools/build-course.js (источник — site/lessons/mXX/_manifest.json).
// Карта курса: модули и уроки. Сайдбар, главная и навигация «назад/вперёд» строятся отсюда.
window.COURSE = ${JSON.stringify({ title: "Java с нуля", modules }, null, 2)};
// «Тренинг код» — отдельный раздел с задачами по темам (tasks/tr-*), страница /training.html
window.TRAINING = ${JSON.stringify(training, null, 2)};
`;
fs.writeFileSync(path.join(root, "site", "js", "course.js"), js);
const open = modules.filter((m) => m.status === "open");
console.log(`Открыто модулей: ${open.length} из ${modules.length}; уроков: ${open.reduce((s, m) => s + m.lessons.length, 0)}; задач: ${open.reduce((s, m) => s + (m.taskCount || 0), 0)}`);
if (problems.length) { console.log("Проблемы:\n- " + problems.join("\n- ")); process.exitCode = 1; }
// шпаргалка берёт названия модулей и номера уроков из course.js — пересобираем её сразу
require("child_process").execFileSync(process.execPath, [path.join(__dirname, "build-cheatsheet.js")], { stdio: "inherit" });
