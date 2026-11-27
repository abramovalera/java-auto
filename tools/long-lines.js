// Ищет слишком длинные строки кода в уроках: node tools/long-lines.js [mXX ...]
// <pre> с кодом — не длиннее 80 символов, код в виджетах trace / check (JSON-поле "code") — не длиннее 72:
// иначе на обычном ноутбуке строка уходит под горизонтальную прокрутку.
const fs = require("fs"), path = require("path");
const root = path.resolve(__dirname, "..", "site", "lessons");
const only = process.argv.slice(2);
const PRE = 80, WIDGET = 72;
const unesc = (s) => s.replace(/<[^>]+>/g, "").replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&amp;/g, "&").replace(/&quot;/g, '"');
let total = 0;
for (const mod of fs.readdirSync(root).filter((d) => /^m\d\d$/.test(d) && (!only.length || only.includes(d))).sort()) {
  for (const f of fs.readdirSync(path.join(root, mod)).filter((x) => x.endsWith(".html")).sort()) {
    const html = fs.readFileSync(path.join(root, mod, f), "utf8");
    const out = [];
    for (const m of html.matchAll(/<pre(?![^>]*class="terminal")[^>]*>([\s\S]*?)<\/pre>/g)) {
      unesc(m[1]).split("\n").forEach((l) => { if (l.length > PRE) out.push(`  pre ${l.length}: ${l.trim().slice(0, 90)}`); });
    }
    for (const m of html.matchAll(/<div data-widget="(trace|check)"[^>]*>\s*<script type="application\/json">([\s\S]*?)<\/script>/g)) {
      let data; try { data = JSON.parse(m[2]); } catch (e) { continue; }
      const codes = [];
      (function walk(o) { if (o && typeof o === "object") for (const [k, v] of Object.entries(o)) { if (k === "code" && typeof v === "string") codes.push(v); else walk(v); } })(data);
      // строки stack trace не код — в виджете они переносятся, их не считаем
      codes.forEach((c) => c.split("\n").forEach((l) => { if (l.length > WIDGET && !/^\s*(at |Exception in thread|Caused by)/.test(l)) out.push(`  ${m[1]} ${l.length}: ${l.trim().slice(0, 90)}`); }));
    }
    if (out.length) { console.log(`${mod}/${f}`); out.forEach((x) => console.log(x)); total += out.length; }
  }
}
console.log(`\nДлинных строк: ${total}`);
process.exitCode = total ? 1 : 0;
