#!/usr/bin/env node
// Режим курса «без подписки»: для каждой фразы урока про наставника / Claude Code / /check /hint /exam
// ставит рядом явный вариант для бесплатного ИИ:
//   <p><span class="ai-only">исходный текст</span><span class="free-only">вариант без подписки</span></p>
// Имя ИИ (DeepSeek / ChatGPT) — <span class="ai-name">DeepSeek</span>, его подставляет app.js.
// Запуск: node tools/free-variants.js [--dry]   (--dry — только напечатать варианты, ничего не писать)
// Повторный запуск безопасен: уже размеченные фразы пропускаются. Неудачный автовариант — в OVERRIDES.
const fs = require("fs");
const path = require("path");

const ROOT = path.join(__dirname, "../site/lessons");
const DRY = process.argv.includes("--dry");
const AI = '<span class="ai-name">DeepSeek</span>';
const CHECK = "промпт «Проверь решение» (кнопка внизу страницы)";
const EXAM = "промпт «Экзамен» (кнопка внизу страницы)";
const SKIP_FILES = new Set(["m00/01-how-it-works.html"]); // урок про устройство курса: там своя плашка для режима без подписки
const TRIGGER = /\/check|\/hint|\/exam|\/start|Claude Code|[Нн]аставник/;

// Ручные варианты: начало исходного текста (без тегов, 60 символов) → HTML для режима без подписки
const OVERRIDES = require("./free-overrides.js");

const plain = (h) => h.replace(/<[^>]+>/g, "").replace(/\s+/g, " ").trim();

function rewrite(h) {
  let s = h;
  const cap = (m, w) => (m[0] === m[0].toUpperCase() ? w[0].toUpperCase() : w[0]) + w.slice(1);
  // экзамен в Claude Code → карточка с промптом «Экзамен»
  s = s.replace(/<strong>Открой наставника:<\/strong>[^<]*Напиши <code>\/exam<\/code>\./g, `<strong>Открой ${AI}</strong> (лучше новый чат) и вставь туда ${EXAM}.`);
  s = s.replace(/<(b|strong)>Открой (Claude Code|наставника)( в папке курса)?<\/\1>( в папке курса)?( и)? напиши <code>\/exam<\/code>/g, (m, t) => `<${t}>Открой ${AI}</${t}> (лучше новый чат) и вставь туда ${EXAM}`);
  s = s.replace(/[Оо]ткрой (Claude Code|наставника)( в папке курса)?( и)? напиши <code>\/exam<\/code>/g, (m) => cap(m, `скопируй ${EXAM} и вставь в ${AI}`));
  s = s.replace(/[Нн]апиши (наставнику )?<code>\/exam<\/code>/g, (m) => cap(m, `пройди мини-экзамен у ${AI}: ${EXAM}`));
  s = s.replace(/<code>\/exam<\/code>/g, EXAM);
  // разбор решений: цепочки «/check m01-t01, /check m01-t02 и так далее»
  s = s.replace(/с наставником:\s*<code>\/check[^<]*<\/code>((,|\s…|\s\.\.\.)\s*<code>\/check[^<]*<\/code>)*(\s*и так далее)?/g, `с ${AI}: ${CHECK}`);
  s = s.replace(/через <code>\/check[^<]*<\/code>/g, `с ${AI}: ${CHECK}`);
  s = s.replace(/потом <code>\/check[^<]*<\/code> у наставника/g, `потом разбери решение с ${AI}: ${CHECK}`);
  s = s.replace(/[Нн]апиши (в Claude Code:? )?<code>\/check[^<]*<\/code>/g, (m) => cap(m, `разбери решение с ${AI}: ${CHECK}`));
  // подсказки
  s = s.replace(/<code>\/hint[^<]*<\/code>\s*наставнику/g, `подсказку у ${AI}`);
  s = s.replace(/[Нн]апиши (в Claude Code )?<code>\/hint[^<]*<\/code>/g, (m) => cap(m, `попроси подсказку у ${AI}`));
  // «засчитан, когда наставник отметит в progress.md» — в режиме без подписки progress.md не ведётся
  s = s.replace(/когда [^.]*?(<code>)?progress\.md(<\/code>)?[^.]*\.(\s*Можешь сам открыть[^.]*\.)?/g, `когда все задачи зелёные и мини-экзамен у ${AI} пройден.`);
  s = s.replace(/,? (но )?без отметок в (<code>)?progress\.md(<\/code>)?( — просто потренироваться)?/g, "");
  s = s.replace(/<code>\/hint[^<]*<\/code>/g, `подсказку у ${AI}`);
  s = s.replace(/<code>\/check[^<]*<\/code>/g, CHECK);
  s = s.replace(/<code>\/start<\/code>/g, "новый чат");
  s = s.replace(/спроси наставника в Claude Code или любой бесплатный ИИ \(например, <a[^>]*>DeepSeek<\/a>\)/g, `спроси ${AI}`);
  s = s.replace(/[Нн]апиши в Claude Code:/g, (m) => (m[0] === "Н" ? "С" : "с") + `проси ${AI}:`);
  s = s.replace(/в Claude Code/g, `в ${AI}`).replace(/Claude Code/g, AI);
  s = s.replace(/[Нн]аставник(ами|ам|ом|ов|а|у|е|и)?(?![а-яё])/g, AI);
  return s;
}

