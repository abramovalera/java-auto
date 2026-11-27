// Прогоняет все задачи курса на эталонных решениях: node tools/check-tasks.js [mXX ...]
// Для каждой задачи: эталон tutor/solutions/module-XX/<Класс>.java копируется в workspace, сервер проверяет,
// ожидается "passed". Файлы ученика при этом не портятся: если в workspace уже был файл — он сохраняется и возвращается.
const fs = require("fs"), path = require("path");
const root = path.resolve(__dirname, "..");
const only = process.argv.slice(2);
const tasksDir = path.join(root, "tasks");
const results = [];

function meta(text, key) {
  const m = text.match(new RegExp(`^## ${key}:\\s*(.+)$`, "m"));
  return m ? m[1].trim() : null;
}

let pending = null;
// Прогон эталонов не должен попадать в журнал проверок ученика: сервер пишет строки без профиля,
// а такие строки достаются самому старому профилю. В конце убираем всё, что дописано за время прогона.
const historyLog = path.join(root, "workspace", ".history.log");
// Убираем только строки без профиля по задачам этого прогона, которых не было до старта:
// так параллельные прогоны и проверки ученика (у них есть profile=) не затирают друг друга.
const historyBefore = new Set(fs.existsSync(historyLog) ? fs.readFileSync(historyLog, "utf8").split("\n") : []);
const checkedIds = new Set();
process.on("exit", () => {
  if (pending) pending();
  try {
    if (!fs.existsSync(historyLog)) return;
    const kept = fs.readFileSync(historyLog, "utf8").split("\n").filter((line) => {
      if (historyBefore.has(line) || /profile=/.test(line)) return true;
      const id = (line.split(" | ")[1] || "").trim();
      return !checkedIds.has(id);
    });
    fs.writeFileSync(historyLog, kept.join("\n"));
  } catch (e) { /* журнал не критичен */ }
});
for (const sig of ["SIGINT", "SIGTERM", "SIGHUP"]) process.on(sig, () => { if (pending) pending(); process.exit(130); });

(async () => {
  const mods = fs.readdirSync(tasksDir).filter((d) => /^(m\d\d|tr-[a-z]+)$/.test(d) && (!only.length || only.includes(d))).sort();
  for (const mod of mods) {
    for (const f of fs.readdirSync(path.join(tasksDir, mod)).filter((x) => x.endsWith(".task")).sort()) {
      const text = fs.readFileSync(path.join(tasksDir, mod, f), "utf8");
      const id = meta(text, "id"), file = meta(text, "file"), cls = meta(text, "class");
      // ★-задания сквозного проекта: эталон — tutor/solutions/minibank/<модуль>/, проверяемый файл — в папке MiniBank
      const isProject = /^(yes|true)$/i.test(meta(text, "project") || "");
      const solution = isProject
        ? path.join(root, "tutor", "solutions", "minibank", mod, path.basename(file))
        : path.join(root, "tutor", "solutions", path.dirname(file), path.basename(file));
      if (!fs.existsSync(solution)) { results.push([id, "НЕТ ЭТАЛОНА", file]); continue; }
      const target = isProject ? path.join(root, "MiniBank", file) : path.join(root, "workspace", file);
      const backup = fs.existsSync(target) ? fs.readFileSync(target) : null;
      fs.mkdirSync(path.dirname(target), { recursive: true });
      fs.copyFileSync(solution, target);
      // если скрипт прервут посреди проверки — всё равно вернуть файл ученика / убрать эталон
      pending = () => { try { if (backup) fs.writeFileSync(target, backup); else fs.unlinkSync(target); } catch (e) {} };
      let status;
      checkedIds.add(id);
      try {
        const r = await fetch(`http://localhost:7070/api/check/${id}`, { method: "POST" }).then((x) => x.json());
        status = r.status + (r.status !== "passed" ? " " + JSON.stringify((r.tests || []).filter((t) => !t.passed).map((t) => t.name + ": " + (t.error || t.diff || "")).concat(r.ruleFails || []).concat(r.compilerOutput ? [r.compilerOutput.slice(0, 200)] : [])) : "");
      } catch (e) { status = "ошибка запроса: " + e.message; }
      pending(); pending = null;
      results.push([id, status, cls]);
    }
  }
  const bad = results.filter((r) => !String(r[1]).startsWith("passed"));
  results.forEach((r) => console.log((String(r[1]).startsWith("passed") ? "✓ " : "✗ ") + r.join("  ")));
  console.log(`\nИтого: ${results.length - bad.length} из ${results.length} задач проходят на эталоне.`);
  // повторяющиеся имена классов (у ученика все модули — в одном проекте IDEA)
  const classes = {};
  for (const mod of mods) for (const f of fs.readdirSync(path.join(tasksDir, mod)).filter((x) => x.endsWith(".task"))) {
    const t = fs.readFileSync(path.join(tasksDir, mod, f), "utf8"), c = meta(t, "class"), file = meta(t, "file");
    (classes[c] = classes[c] || new Set()).add(file);
  }
  const dup = Object.entries(classes).filter(([, s]) => s.size > 1);
  if (dup.length) console.log("Повторяются классы в разных файлах: " + dup.map(([c, s]) => `${c} (${[...s].join(", ")})`).join("; "));
  // утечка ответов: в workspace не должно лежать файлов, совпадающих с эталоном
  const leaks = [];
  const solRoot = path.join(root, "tutor", "solutions");
  // эталоны лежат на разной глубине: module-XX/Класс.java, trainer/<тема>/Класс.java, minibank/<модуль>/Main.java
  const walk = (dir) => fs.readdirSync(dir, { withFileTypes: true }).flatMap((e) => e.isDirectory() ? walk(path.join(dir, e.name)) : [path.join(dir, e.name)]);
  for (const sol of fs.existsSync(solRoot) ? walk(solRoot) : []) {
    const rel = path.relative(solRoot, sol);
    if (rel.startsWith("minibank")) continue; // эталоны МиниБанка сравниваются с папкой MiniBank, а не с workspace
    const w = path.join(root, "workspace", rel);
    if (fs.existsSync(w) && fs.statSync(w).isFile() && fs.readFileSync(w).equals(fs.readFileSync(sol))) leaks.push("workspace/" + rel.split(path.sep).join("/"));
  }
  if (leaks.length) console.log("⚠ В workspace лежат копии эталонов (убери их!): " + leaks.join(", "));
  process.exitCode = bad.length ? 1 : 0;
})();
