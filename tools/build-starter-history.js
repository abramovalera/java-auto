// История версий заготовок: для каждого файла starter/module-XX/… — отпечатки всех его прошлых версий из Git.
// Сервер по ним узнаёт «нетронутую» заготовку у ученика и тихо заменяет её на свежую.
// Запуск: node tools/build-starter-history.js (его сам вызывает tools/build-course.js). Пишет starter/_meta/versions.json.
const fs = require("fs"), path = require("path"), crypto = require("crypto"), { execSync } = require("child_process");
const root = path.resolve(__dirname, "..");
const starterDir = path.join(root, "starter");
// отпечаток не зависит от переводов строк (Windows/Linux) и пробелов в конце строк
const fingerprint = (text) => crypto.createHash("sha256").update(text.replace(/\r/g, "").split("\n").map((l) => l.replace(/\s+$/, "")).join("\n").trim()).digest("hex");
const git = (args) => execSync("git " + args, { cwd: root, stdio: ["ignore", "pipe", "ignore"], maxBuffer: 64 * 1024 * 1024 }).toString();

const files = [];
const walk = (d) => { for (const e of fs.readdirSync(d, { withFileTypes: true })) { const p = path.join(d, e.name); if (e.isDirectory()) { if (!["_meta", "system"].includes(e.name)) walk(p); } else if (p.endsWith(".java")) files.push(p); } };
walk(starterDir);

const out = {};
let inGit = true;
for (const f of files) {
  const rel = path.relative(starterDir, f).split(path.sep).join("/");
  const hashes = new Set([fingerprint(fs.readFileSync(f, "utf8"))]);
  try {
    const repoPath = "starter/" + rel;
    for (const sha of git(`log --format=%H --follow -- "${repoPath}"`).split("\n").filter(Boolean)) {
      // файл мог называться иначе в старых коммитах (--follow) — берём путь из самого коммита
      const names = git(`show --name-only --format= ${sha}`).split("\n").filter((n) => n.startsWith("starter/") && n.endsWith(path.basename(f)));
      for (const n of names.length ? names : [repoPath]) {
        try { hashes.add(fingerprint(git(`show ${sha}:"${n}"`))); } catch (e) { /* в этом коммите файл удалён */ }
      }
    }
  } catch (e) { inGit = false; }
  out[rel] = [...hashes].sort();
}
fs.mkdirSync(path.join(starterDir, "_meta"), { recursive: true });
fs.writeFileSync(path.join(starterDir, "_meta", "versions.json"), JSON.stringify(out, null, 1) + "\n");
console.log(`starter/_meta/versions.json: ${files.length} заготовок, ${Object.values(out).reduce((n, h) => n + h.length, 0)} версий${inGit ? "" : " (Git недоступен — только текущие версии)"}`);