let total = 0, changed = 0;
const report = [];
for (const d of fs.readdirSync(ROOT).sort()) {
  for (const f of fs.readdirSync(path.join(ROOT, d)).filter((x) => x.endsWith(".html")).sort()) {
    const rel = d + "/" + f;
    if (SKIP_FILES.has(rel)) continue;
    const file = path.join(ROOT, d, f);
    const src = fs.readFileSync(file, "utf8");
    let out = "", last = 0;
    const re = /<(p|li|td|h2|h3|h4|figcaption|summary)(\s[^>]*)?>([\s\S]*?)<\/\1>/g;
    let m;
    while ((m = re.exec(src))) {
      const [all, tag, attrs = "", inner] = m;
      const before = src.slice(0, m.index);
      if (before.lastIndexOf("<script") > before.lastIndexOf("</script>")) continue;           // JSON виджетов
      if (!TRIGGER.test(inner) || /<(p|li|ul|ol|div)[\s>]/.test(inner)) continue;
      if (/ai-only|free-only/.test(attrs) || /class="(ai|free)-only"/.test(inner)) continue;     // уже размечено
      const row = before.lastIndexOf("<tr"), rowEnd = before.lastIndexOf("</tr>");          // строка таблицы только для одного режима
      if (row > rowEnd && /^<tr class="(ai|free)-only"/.test(before.slice(row))) continue;
      // внутри «callout ai» — этот блок целиком превращается в «Спроси DeepSeek»
      const opens = [...before.matchAll(/<div class="callout( [^"]*)?"/g)];
      const lastOpen = opens.length ? opens[opens.length - 1] : null;
      if (lastOpen && /(^| )ai( |$)/.test((lastOpen[1] || "").trim()) && before.indexOf("</div>", lastOpen.index) === -1) continue;
      const key = plain(inner).slice(0, 60);
      const free = OVERRIDES[key] !== undefined ? OVERRIDES[key] : rewrite(inner);
      total++;
      report.push(`${rel}\n  AI:   ${plain(inner)}\n  FREE: ${plain(free)}`);
      out += src.slice(last, m.index) + `<${tag}${attrs}><span class="ai-only">${inner}</span><span class="free-only">${free}</span></${tag}>`;
      last = m.index + all.length;
    }
    if (last) {
      out += src.slice(last);
      if (!DRY) fs.writeFileSync(file, out);
      changed++;
    }
  }
}
console.log(report.join("\n"));
console.log(`\n${total} фраз в ${changed} файлах${DRY ? " (dry run, ничего не записано)" : ""}`);
