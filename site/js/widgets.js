// Мини-игры и тренажёры. Подключаются автоматически из app.js, если на странице есть [data-widget].
// Разметка в уроке: <div data-widget="имя" id="уникальный-id"> + данные в <script type="application/json"> или "text/plain".
(function () {
  "use strict";
  const JC = window.JC, W = JC.widgets, esc = JC.esc;
  const sleep = (ms) => new Promise((r) => setTimeout(r, ms));
  const shuffle = (a) => { a = a.slice(); for (let i = a.length - 1; i > 0; i--) { const j = Math.floor(Math.random() * (i + 1)); [a[i], a[j]] = [a[j], a[i]]; } return a; };

  function readData(el, type) {
    const s = el.querySelector(`script[type="${type || "application/json"}"]`);
    if (!s) return null;
    return type === "text/plain" ? s.textContent : JSON.parse(s.textContent);
  }

  // Общая рамка: заголовок, цели (галочки), тело. Все цели выполнены → XP и конфетти.
  function frame(el, o) {
    el.classList.add("widget");
    el.innerHTML = `
      <div class="w-head"><div class="w-icon">${o.icon}</div>
        <div><div class="w-kind">${esc(o.kind || "Мини-игра")}</div><div class="w-title">${esc(o.title)}</div></div></div>
      ${o.goals ? `<ul class="w-goals">${o.goals.map((g) => `<li>${esc(g)}</li>`).join("")}</ul>` : ""}
      <div class="w-body"></div>`;
    const body = el.querySelector(".w-body");
    const goalEls = [...el.querySelectorAll(".w-goals li")];
    // ключ не зависит от имени файла урока: id урока + id виджета (переименование файла прогресс не сломает)
    const key = "w:" + (JC.lessonId || location.pathname) + ":" + (el.id || o.title);
    const done = new Set(JC.store.get(key, []));
    done.forEach((i) => goalEls[i] && goalEls[i].classList.add("ok"));
    if (goalEls.length && done.size >= goalEls.length) el.classList.add("complete");
    function goal(i) {
      if (done.has(i) || !goalEls[i]) return;
      done.add(i); JC.store.set(key, [...done]);
      goalEls[i].classList.add("ok");
      goalEls[i].animate([{ transform: "scale(1.15)" }, { transform: "scale(1)" }], { duration: 300 });
      if (done.size === goalEls.length) {
        el.classList.add("complete");
        JC.award(key, o.xp || 20, "Пройдено: " + o.title);
        JC.confetti(45);
      }
    }
    return { body, goal };
  }

  // Кот в SVG — герой примеров про объекты и память
  function cat(color) {
    const dark = color === "#3a3d4d";
    const eye = dark ? "#ffd866" : "#1e2033";
    const line = dark ? "#9aa0b4" : "#1e2033";
    return `<svg viewBox="0 0 80 80" aria-hidden="true">
      <path d="M62 64q16-2 11-22" stroke="${color}" stroke-width="7" fill="none" stroke-linecap="round"/>
      <ellipse cx="40" cy="60" rx="21" ry="15" fill="${color}" stroke="${line}" stroke-opacity=".25" stroke-width="2"/>
      <path d="M19 30 22 9 35 23Z" fill="${color}" stroke="${line}" stroke-opacity=".25" stroke-width="2"/>
      <path d="M61 30 58 9 45 23Z" fill="${color}" stroke="${line}" stroke-opacity=".25" stroke-width="2"/>
      <path d="M22.5 14 24 24 30 21Z M57.5 14 56 24 50 21Z" fill="#ff9fb0" opacity=".7"/>
      <circle cx="40" cy="35" r="19" fill="${color}" stroke="${line}" stroke-opacity=".25" stroke-width="2"/>
      <ellipse cx="33" cy="33" rx="3" ry="4.2" fill="${eye}"/><ellipse cx="47" cy="33" rx="3" ry="4.2" fill="${eye}"/>
      <circle cx="34" cy="31.6" r="1" fill="#fff"/><circle cx="48" cy="31.6" r="1" fill="#fff"/>
      <path d="M38 40h4l-2 2.6z" fill="#ff7f98"/>
      <path d="M40 42.6q-2 3-5 1.5M40 42.6q2 3 5 1.5" stroke="${line}" stroke-width="1.4" fill="none" stroke-linecap="round"/>
      <path d="M26 40h-9M26 43l-8 2M54 40h9M54 43l8 2" stroke="${line}" stroke-width="1.2" opacity=".5" stroke-linecap="round"/>
    </svg>`;
  }
  JC.cat = cat;
  const CAT_COLORS = [
    { n: "рыжий", c: "#ff9a3c" }, { n: "серый", c: "#9aa0b4" }, { n: "чёрный", c: "#3a3d4d" },
    { n: "белый", c: "#f3efe6" }, { n: "шоколадный", c: "#8a5a3c" }
  ];
  const CAT_NAMES = ["Мурзик", "Барсик", "Снежок", "Пушок", "Тиша", "Луна", "Кекс", "Симба", "Маркиз", "Соня", "Персик", "Боня"];

  // =====================================================================
  // 1. Машина времени: события по годам
  // =====================================================================
  W.timeline = function (el) {
    const data = readData(el);
    const items = data.items;
    const { body, goal } = frame(el, { icon: "🕰️", kind: "Машина времени", title: data.title, goals: ["Посмотри все события"], xp: 10 });
    body.innerHTML = `<div class="tl-card"></div>
      <div class="tl-track">${items.map((it, i) => `<button class="tl-dot" data-i="${i}" aria-label="${esc(it.year)}"><i></i><span>${esc(it.year)}</span></button>`).join("")}</div>
      <div class="tl-nav"><button class="btn ghost small prev">← Раньше</button><button class="btn small next">Позже →</button></div>`;
    const card = body.querySelector(".tl-card");
    const dots = [...body.querySelectorAll(".tl-dot")];
    const prev = body.querySelector(".prev"), next = body.querySelector(".next");
    const seen = new Set();
    let cur = 0;
    function show(i) {
      cur = i; seen.add(i);
      const it = items[i];
      card.innerHTML = `<div class="tl-emoji">${it.emoji}</div><div><div class="tl-year">${esc(it.year)}</div><div class="tl-title">${esc(it.title)}</div><p>${it.text}</p></div>`;
      card.animate([{ opacity: 0, transform: "translateX(24px)" }, { opacity: 1, transform: "none" }], { duration: 280, easing: "ease-out" });
      dots.forEach((d, j) => { d.classList.toggle("cur", j === i); d.classList.toggle("seen", seen.has(j)); });
      prev.disabled = i === 0; next.disabled = i === items.length - 1;
      if (seen.size === items.length) goal(0);
    }
    dots.forEach((d, i) => d.addEventListener("click", () => show(i)));
    prev.addEventListener("click", () => cur > 0 && show(cur - 1));
    next.addEventListener("click", () => cur < items.length - 1 && show(cur + 1));
    show(0);
  };

  // =====================================================================
  // 2. Сортировка карточек по корзинам («Java или не Java?»)
  // =====================================================================
  W.buckets = function (el) {
    const data = readData(el);
    const { body, goal } = frame(el, { icon: data.icon || "🗂️", kind: "Игра", title: data.title, goals: ["Разбери все карточки", "Ответь без единой ошибки"], xp: 20 });
    let order, i, score, answered;
    function start() { order = shuffle(data.items); i = 0; score = 0; render(); }
    function render() {
      if (i >= order.length) {
        const perfect = score === order.length;
        body.innerHTML = `<div class="bk-card ${perfect ? "right" : ""}"><div class="big">${perfect ? "🏆" : "👍"}</div>
          <div class="txt">Правильно ${score} из ${order.length}</div>
          <p style="margin:8px 0 0">${perfect ? "Идеально! Ни одной ошибки." : "Неплохо! Сыграй ещё раз, чтобы закрыть цель «без ошибок»."}</p></div>
          <div class="bk-buttons"><button class="btn ghost again">Сыграть ещё</button></div>`;
        body.querySelector(".again").addEventListener("click", start);
        goal(0); if (perfect) goal(1);
        return;
      }
      const it = order[i];
      answered = false;
      body.innerHTML = `<div class="bk-progress">Карточка ${i + 1} из ${order.length} · верно: ${score}</div>
        <div class="bk-card"><div class="big">${it.emoji}</div><div class="txt">${esc(it.text)}</div></div>
        <div class="bk-buttons">${data.buckets.map((b) => `<button class="btn ${b.style || "indigo"}" data-b="${esc(b.id)}">${esc(b.label)}</button>`).join("")}</div>
        <div class="w-msg" hidden></div>`;
      body.querySelectorAll("[data-b]").forEach((btn) => btn.addEventListener("click", () => answer(btn.dataset.b)));
    }
    function answer(b) {
      if (answered) return;
      answered = true;
      const it = order[i];
      const ok = b === it.bucket;
      if (ok) score++;
      body.querySelector(".bk-progress").textContent = `Карточка ${i + 1} из ${order.length} · верно: ${score}`;
      body.querySelector(".bk-card").classList.add(ok ? "right" : "wrong");
      const msg = body.querySelector(".w-msg");
      msg.hidden = false; msg.className = "w-msg " + (ok ? "ok" : "bad");
      msg.innerHTML = (ok ? "✓ Верно! " : "✗ Нет. ") + it.why;
      const btns = body.querySelector(".bk-buttons");
      btns.innerHTML = `<button class="btn green nx">Дальше →</button>`;
      btns.querySelector(".nx").addEventListener("click", () => { i++; render(); });
      btns.querySelector(".nx").focus();
    }
    start();
  };

  // =====================================================================
  // 3. Чертёж и объекты: класс Кот → new Кот(...)
  // =====================================================================
  W.blueprint = function (el) {
    const { body, goal } = frame(el, { icon: "📐", kind: "Песочница", title: "Чертёж и объекты: класс Кот", goals: ["Создай 3 кота по одному чертежу", "Вызови метод мяу() у кота"] });
    const MAX = 6;
    let cats = [], n = 0, color = 0;
    body.innerHTML = `<div class="bp">
      <div>
        <div class="bp-plan"><div class="c">// чертёж = класс</div><div class="t">class Кот {</div>
          <div>&nbsp;&nbsp;String имя;</div><div>&nbsp;&nbsp;String цвет;</div><div>&nbsp;&nbsp;void мяу() {…}</div><div class="t">}</div>
          <span class="stamp">ЧЕРТЁЖ</span></div>
        <div style="display:grid;gap:10px;margin-top:14px">
          <input class="w-input name" maxlength="12" placeholder="Имя кота" aria-label="Имя кота">
          <div class="w-row colors">${CAT_COLORS.map((c, i) => `<button class="chip ${i === 0 ? "sel" : ""}" data-c="${i}" title="${c.n}"><span style="width:16px;height:16px;border-radius:50%;background:${c.c};border:1px solid #0003;display:inline-block"></span>${c.n}</button>`).join("")}</div>
          <div class="w-row"><button class="btn indigo make">new Кот(…)</button><button class="btn ghost small clear">Очистить</button></div>
        </div>
      </div>
      <div><div class="bp-world"></div><div class="bp-code"><pre><code class="java-live"></code></pre></div></div>
    </div><div class="w-msg">Слева — чертёж. По нему пока ничего не построено. Введи имя, выбери цвет и нажми <b>new Кот(…)</b>.</div>`;
    const world = body.querySelector(".bp-world"), code = body.querySelector(".java-live"), msg = body.querySelector(".w-msg");
    const nameIn = body.querySelector(".name");
    const showCode = (s) => { code.innerHTML = JC.highlightJava(s); };
    body.querySelectorAll("[data-c]").forEach((b) => b.addEventListener("click", () => {
      color = +b.dataset.c; body.querySelectorAll("[data-c]").forEach((x) => x.classList.toggle("sel", x === b));
    }));
    function draw() {
      if (!cats.length) { world.innerHTML = `<div class="empty">Тут пока пусто.<br>Объектов нет — есть только чертёж.</div>`; return; }
      world.innerHTML = cats.map((c, i) => `<div class="obj" data-i="${i}" title="Нажми — кот замяукает">${cat(c.color)}<div class="name">${esc(c.name)}</div><div class="var">${c.v}</div></div>`).join("");
      world.querySelectorAll(".obj").forEach((o) => o.addEventListener("click", () => meow(+o.dataset.i, o)));
    }
    function meow(i, node) {
      const c = cats[i];
      node.querySelector(".speech")?.remove();
      const s = document.createElement("div"); s.className = "speech"; s.textContent = `Мяу! Я ${c.name}`;
      node.appendChild(s); setTimeout(() => s.remove(), 1600);
      showCode(`${c.v}.мяу();   // Мяу! Я ${c.name}`);
      msg.className = "w-msg ok";
      msg.innerHTML = `Ты вызвал <b>метод</b> <code>мяу()</code> у объекта <code>${c.v}</code>. Метод описан в чертеже один раз, а работает у каждого кота со своим именем.`;
      goal(1);
    }
    body.querySelector(".make").addEventListener("click", () => {
      if (cats.length >= MAX) {
        msg.className = "w-msg info"; msg.innerHTML = `Котов уже ${MAX} — хватит, а то память не резиновая 🙂 Нажми «Очистить».`; return;
      }
      let name = nameIn.value.replace(/[^A-Za-zА-Яа-яЁё0-9 -]/g, "").trim();
      if (!name) name = CAT_NAMES[Math.floor(Math.random() * CAT_NAMES.length)];
      name = name.charAt(0).toUpperCase() + name.slice(1);
      n++;
      const c = { name, color: CAT_COLORS[color].c, colorName: CAT_COLORS[color].n, v: "кот" + n };
      cats.push(c);
      nameIn.value = "";
      draw();
      showCode(`Кот ${c.v} = new Кот("${c.name}", "${c.colorName}");`);
      msg.className = "w-msg ok";
      msg.innerHTML = cats.length === 1
        ? `Готово! <code>new</code> построил по чертежу первый <b>объект</b>. Создай ещё парочку — с другими именами и цветами.`
        : cats.length === 3
          ? `Три разных кота, один чертёж. У каждого свои <b>данные</b> (имя, цвет), но устроены все одинаково. Это и есть суть ООП. Теперь нажми на любого кота.`
          : `Объектов: ${cats.length}. Чертёж при этом один и не изменился.`;
      if (cats.length >= 3) goal(0);
    });
    nameIn.addEventListener("keydown", (e) => { if (e.key === "Enter") body.querySelector(".make").click(); });
    body.querySelector(".clear").addEventListener("click", () => { cats = []; n = 0; draw(); showCode("// объектов нет"); msg.className = "w-msg"; msg.textContent = "Все объекты удалены. Чертёж остался — можно строить заново."; });
    draw(); showCode("// тут появится код");
  };

  // =====================================================================
  // 4. Сборщик мусора: переменные-ниточки и объекты в памяти
  // =====================================================================
  W.gc = function (el) {
    const { body, goal } = frame(el, { icon: "🧹", kind: "Мини-игра", title: "Сборщик мусора: кто кому нужен?", xp: 30,
      goals: ["Создай объект через new", "Сделай объект мусором", "Запусти сборщик мусора", "Пусть две переменные смотрят на один объект"] });
    const CAP = 6;
    const VARS = [{ v: "кот1", col: "#5b5bd6" }, { v: "кот2", col: "#e5488a" }, { v: "кот3", col: "#0fa596" }];
    let objs = [], refs = { кот1: null, кот2: null, кот3: null }, seq = 0, busy = false;
    body.innerHTML = `<div class="gc">
        <svg class="gc-lines"></svg>
        <div class="gc-vars">${VARS.map((x, i) => `
          <div class="gc-var" data-v="${x.v}">
            <div class="vn"><i style="background:${x.col}"></i>${x.v}</div><div class="vv"></div>
            <div class="w-row">
              <button class="chip" data-act="new">= new Кот()</button>
              <button class="chip" data-act="null">= null</button>
              ${i > 0 ? `<button class="chip" data-act="copy">= ${VARS[i - 1].v}</button>` : ""}
            </div></div>`).join("")}</div>
        <div class="gc-heap"><div class="gc-meter">${"<b></b>".repeat(CAP)}</div></div>
      </div>
      <div class="w-row" style="justify-content:center;margin-top:16px"><button class="btn green run">🧹 Запустить сборщик мусора</button></div>
      <div class="w-msg">Слева — <b>переменные</b>: это ниточки, за которые программа держит объекты. Справа — <b>память</b>. Нажми <code>= new Кот()</code> у переменной <code>кот1</code>.</div>`;
    const wrap = body.querySelector(".gc"), svg = body.querySelector(".gc-lines"), heap = body.querySelector(".gc-heap"), msg = body.querySelector(".w-msg");
    const say = (cls, html) => { msg.className = "w-msg " + cls; msg.innerHTML = html; };
    const refCount = (id) => Object.values(refs).filter((r) => r === id).length;

    function render() {
      VARS.forEach((x) => {
        const id = refs[x.v], o = objs.find((q) => q.id === id);
        wrap.querySelector(`[data-v="${x.v}"] .vv`).textContent = o ? `→ ${o.name}` : "= null (ни на кого не смотрит)";
      });
      heap.querySelectorAll(".gc-obj").forEach((n) => { if (!objs.find((o) => "o" + o.id === n.dataset.id)) n.remove(); });
      objs.forEach((o) => {
        let node = heap.querySelector(`[data-id="o${o.id}"]`);
        if (!node) {
          node = document.createElement("div"); node.className = "gc-obj"; node.dataset.id = "o" + o.id;
          node.innerHTML = `${cat(o.color)}<div class="name">${esc(o.name)}</div><div class="refs"></div>`;
          heap.appendChild(node);
        }
        const rc = refCount(o.id);
        node.classList.toggle("garbage", rc === 0);
        node.querySelector(".refs").textContent = rc === 0 ? "никто не держит" : `ниточек: ${rc}`;
      });
      const meter = [...heap.querySelectorAll(".gc-meter b")];
      meter.forEach((b, i) => { const o = objs[i]; b.className = !o ? "" : refCount(o.id) ? "used" : "junk"; });
      requestAnimationFrame(drawLines);
    }
    function drawLines() {
      const wr = wrap.getBoundingClientRect();
      svg.setAttribute("viewBox", `0 0 ${wr.width} ${wr.height}`);
      svg.setAttribute("width", wr.width); svg.setAttribute("height", wr.height);
      let html = "";
      VARS.forEach((x) => {
        const id = refs[x.v]; if (id === null) return;
        // ниточка выходит из правого края карточки переменной (на телефоне — из нижнего), чтобы не пересекать соседние карточки
        const a = wrap.querySelector(`[data-v="${x.v}"]`).getBoundingClientRect();
        const bNode = heap.querySelector(`[data-id="o${id}"]`); if (!bNode) return;
        const b = bNode.getBoundingClientRect();
        let ax, ay, bx, by, d;
        if (b.left > a.right + 10) {
          ax = a.right - wr.left; ay = a.top + 22 - wr.top; bx = b.left - wr.left; by = b.top + b.height / 2 - wr.top;
          const mx = (ax + bx) / 2; d = `M${ax},${ay} C${mx},${ay} ${mx},${by} ${bx},${by}`;
        } else {
          ax = a.left + a.width / 2 - wr.left; ay = a.bottom - wr.top; bx = b.left + b.width / 2 - wr.left; by = b.top - wr.top;
          const my = (ay + by) / 2; d = `M${ax},${ay} C${ax},${my} ${bx},${my} ${bx},${by}`;
        }
        html += `<path d="${d}" stroke="${x.col}" opacity=".85"/><circle cx="${ax}" cy="${ay}" r="5" fill="${x.col}"/><circle cx="${bx}" cy="${by}" r="5" fill="${x.col}"/>`;
      });
      svg.innerHTML = html;
    }
    new ResizeObserver(() => drawLines()).observe(wrap);

    async function collect(auto) {
      const junk = objs.filter((o) => refCount(o.id) === 0);
      if (!junk.length) { if (!auto) say("info", "Мусора нет: на каждый объект кто-то держит ниточку. Сборщик ничего не тронет — эти объекты ещё нужны программе."); return 0; }
      busy = true;
      const robot = document.createElement("div"); robot.className = "gc-robot"; robot.textContent = "🧹"; robot.style.left = "0px";
      heap.appendChild(robot);
      requestAnimationFrame(() => (robot.style.left = heap.clientWidth - 50 + "px"));
      await sleep(450);
      junk.forEach((o) => heap.querySelector(`[data-id="o${o.id}"]`)?.classList.add("swept"));
      await sleep(650);
      objs = objs.filter((o) => refCount(o.id) > 0);
      robot.remove(); busy = false;
      render();
      goal(2);
      return junk.length;
    }
    async function act(v, a) {
      if (busy) return;
      const before = refs[v];
      if (a === "new") {
        if (objs.length >= CAP) {
          const freed = await collect(true);
          if (!freed) { say("bad", `💥 <b>OutOfMemoryError!</b> Память кончилась, а выбросить нечего — все объекты кому-то нужны. Отпусти какой-нибудь объект (<code>= null</code>) и попробуй снова.`); return; }
          say("info", `Память была заполнена — JVM <b>сама</b> позвала сборщика мусора, он освободил ${freed} ${freed === 1 ? "ячейку" : freed < 5 ? "ячейки" : "ячеек"}. Так и работает Java: программист не убирает за собой вручную.`);
        }
        const c = CAT_COLORS[seq % CAT_COLORS.length];
        const o = { id: ++seq, name: CAT_NAMES[(seq - 1) % CAT_NAMES.length], color: c.c };
        objs.push(o); refs[v] = o.id;
        goal(0);
        if (objs.length < CAP || msg.className.indexOf("info") < 0) say("ok", `<code>${v} = new Кот()</code> — в памяти появился объект <b>${o.name}</b>, а ${v} держит его за ниточку.`);
      } else if (a === "null") {
        refs[v] = null;
        say("", `<code>${v} = null</code> — переменная отпустила ниточку.`);
      } else if (a === "copy") {
        const src = VARS[VARS.findIndex((x) => x.v === v) - 1].v;
        if (refs[src] === null) { say("info", `<code>${src}</code> сейчас ни на кого не смотрит, копировать нечего. Сначала создай объект для ${src}.`); return; }
        refs[v] = refs[src];
        say("ok", `<code>${v} = ${src}</code> — теперь <b>две</b> переменные смотрят на один и тот же объект. Это не копия кота, это вторая ниточка к тому же коту!`);
        goal(3);
      }
      render();
      if (before !== null && refCount(before) === 0 && objs.find((o) => o.id === before)) {
        const o = objs.find((q) => q.id === before);
        say("info", `🎈 На объект <b>${o.name}</b> больше никто не держит ниточку — он стал <b>мусором</b>: программа не сможет до него добраться. Нажми «Запустить сборщик мусора».`);
        goal(1);
      }
    }
    wrap.querySelectorAll("[data-act]").forEach((b) => b.addEventListener("click", () => act(b.closest("[data-v]").dataset.v, b.dataset.act)));
    body.querySelector(".run").addEventListener("click", async () => {
      if (busy) return;
      const n = await collect(false);
      if (n) say("ok", `🧹 Сборщик мусора убрал ${n === 1 ? "1 объект, за который" : n + (n < 5 ? " объекта" : " объектов") + ", за которые"} никто не держал ниточку. Память освобождена!`);
    });
    render();
  };

  // =====================================================================
  // 5. Фабрика Java: Main.java → javac → Main.class → JVM → экран
  // =====================================================================
  W.pipeline = function (el) {
    const { body, goal } = frame(el, { icon: "🏭", kind: "Мини-игра", title: "Фабрика Java: скомпилируй и запусти", xp: 30,
      goals: ["Скомпилируй и запусти программу", "Сломай код и посмотри, что скажет javac", "Измени текст и запусти без javac — что будет?"] });
    let text = "Hello, World!", broken = false, cls = null, busy = false;
    const ST = [["📄", "Main.java", "исходный код"], ["⚙️", "javac", "компилятор"], ["📦", "Main.class", "байт-код"], ["☕", "JVM", "виртуальная машина"], ["🖥️", "Экран", "вывод программы"]];
    body.innerHTML = `
      <div class="pl-editor">
        <pre data-file="Main.java"><code class="src"></code></pre>
        <div class="pl-controls">
          <label>Что напечатать?<input class="w-input txt" maxlength="24" value="${esc(text)}"></label>
          <label class="check"><input type="checkbox" class="brk"> Сломать код: убрать точку с запятой <code>;</code> в конце команды</label>
        </div>
      </div>
      <div class="pl-track">${ST.map((s, i) => `<div class="pl-st" data-i="${i}"><div class="ic"><span>${s[0]}</span></div><div><div class="lb">${s[1]}</div><div class="sb">${s[2]}</div></div></div>`).join("")}<div class="pl-packet"></div></div>
      <div class="pl-actions"><button class="btn indigo javac">javac Main.java</button><button class="btn green java">java Main</button></div>
      <div class="pl-term"><span class="h ph">// здесь, как в настоящем терминале, появятся команды и ответы</span></div>
      <div class="w-msg">Сначала <b>скомпилируй</b> (javac), потом <b>запусти</b> (java). Следи за посылкой — она едет по конвейеру.</div>`;
    const src = body.querySelector(".src"), term = body.querySelector(".pl-term"), msg = body.querySelector(".w-msg");
    const track = body.querySelector(".pl-track"), packet = body.querySelector(".pl-packet");
    const st = [...body.querySelectorAll(".pl-st")];
    const say = (cls2, html) => { msg.className = "w-msg " + cls2; msg.innerHTML = html; };
    const log = (cls2, t) => { if (term.querySelector(".ph")) term.innerHTML = ""; term.insertAdjacentHTML("beforeend", `<div class="${cls2}">${esc(t)}</div>`); term.scrollTop = term.scrollHeight; };
    const code = () => `public class Main {\n    public static void main(String[] args) {\n        System.out.println("${text}")${broken ? "" : ";"}\n    }\n}`;
    function paintSrc() {
      src.innerHTML = JC.highlightJava(code());
      st[2].classList.toggle("stale", cls !== null && (cls !== text || broken));
      st[2].querySelector(".sb").textContent = cls === null ? "байт-код" : (cls !== text || broken) ? "⚠ устарел" : "CA FE BA BE";
    }
    function reset() { st.forEach((s) => s.classList.remove("active", "ok", "err", "spin")); }
    async function moveTo(i) {
      const t = track.getBoundingClientRect(), r = st[i].querySelector(".ic").getBoundingClientRect();
      packet.style.top = "0px"; packet.style.left = "0px";
      packet.style.transform = `translate(${r.left - t.left + r.width / 2 - 13}px, ${r.top - t.top + r.height / 2 - 13}px)`;
      packet.style.opacity = 1;
      await sleep(560);
    }
    function jump(i) { packet.style.transition = "none"; moveTo(i); packet.offsetWidth; packet.style.transition = ""; }
    const lock = (on) => { busy = on; body.querySelectorAll(".pl-actions .btn").forEach((b) => (b.disabled = on)); };

    body.querySelector(".txt").addEventListener("input", (e) => {
      // кавычки и обратный слэш внутри строки сломали бы программу — убираем их
      const clean = e.target.value.replace(/["\\]/g, "");
      if (clean !== e.target.value) e.target.value = clean;
      text = clean; paintSrc();
    });
    body.querySelector(".brk").addEventListener("change", (e) => { broken = e.target.checked; paintSrc(); });

    body.querySelector(".javac").addEventListener("click", async () => {
      if (busy) return; lock(true); reset();
      log("c", "javac Main.java");
      jump(0); st[0].classList.add("active"); await sleep(250);
      await moveTo(1); st[0].classList.remove("active"); st[1].classList.add("active", "spin"); await sleep(800); st[1].classList.remove("spin");
      if (broken) {
        st[1].classList.remove("active"); st[1].classList.add("err"); packet.style.opacity = 0;
        log("e", "Main.java:3: error: ';' expected");
        log("e", `        System.out.println("${text}")`);
        log("e", " ".repeat(8 + 'System.out.println("'.length + text.length + 2) + "^");
        log("e", "1 error");
        say("bad", `🛑 <b>javac нашёл ошибку и остановился.</b> Новый Main.class не появился. Сообщение говорит: файл <code>Main.java</code>, строка 3, ожидалась <code>;</code>. Убери галочку и скомпилируй снова.`);
        goal(1);
      } else {
        st[1].classList.remove("active"); st[1].classList.add("ok");
        await moveTo(2); st[2].classList.add("ok"); packet.style.opacity = 0;
        cls = text; paintSrc();
        log("h", "(тишина — значит, ошибок нет; рядом появился файл Main.class)");
        say("ok", `✅ Скомпилировано! javac перевёл текст в <b>байт-код</b> — файл <code>Main.class</code>. Теперь запусти его: <b>java Main</b>.`);
      }
      lock(false);
    });

    body.querySelector(".java").addEventListener("click", async () => {
      if (busy) return; lock(true); reset();
      log("c", "java Main");
      if (cls === null) {
        st[2].classList.add("err");
        log("e", "Error: Could not find or load main class Main");
        say("bad", `🤷 JVM не нашла <code>Main.class</code> — его ещё нет. Сначала скомпилируй программу командой <b>javac</b>.`);
        lock(false); return;
      }
      jump(2); st[2].classList.add("active"); await sleep(250);
      await moveTo(3); st[2].classList.remove("active"); st[3].classList.add("active", "spin"); await sleep(700); st[3].classList.remove("spin", "active"); st[3].classList.add("ok");
      await moveTo(4); st[4].classList.add("ok"); packet.style.opacity = 0;
      log("o", cls);
      if (cls !== text || broken) {
        say("info", `😮 <b>Напечаталось старое «${esc(cls)}»!</b> Ты поменял Main.java, но не перекомпилировал. JVM запускает <code>Main.class</code>, а не исходник — а он остался прежним. Частая ошибка новичков: после правки — снова javac.`);
        goal(2);
      } else {
        say("ok", `🎉 Программа сработала! JVM прочитала байт-код и вывела текст. Попробуй теперь <b>поменять текст</b> и сразу нажать <b>java Main</b>, без javac.`);
        goal(0);
      }
      lock(false);
    });
    paintSrc();
  };

  // =====================================================================
  // 6. Один .class — три системы
  // =====================================================================
  W.osrun = function (el) {
    const { body, goal } = frame(el, { icon: "🌍", kind: "Эксперимент", title: "Написал один раз — запускай где угодно", goals: ["Запусти Main.class на всех трёх системах", "Попробуй запустить .exe не на Windows"] });
    const OS = [{ id: "win", name: "Windows", jvm: "JVM для Windows" }, { id: "mac", name: "macOS", jvm: "JVM для macOS (Apple M)" }, { id: "lin", name: "Linux", jvm: "JVM для Linux" }];
    let file = "class"; const ran = new Set();
    body.innerHTML = `<p style="margin-top:0"><b>1.</b> Выбери файл. <b>2.</b> Нажми на компьютер, где хочешь его запустить.</p>
      <div class="os-files"><button class="chip sel" data-f="class">📦 Main.class (байт-код Java)</button><button class="chip" data-f="exe">🪟 hello.exe (программа под Windows)</button></div>
      <div class="os-grid">${OS.map((o) => `<div class="os-win ${o.id}" data-os="${o.id}" role="button" tabindex="0">
          <div class="os-bar">${o.id === "mac" ? "<i></i>" : ""}<span>${o.id === "lin" ? "user@linux: ~" : o.id === "win" ? "Командная строка" : "Терминал"}</span>${o.id === "win" ? "<span>— ☐ ✕</span>" : ""}</div>
          <div class="os-screen"><span class="h">нажми, чтобы запустить</span></div><div class="os-label">${o.name}</div></div>`).join("")}</div>
      <div class="w-msg">Один и тот же файл попробуем запустить на трёх разных компьютерах.</div>`;
    const msg = body.querySelector(".w-msg");
    body.querySelectorAll("[data-f]").forEach((b) => b.addEventListener("click", () => {
      file = b.dataset.f; body.querySelectorAll("[data-f]").forEach((x) => x.classList.toggle("sel", x === b));
    }));
    async function run(osId, node) {
      const os = OS.find((o) => o.id === osId), scr = node.querySelector(".os-screen");
      const prompt = osId === "win" ? "C:\\> " : "$ ";
      scr.innerHTML = "";
      const line = async (cls, t, d = 380) => { scr.insertAdjacentHTML("beforeend", `<div class="${cls}">${esc(t)}</div>`); await sleep(d); };
      if (file === "class") {
        await line("", prompt + "java Main");
        await line("h", `[${os.jvm} читает байт-код…]`, 600);
        await line("o", "Hello, World!");
        ran.add(osId);
        msg.className = "w-msg ok";
        msg.innerHTML = ran.size < 3 ? `Сработало на ${os.name}! Свою JVM для каждой системы уже сделали за нас. Запусти и на остальных.` : `✅ Один и тот же <code>Main.class</code> заработал везде. Байт-код — как ноты, а JVM — музыкант, который играет их на своём инструменте.`;
        if (ran.size === 3) goal(0);
      } else {
        await line("", prompt + (osId === "win" ? "hello.exe" : "./hello.exe"));
        if (osId === "win") { await line("o", "Hello, World!"); msg.className = "w-msg"; msg.innerHTML = "На Windows .exe работает — он сделан как раз под неё. А теперь попробуй на Mac или Linux."; }
        else {
          await line("e", osId === "mac" ? "zsh: exec format error: ./hello.exe" : "bash: ./hello.exe: cannot execute binary file");
          msg.className = "w-msg bad"; msg.innerHTML = `❌ Не запустилось! <code>.exe</code> — это машинный код под Windows. Другой системе он непонятен. Вот почему Java придумала байт-код и JVM.`;
          goal(1);
        }
      }
    }
    body.querySelectorAll("[data-os]").forEach((n) => {
      n.addEventListener("click", () => run(n.dataset.os, n));
      n.addEventListener("keydown", (e) => { if (e.key === "Enter") run(n.dataset.os, n); });
    });
  };

  // =====================================================================
  // 7. Матрёшка JDK ⊃ JRE ⊃ JVM
  // =====================================================================
  W.matryoshka = function (el) {
    const { body, goal } = frame(el, { icon: "🪆", kind: "Открой матрёшку", title: "Что внутри JDK?", goals: ["Открой JDK", "Открой JRE", "Найди JVM"], xp: 15 });
    const INFO = {
      jdk: ["🧑‍🍳", "JDK — набор разработчика", "Всё, чтобы <b>писать</b> программы. Внутри — среда выполнения (JRE) и инструменты: компилятор <code>javac</code>, песочница <code>jshell</code> и другие. Это вся кухня шеф-повара. <b>Тебе нужен именно JDK.</b>"],
      jre: ["🍳", "JRE — среда выполнения", "Всё, чтобы <b>запускать</b> готовые программы: JVM + стандартная библиотека. Как плита с кастрюлями и продуктами: разогреть готовое блюдо можно, а придумать своё нечем — нет компилятора."],
      jvm: ["⚙️", "JVM — виртуальная машина Java", "Та самая «плита»: читает байт-код из <code>.class</code> и выполняет его на твоём процессоре. Ещё она сама следит за памятью — там живёт сборщик мусора."],
      lib: ["📚", "Стандартная библиотека", "Тысячи готовых классов, которые идут вместе с Java: <code>String</code>, <code>Math</code>, <code>Scanner</code>, списки… Продукты из магазина — не надо выращивать пшеницу, чтобы испечь пирог."],
      tool: ["🧰", "Инструменты разработчика", "<code>javac</code> — компилятор, <code>java</code> — запуск, <code>jshell</code> — песочница для экспериментов, <code>javap</code> — показывает байт-код. Ножи и весы шеф-повара."]
    };
    body.innerHTML = `<div class="mt">
      <div class="mt-box lvl-jdk" data-k="jdk"><div class="mt-h">🧑‍🍳 JDK <small>нажми, чтобы открыть</small></div>
        <div class="mt-in">
          <div class="mt-box lvl-jre" data-k="jre"><div class="mt-h">🍳 JRE <small>нажми, чтобы открыть</small></div>
            <div class="mt-in">
              <div class="mt-box lvl-jvm" data-k="jvm"><div class="mt-h">⚙️ JVM <small>сердце Java</small></div></div>
              <div class="mt-box lvl-lib" data-k="lib"><div class="mt-h">📚 Стандартная библиотека</div></div>
            </div></div>
          <div class="mt-box lvl-tool" data-k="tool"><div class="mt-h">🧰 Инструменты</div>
            <div class="mt-tools" style="margin-top:8px"><span class="chip">javac</span><span class="chip">java</span><span class="chip">jshell</span><span class="chip">javap</span></div></div>
        </div></div>
      <div class="mt-info"><div class="big">🪆</div><h4>Java как матрёшка</h4><p>Три слова, которые путают все новички, на самом деле вложены друг в друга. Открывай слой за слоем.</p></div>
    </div>`;
    const info = body.querySelector(".mt-info");
    body.querySelectorAll(".mt-box").forEach((box) => box.addEventListener("click", (e) => {
      e.stopPropagation();
      const k = box.dataset.k;
      if (box.querySelector(":scope > .mt-in")) { box.classList.add("open"); const s = box.querySelector(":scope > .mt-h small"); if (s) s.textContent = "открыто"; }
      body.querySelectorAll(".mt-box").forEach((b) => b.classList.toggle("sel", b === box));
      const [ic, h, p] = INFO[k];
      info.innerHTML = `<div class="big">${ic}</div><h4>${h}</h4><p>${p}</p>`;
      info.animate([{ opacity: 0 }, { opacity: 1 }], { duration: 250 });
      if (k === "jdk") goal(0); if (k === "jre") goal(1); if (k === "jvm") goal(2);
    }));
  };

  // =====================================================================
  // 8. Собери по порядку (Parsons problem)
  // =====================================================================
  // ---- Сортируемый список: перетаскивание мышкой/пальцем + кнопки ↑↓ + клавиатура (Alt+↑/↓) ----
  // items: [{t, k}] в текущем порядке; onChange() вызывается после каждой перестановки.
  const SORT_HINT = "Перетаскивай строки мышкой (или пальцем), чтобы выстроить правильный порядок. Можно и кнопками ↑ ↓ справа.";
  function sortable(list, items, isCode, onChange) {
    function render() {
      list.innerHTML = items.map((x, n) => `
        <div class="ps-line sortable ${isCode ? "code" : ""}" data-k="${x.k}" tabindex="0">
          <span class="grip" aria-hidden="true">⠿</span><span class="n">${n + 1}</span><span class="txt">${esc(x.t)}</span>
          <span class="mv"><button class="mv-up" aria-label="Выше" ${n === 0 ? "disabled" : ""}>↑</button><button class="mv-dn" aria-label="Ниже" ${n === items.length - 1 ? "disabled" : ""}>↓</button></span>
        </div>`).join("");
      list.querySelectorAll(".ps-line").forEach((row, n) => {
        row.querySelector(".mv-up").addEventListener("click", (e) => { e.stopPropagation(); move(n, n - 1, true); });
        row.querySelector(".mv-dn").addEventListener("click", (e) => { e.stopPropagation(); move(n, n + 1, true); });
        row.addEventListener("keydown", (e) => {
          if (e.altKey && e.key === "ArrowUp") { e.preventDefault(); move(n, n - 1, true); }
          if (e.altKey && e.key === "ArrowDown") { e.preventDefault(); move(n, n + 1, true); }
        });
        row.addEventListener("pointerdown", (e) => startDrag(e, row));
      });
    }
    function move(from, to, focus) {
      if (to < 0 || to >= items.length || list.classList.contains("locked")) return;
      items.splice(to, 0, items.splice(from, 1)[0]);
      render(); onChange();
      if (focus) list.children[to].focus({ preventScroll: true });
    }
    function startDrag(e, row) {
      if (e.button !== 0 || e.target.closest("button") || list.classList.contains("locked")) return;
      e.preventDefault();
      row.focus({ preventScroll: true }); // чтобы сразу работали Alt+↑/↓
      const rows = () => [...list.querySelectorAll(".ps-line")];
      let startY = e.clientY;
      row.classList.add("dragging");
      const onMove = (ev) => {
        let dy = ev.clientY - startY;
        row.style.transform = `translateY(${dy}px)`;
        const r = row.getBoundingClientRect(), mid = r.top + r.height / 2;
        const prev = row.previousElementSibling, next = row.nextElementSibling;
        const before = row.offsetTop;
        if (next && mid > next.getBoundingClientRect().top + next.offsetHeight / 2) list.insertBefore(next, row);
        else if (prev && mid < prev.getBoundingClientRect().top + prev.offsetHeight / 2) list.insertBefore(prev, row.nextSibling);
        else return;
        startY += row.offsetTop - before; // строка переехала в DOM — компенсируем, чтобы она не прыгала под пальцем
        row.style.transform = `translateY(${ev.clientY - startY}px)`;
      };
      const onUp = () => {
        window.removeEventListener("pointermove", onMove);
        window.removeEventListener("pointerup", onUp);
        window.removeEventListener("pointercancel", onUp);
        row.classList.remove("dragging"); row.style.transform = "";
        const order = rows().map((n) => +n.dataset.k);
        const changed = order.some((k, i) => k !== items[i].k);
        items.sort((a, b) => order.indexOf(a.k) - order.indexOf(b.k));
        render(); if (changed) onChange();
      };
      // слушаем на window: отпускание мыши поймается, где бы курсор ни оказался
      window.addEventListener("pointermove", onMove);
      window.addEventListener("pointerup", onUp);
      window.addEventListener("pointercancel", onUp);
    }
    render();
    return { render };
  }
  /** Перемешать так, чтобы порядок точно не совпал с правильным. */
  function shuffledOrder(lines) {
    let sh = shuffle(lines.map((t, k) => ({ t, k })));
    if (lines.length > 1 && sh.every((x, i) => x.t === lines[i])) sh = sh.reverse();
    return sh;
  }
  /** Подсветить строки: на своём месте / нет. Одинаковые строки (например, «}») взаимозаменяемы. */
  function markOrder(list, items, lines) {
    let wrong = 0;
    list.querySelectorAll(".ps-line").forEach((n, i) => {
      const ok = items[i].t === lines[i];
      n.classList.toggle("good", ok); n.classList.toggle("bad", !ok); if (!ok) wrong++;
    });
    return wrong;
  }

  W.parsons = function (el) {
    const raw = readData(el, "text/plain") || "";
    const lines = raw.replace(/^\n+|\s+$/g, "").split("\n");
    const isCode = el.dataset.code !== undefined;
    const { body, goal } = frame(el, { icon: el.dataset.icon || "🧩", kind: "Собери по порядку", title: el.dataset.title || "Расставь строки по порядку", goals: ["Собери правильный порядок"], xp: 15 });
    // старые подсказки про «нажимай по порядку» больше не подходят — показываем свою
    const extra = el.dataset.hint && !/нажима/i.test(el.dataset.hint) ? " " + el.dataset.hint : "";
    body.innerHTML = `<p style="margin-top:0">${SORT_HINT}${extra}</p>
      <div class="ps-zone sort-list"></div>
      <div class="w-row" style="justify-content:center"><button class="btn green check">Проверить</button><button class="btn ghost small again">Перемешать заново</button></div>
      <div class="w-msg" hidden></div>`;
    const list = body.querySelector(".sort-list"), msg = body.querySelector(".w-msg");
    let items;
    const changed = () => { msg.hidden = true; list.querySelectorAll(".ps-line").forEach((n) => n.classList.remove("good", "bad")); };
    function start() { items = shuffledOrder(lines); msg.hidden = true; sortable(list, items, isCode, changed); }
    // data-accept='[[1,3,2,4]]' — другие верные порядки: номера строк исходного списка (с 1).
    // Нужно, когда строки честно можно поменять местами (например, две независимые переменные).
    let accept = [];
    try { accept = JSON.parse(el.dataset.accept || "[]"); } catch (e) { accept = []; }
    body.querySelector(".check").addEventListener("click", () => {
      msg.hidden = false;
      const cur = items.map((x) => x.t);
      const alt = accept.find((order) => order.length === cur.length && order.every((n, i) => lines[n - 1] === cur[i]));
      const wrong = markOrder(list, items, alt ? alt.map((n) => lines[n - 1]) : lines);
      if (!wrong) { msg.className = "w-msg ok"; msg.innerHTML = el.dataset.success || "🎉 Всё верно!"; goal(0); }
      else { msg.className = "w-msg bad"; msg.textContent = `Не на своём месте: ${wrong}. Перетащи красные строки и проверь ещё раз.`; }
    });
    body.querySelector(".again").addEventListener("click", start);
    start();
  };

  // =====================================================================
  // 10. Трассировщик: код по шагам + переменные (+ стек и куча со стрелками)
  //     steps[i] = { line, vars:{имя:значение}, out:"напечатано на этом шаге", note:"пояснение",
  //                  ask:["имя"] — ученик вписывает значение сам, stack:[{frame, vars}], heap:[{id,label,fields}] }
  //     Значение вида "→#1" — ссылка на объект #1 в куче (рисуется стрелкой).
  // =====================================================================
  W.trace = function (el) {
    const data = readData(el);
    const steps = data.steps, lines = data.code.split("\n");
    const memory = steps.some((s) => s.stack || s.heap);
    const hasAsk = steps.some((s) => s.ask && s.ask.length);
    const goals = ["Пройди программу до конца"];
    if (hasAsk) goals.push("Угадай значения на шагах");
    const { body, goal } = frame(el, { icon: data.icon || (memory ? "🧠" : "🔍"), kind: data.kind || (memory ? "Память по шагам" : "Трассировка"), title: data.title || "Выполняем программу по шагам", goals, xp: 15 });
    let cur = -1, outText = "", guessedAll = true;
    body.innerHTML = `
      <div class="tr-grid ${memory ? "mem" : ""} ${Math.max(...lines.map((l) => l.length)) > 56 ? "stack" : ""}">
        <div class="tr-code">${lines.map((l, k) => `<div class="tr-line" data-l="${k + 1}"><span class="no">${k + 1}</span><code>${JC.highlightJava(l) || " "}</code></div>`).join("")}</div>
        <div class="tr-side">
          <div class="tr-vars"></div>
          <div class="tr-console"><small>Консоль</small><pre></pre></div>
        </div>
      </div>
      <div class="tr-note w-msg">${data.intro || "Нажимай «Шаг ▶» — программа выполнится по одной строке. Справа видно, что лежит в переменных."}</div>
      <div class="tr-ask" hidden></div>
      <div class="w-row tr-nav"><button class="btn ghost small back">◀ Назад</button><span class="tr-count"></span><button class="btn indigo next">Шаг ▶</button><button class="btn ghost small reset">⟲ Сначала</button></div>`;
    const codeLines = [...body.querySelectorAll(".tr-line")];
    const varsBox = body.querySelector(".tr-vars"), con = body.querySelector(".tr-console pre");
    const note = body.querySelector(".tr-note"), askBox = body.querySelector(".tr-ask");
    const next = body.querySelector(".next"), back = body.querySelector(".back"), count = body.querySelector(".tr-count");

    function varsTable(vars, changed, hide) {
      const names = Object.keys(vars || {});
      if (!names.length) return `<div class="tr-empty">переменных пока нет</div>`;
      return `<table class="tr-table"><tr><th>переменная</th><th>значение</th></tr>${names.map((n) => {
        const v = String(vars[n]);
        const shown = hide && hide.includes(n) ? `<span class="tr-q">?</span>` : v.startsWith("→") ? `<span class="tr-ref" data-ref="${esc(v.slice(1))}">● ссылка ${esc(v.slice(1))}</span>` : esc(v);
        return `<tr class="${changed.includes(n) ? "chg" : ""}"><td class="mono">${esc(n)}</td><td class="mono" data-var="${esc(n)}">${shown}</td></tr>`;
      }).join("")}</table>`;
    }
    function render(hideAsk) {
      const s = steps[cur] || {};
      const prev = steps[cur - 1] || {};
      codeLines.forEach((l) => l.classList.toggle("cur", cur >= 0 && +l.dataset.l === s.line));
      const changed = Object.keys(s.vars || {}).filter((k) => !prev.vars || prev.vars[k] !== s.vars[k]);
      if (memory) {
        const stack = s.stack || [{ frame: "main", vars: s.vars || {} }];
        varsBox.innerHTML = `<div class="tr-mem"><div class="tr-stack"><small>Стек (переменные методов)</small>${stack.slice().reverse().map((f, k) => `<div class="tr-frame ${k === 0 ? "top" : ""}"><div class="fn">${esc(f.frame)}${f.frame.includes("(") ? "" : "()"}${f.returns !== undefined ? ` <span class="ret">→ вернёт ${esc(f.returns)}</span>` : ""}</div>${varsTable(f.vars, k === 0 ? changed : [], hideAsk ? s.ask : null)}</div>`).join("")}</div>
          <div class="tr-heap"><small>Куча (объекты)</small>${(s.heap || []).map((o) => `<div class="tr-obj" data-id="${esc(o.id)}"><div class="hd">${esc(o.id)} · ${esc(o.label)}</div>${o.fields ? Object.entries(o.fields).map(([k, v]) => `<div class="fl"><span>${esc(k)}</span><b>${hideAsk && s.ask && s.ask.includes(o.id + "." + k) ? `<span class="tr-q">?</span>` : esc(v)}</b></div>`).join("") : ""}</div>`).join("") || `<div class="tr-empty">объектов нет</div>`}</div>
          <svg class="tr-arrows"></svg></div>`;
        requestAnimationFrame(drawArrows);
      } else {
        varsBox.innerHTML = `<small>Переменные</small>` + varsTable(s.vars, changed, hideAsk ? s.ask : null);
      }
      con.textContent = outText || " ";
      count.textContent = cur < 0 ? `0 / ${steps.length}` : `шаг ${cur + 1} / ${steps.length}`;
      back.disabled = cur < 0; next.disabled = cur >= steps.length - 1 && !hideAsk;
      next.textContent = cur >= steps.length - 1 ? "Готово ✓" : "Шаг ▶";
    }
    function drawArrows() {
      const wrap = varsBox.querySelector(".tr-mem"); if (!wrap) return;
      const svg = wrap.querySelector(".tr-arrows"), wr = wrap.getBoundingClientRect();
      svg.setAttribute("width", wr.width); svg.setAttribute("height", wr.height);
      let h = "";
      wrap.querySelectorAll(".tr-ref").forEach((r) => {
        const obj = wrap.querySelector(`.tr-obj[data-id="${CSS.escape(r.dataset.ref)}"]`); if (!obj) return;
        const a = r.getBoundingClientRect(), b = obj.getBoundingClientRect();
        const x1 = a.right + 4 - wr.left, y1 = a.top + a.height / 2 - wr.top;
        let x2, y2, d;
        if (b.left > a.right) { // куча справа от стека
          x2 = b.left - wr.left; y2 = b.top + 16 - wr.top;
          const mx = (x1 + x2) / 2; d = `M${x1},${y1} C${mx},${y1} ${mx},${y2} ${x2},${y2}`;
        } else { // куча под стеком (узкий экран): выходим вправо и спускаемся к верху объекта
          x2 = b.right - 18 - wr.left; y2 = b.top - wr.top;
          const rx = Math.max(x1, x2) + 24; d = `M${x1},${y1} C${rx},${y1} ${x2},${y2 - 40} ${x2},${y2}`;
        }
        h += `<path d="${d}" stroke="var(--indigo)" stroke-width="2.5" fill="none"/><circle cx="${x2}" cy="${y2}" r="4" fill="var(--indigo)"/>`;
      });
      svg.innerHTML = h;
    }
    function computeOut(upto) { outText = ""; for (let k = 0; k <= upto; k++) if (steps[k] && steps[k].out !== undefined) outText += steps[k].out; }
    function go(i) {
      cur = i; computeOut(cur);
      const s = steps[cur] || {};
      askBox.hidden = true;
      if (s.ask && s.ask.length && !s._done) {
        render(true);
        askBox.hidden = false;
        askBox.innerHTML = `<b>Твой ход:</b> какое значение будет у ${s.ask.map((n) => `<code>${esc(n)}</code>`).join(", ")} после этой строки?
          <div class="w-row" style="margin-top:8px">${s.ask.map((n) => `<label class="mono">${esc(n)} = <input class="w-input tr-in" data-n="${esc(n)}" size="8"></label>`).join("")}<button class="btn small green ok">Проверить</button></div>`;
        // пояснение не показываем до ответа — оно может выдать ответ
        note.className = "tr-note w-msg info"; note.innerHTML = "Подумай: что сделает подсвеченная строка? Впиши значение и нажми «Проверить».";
        next.disabled = true;
        const check = () => {
          let ok = true;
          // в режиме памяти значение берём из верхнего фрейма стека (если vars не заданы)
          const top = s.stack ? s.stack[s.stack.length - 1].vars : {};
          const lookup = (n) => {
            const hm = n.match(/^(#[^.]+)\.(.+)$/); // "#1.имя" — поле объекта в куче
            if (hm) { const o = (s.heap || []).find((x) => x.id === hm[1]); return o && o.fields ? o.fields[hm[2]] : undefined; }
            return (s.vars && n in s.vars) ? s.vars[n] : top[n];
          };
          askBox.querySelectorAll(".tr-in").forEach((inp) => { const norm = (v) => String(v).trim().replace(/^"|"$/g, "").replace(/\s+/g, ""); // пробелы не важны: [1, 2] = [1,2]
            const right = norm(lookup(inp.dataset.n)); const good = norm(inp.value) === right; inp.classList.toggle("right", good); inp.classList.toggle("wrong", !good); if (!good) ok = false; });
          if (!ok) guessedAll = false;
          s._done = true;
          askBox.insertAdjacentHTML("beforeend", `<div class="w-msg ${ok ? "ok" : "bad"}" style="margin-top:8px">${ok ? "✓ Верно!" : "✗ Смотри правильное значение в таблице."}</div>`);
          askBox.querySelector(".ok").disabled = true;
          render(false); note.className = "tr-note w-msg"; note.innerHTML = s.note || "";
          next.disabled = false;
        };
        askBox.querySelector(".ok").addEventListener("click", check);
        askBox.querySelectorAll(".tr-in").forEach((i2) => i2.addEventListener("keydown", (e) => { if (e.key === "Enter") check(); }));
        setTimeout(() => askBox.querySelector(".tr-in").focus({ preventScroll: true }), 30);
        return;
      }
      render(false);
      note.className = "tr-note w-msg" + (cur === steps.length - 1 ? " ok" : "");
      note.innerHTML = cur < 0 ? (data.intro || "") : (s.note || "");
      if (cur === steps.length - 1) { goal(0); if (hasAsk && guessedAll && steps.every((x) => !x.ask || x._done)) goal(1); }
    }
    next.addEventListener("click", () => { if (cur < steps.length - 1) go(cur + 1); });
    back.addEventListener("click", () => { if (cur >= 0) go(cur - 1); });
    body.querySelector(".reset").addEventListener("click", () => { steps.forEach((s) => delete s._done); guessedAll = true; go(-1); });
    new ResizeObserver(() => drawArrows()).observe(body);
    go(-1);
  };

  // =====================================================================
  // 9. Быстрая проверка: лента коротких заданий разных типов
  //    single · multi · tf · match · blank · predict · bug · order
  //    Ошибки возвращаются в конце (как в Duolingo).
  // =====================================================================
  const normOut = (s) => String(s ?? "").replace(/\r\n?/g, "\n").split("\n").map((l) => l.replace(/\s+$/, "")).join("\n").replace(/\n+$/, "");
  const codeHtml = (src) => `<pre class="ck-code"><code>${JC.highlightJava(src)}</code></pre>`;

  W.check = function (el) {
    const data = readData(el);
    const items = data.items;
    const { body, goal } = frame(el, { icon: data.icon || "⚡", kind: data.kind || "Быстрая проверка", title: data.title || "Проверь себя", goals: ["Пройди проверку", "Ответь на всё без ошибок"], xp: 15 });
    let queue, idx, firstTry, wrongIds, answered, mistakes, total, checked, state;
    let touched = false; // ученик уже нажимал кнопки этой проверки

    function start() {
      queue = items.map((it, i) => ({ it, i, retry: false }));
      idx = 0; firstTry = 0; mistakes = 0; wrongIds = new Set(); answered = new Set(); total = items.length;
      show();
    }
    function show() {
      if (idx >= queue.length) return finish();
      const { it, retry } = queue[idx];
      checked = false; state = {};
      const n = queue.slice(0, idx + 1).filter((q) => !q.retry).length;
      body.innerHTML = `
        <div class="ck-top"><span>${retry ? "🔁 Повторим ошибку" : `Вопрос ${n} из ${total}`}</span>
          <span class="ck-dots">${items.map((_, i) => `<i class="${i === queue[idx].i ? "cur" : wrongIds.has(i) ? "bad" : answered.has(i) ? "done" : ""}"></i>`).join("")}</span></div>
        <div class="ck-q">${it.q}</div>
        <div class="ck-body"></div>
        <div class="w-msg" hidden></div>
        <div class="ck-actions"><button class="btn green ck-go" disabled>Проверить</button></div>`;
      const box = body.querySelector(".ck-body");
      const go = body.querySelector(".ck-go");
      const ready = (ok) => { go.disabled = !ok; };
      RENDER[it.type](box, it, state, ready);
      go.addEventListener("click", () => { touched = true; if (checked) { idx++; show(); } else check(); });
      // прокручиваем к вопросу только когда ученик сам листает проверку — не при загрузке страницы
      if (touched) body.querySelector(".ck-q").scrollIntoView({ block: "nearest" });
    }
    function check() {
      const { it, i, retry } = queue[idx];
      const ok = CHECK[it.type](body.querySelector(".ck-body"), it, state);
      checked = true;
      body.querySelector(".ck-body").classList.add("locked");
      const msg = body.querySelector(".w-msg");
      msg.hidden = false; msg.className = "w-msg " + (ok ? "ok" : "bad");
      msg.innerHTML = (ok ? "✓ Верно! " : "✗ Не совсем. ") + (it.why || "");
      if (ok && !retry && !wrongIds.has(i)) firstTry++;
      if (ok) { answered.add(i); if (retry) wrongIds.delete(i); }
      if (!ok) { mistakes++; wrongIds.add(i); queue.push({ it, i, retry: true }); }
      const go = body.querySelector(".ck-go");
      go.disabled = false; go.textContent = idx + 1 >= queue.length ? "Результат →" : "Дальше →";
      go.focus();
    }
    function finish() {
      const perfect = mistakes === 0;
      body.innerHTML = `<div class="ck-final ${perfect ? "perfect" : ""}">
          <div class="big">${perfect ? "🏆" : firstTry >= total * 0.7 ? "💪" : "📚"}</div>
          <h4>С первой попытки: ${firstTry} из ${total}</h4>
          <p>${perfect ? "Идеально — ни одной ошибки!" : `Ошибки разобраны и повторены. ${firstTry < total * 0.7 ? "Загляни ещё раз в разборы выше — и попробуй пройти проверку заново, второй раз пойдёт легче." : "Хороший результат!"}`}</p>
          <button class="btn ghost small again">Пройти ещё раз</button></div>`;
      body.querySelector(".again").addEventListener("click", start);
      goal(0); if (perfect) goal(1);
    }

    // ---------- отрисовка заданий ----------
    const RENDER = {
      single(box, it, st, ready) {
        box.innerHTML = (it.code ? codeHtml(it.code) : "") + `<div class="ck-opts">${it.options.map((o, k) => `<button class="ck-opt" data-k="${k}"><span class="mark">${"АБВГДЕ"[k]}</span><span>${o}</span></button>`).join("")}</div>`;
        box.querySelectorAll(".ck-opt").forEach((b) => b.addEventListener("click", () => {
          box.querySelectorAll(".ck-opt").forEach((x) => x.classList.toggle("sel", x === b)); st.pick = +b.dataset.k; ready(true);
        }));
      },
      multi(box, it, st, ready) {
        st.picks = new Set();
        box.innerHTML = (it.code ? codeHtml(it.code) : "") + `<p class="ck-hint">Отметь все подходящие варианты</p><div class="ck-opts">${it.options.map((o, k) => `<button class="ck-opt multi" data-k="${k}"><span class="mark box"></span><span>${o}</span></button>`).join("")}</div>`;
        box.querySelectorAll(".ck-opt").forEach((b) => b.addEventListener("click", () => {
          const k = +b.dataset.k; st.picks.has(k) ? st.picks.delete(k) : st.picks.add(k); b.classList.toggle("sel"); ready(st.picks.size > 0);
        }));
      },
      tf(box, it, st, ready) {
        st.ans = {};
        box.innerHTML = `<div class="ck-tf">${it.statements.map((s, k) => `<div class="ck-tf-row" data-k="${k}"><div class="s">${s.s}</div>
          <div class="b"><button class="ck-tfb" data-v="1">Верно</button><button class="ck-tfb" data-v="0">Неверно</button></div><div class="why" hidden></div></div>`).join("")}</div>`;
        box.querySelectorAll(".ck-tf-row").forEach((row) => row.querySelectorAll(".ck-tfb").forEach((b) => b.addEventListener("click", () => {
          row.querySelectorAll(".ck-tfb").forEach((x) => x.classList.toggle("sel", x === b));
          st.ans[row.dataset.k] = b.dataset.v === "1"; ready(Object.keys(st.ans).length === it.statements.length);
        })));
      },
      match(box, it, st, ready) {
        st.pairs = {}; st.sel = null;
        const order = shuffle(it.right.map((t, k) => ({ t, k })));
        box.innerHTML = `<p class="ck-hint">Нажми на элемент слева, потом на подходящий справа — появится линия. Нажми ещё раз, чтобы убрать.</p>
          <div class="ck-match"><svg class="ck-lines"></svg>
            <div class="col l">${it.left.map((t, k) => `<button class="ck-m" data-side="l" data-k="${k}">${t}</button>`).join("")}</div>
            <div class="col r">${order.map((o) => `<button class="ck-m" data-side="r" data-k="${o.k}">${o.t}</button>`).join("")}</div></div>`;
        const wrap = box.querySelector(".ck-match"), svg = box.querySelector(".ck-lines");
        const COLORS = ["#5b5bd6", "#e5488a", "#0fa596", "#f2a20c", "#2f80ed", "#8b5cf6", "#ff6b1a"];
        st.draw = (result) => {
          const wr = wrap.getBoundingClientRect();
          svg.setAttribute("width", wr.width); svg.setAttribute("height", wr.height);
          let html = "";
          Object.entries(st.pairs).forEach(([l, r], n) => {
            const a = wrap.querySelector(`[data-side="l"][data-k="${l}"]`).getBoundingClientRect();
            const b = wrap.querySelector(`[data-side="r"][data-k="${r}"]`).getBoundingClientRect();
            const x1 = a.right - wr.left, y1 = a.top + a.height / 2 - wr.top, x2 = b.left - wr.left, y2 = b.top + b.height / 2 - wr.top;
            const col = result ? (+l === +r ? "var(--green)" : "var(--red)") : COLORS[n % COLORS.length];
            const mx = (x1 + x2) / 2;
            html += `<path d="M${x1},${y1} C${mx},${y1} ${mx},${y2} ${x2},${y2}" stroke="${col}" stroke-width="3.5" fill="none" stroke-linecap="round"/><circle cx="${x1}" cy="${y1}" r="5" fill="${col}"/><circle cx="${x2}" cy="${y2}" r="5" fill="${col}"/>`;
          });
          svg.innerHTML = html;
          wrap.querySelectorAll(".ck-m").forEach((m) => {
            const k = m.dataset.k, side = m.dataset.side;
            const paired = side === "l" ? k in st.pairs : Object.values(st.pairs).map(String).includes(k);
            m.classList.toggle("paired", paired);
            m.classList.toggle("sel", side === "l" && st.sel === k);
          });
        };
        wrap.querySelectorAll(".ck-m").forEach((m) => m.addEventListener("click", () => {
          if (box.classList.contains("locked")) return;
          const k = m.dataset.k;
          if (m.dataset.side === "l") {
            if (k in st.pairs) { delete st.pairs[k]; st.sel = null; } else st.sel = st.sel === k ? null : k;
          } else if (st.sel !== null) {
            for (const l in st.pairs) if (String(st.pairs[l]) === k) delete st.pairs[l];
            st.pairs[st.sel] = +k; st.sel = null;
          } else {
            for (const l in st.pairs) if (String(st.pairs[l]) === k) delete st.pairs[l];
          }
          st.draw(); ready(Object.keys(st.pairs).length === it.left.length);
        }));
        new ResizeObserver(() => st.draw(st.result)).observe(wrap);
        requestAnimationFrame(() => st.draw());
      },
      blank(box, it, st, ready) {
        const parts = it.code.split("____");
        let html = "";
        parts.forEach((p, k) => {
          html += JC.highlightJava(p);
          if (k < parts.length - 1) {
            const w = Math.max(4, ...it.answers[k].map((a) => a.length)) + 1;
            html += `<input class="ck-blank" data-k="${k}" style="width:${w}ch" autocomplete="off" spellcheck="false">`;
          }
        });
        box.innerHTML = `<p class="ck-hint">Впиши пропущенное прямо в код</p><pre class="ck-code"><code>${html}</code></pre>`;
        const ins = [...box.querySelectorAll(".ck-blank")];
        ins.forEach((i) => i.addEventListener("input", () => ready(ins.every((x) => x.value.trim()))));
        ins.forEach((i) => i.addEventListener("keydown", (e) => { if (e.key === "Enter") { e.preventDefault(); body.querySelector(".ck-go").click(); } }));
        setTimeout(() => ins[0] && ins[0].focus({ preventScroll: true }), 50);
      },
      predict(box, it, st, ready) {
        box.innerHTML = codeHtml(it.code) + `<p class="ck-hint">Что напечатает программа? Напиши вывод точно, строка в строку.</p>
          <textarea class="ck-out" rows="${Math.max(2, String(it.answer).split("\n").length + 1)}" spellcheck="false" placeholder="вывод программы…"></textarea>`;
        const t = box.querySelector(".ck-out");
        t.addEventListener("input", () => ready(t.value.trim().length > 0));
      },
      bug(box, it, st, ready) {
        st.picks = new Set();
        const lines = it.code.split("\n");
        box.innerHTML = `<p class="ck-hint">${it.answer.length > 1 ? `Здесь ${it.answer.length} ошибочные строки — отметь их` : "Нажми на строку с ошибкой"}</p>
          <div class="ck-bug">${lines.map((l, k) => `<button class="ck-line" data-k="${k + 1}"><span class="no">${k + 1}</span><code>${JC.highlightJava(l) || " "}</code></button>`).join("")}</div>`;
        box.querySelectorAll(".ck-line").forEach((b) => b.addEventListener("click", () => {
          if (box.classList.contains("locked")) return;
          const k = +b.dataset.k;
          if (it.answer.length === 1) { st.picks = new Set([k]); box.querySelectorAll(".ck-line").forEach((x) => x.classList.toggle("sel", x === b)); }
          else { st.picks.has(k) ? st.picks.delete(k) : st.picks.add(k); b.classList.toggle("sel"); }
          ready(st.picks.size > 0);
        }));
      },
      order(box, it, st, ready) {
        st.items = shuffledOrder(it.lines);
        box.innerHTML = `<p class="ck-hint">${SORT_HINT}</p><div class="ps-zone sort-list"></div>`;
        st.list = box.querySelector(".sort-list");
        sortable(st.list, st.items, !!it.code, () => {});
        ready(true); // все строки уже в списке — можно проверять в любой момент
      },
      // «Собери из квадратиков»: описание строки кода из блоков; лишние блоки (extra) остаются в запасе
      build(box, it, st, ready) {
        st.picked = [];
        const pool = shuffle([...it.answer, ...(it.extra || [])].map((t, k) => ({ t, k })));
        box.innerHTML = (it.code ? codeHtml(it.code) : "") +
          `<p class="ck-hint">Нажимай квадратики по порядку — соберётся описание. Лишние не трогай. Ошибся — нажми на квадратик в ответе, он вернётся.</p>
          <div class="bd-answer" aria-label="Твой ответ"><span class="bd-empty">сюда встанут квадратики</span></div>
          <div class="bd-pool">${pool.map((p) => `<button class="bd-chip" data-k="${p.k}">${p.t}</button>`).join("")}</div>`;
        const ans = box.querySelector(".bd-answer"), empty = ans.querySelector(".bd-empty");
        const sync = () => { empty.hidden = st.picked.length > 0; ready(st.picked.length > 0); };
        box.querySelectorAll(".bd-pool .bd-chip").forEach((b) => b.addEventListener("click", () => {
          if (st.locked || b.disabled) return;
          b.disabled = true;
          const c = document.createElement("button");
          c.className = "bd-chip in"; c.innerHTML = b.innerHTML; c.dataset.k = b.dataset.k;
          c.addEventListener("click", () => {
            if (st.locked) return;
            st.picked = st.picked.filter((x) => x.el !== c); c.remove(); b.disabled = false; sync();
          });
          ans.appendChild(c); st.picked.push({ el: c, t: b.textContent }); sync();
        }));
        sync();
      }
    };

    // ---------- проверка ответов (true = верно) + подсветка ----------
    const CHECK = {
      single(box, it, st) {
        box.querySelectorAll(".ck-opt").forEach((b) => { const k = +b.dataset.k; b.classList.toggle("right", k === it.answer); b.classList.toggle("wrong", k === st.pick && k !== it.answer); });
        return st.pick === it.answer;
      },
      multi(box, it, st) {
        const ans = new Set(it.answer);
        box.querySelectorAll(".ck-opt").forEach((b) => { const k = +b.dataset.k; b.classList.toggle("right", ans.has(k)); b.classList.toggle("wrong", st.picks.has(k) && !ans.has(k)); b.classList.toggle("missed", ans.has(k) && !st.picks.has(k)); });
        return ans.size === st.picks.size && [...ans].every((k) => st.picks.has(k));
      },
      tf(box, it, st) {
        let ok = true;
        box.querySelectorAll(".ck-tf-row").forEach((row) => {
          const k = row.dataset.k, s = it.statements[k], good = st.ans[k] === s.a;
          if (!good) ok = false;
          row.classList.add(good ? "right" : "wrong");
          if (!good && s.why) { const w = row.querySelector(".why"); w.hidden = false; w.innerHTML = (s.a ? "Это верно: " : "Это неверно: ") + s.why; }
        });
        return ok;
      },
      match(box, it, st) {
        st.result = true; st.draw(true);
        box.querySelectorAll('.ck-m[data-side="l"]').forEach((m) => m.classList.add(+st.pairs[m.dataset.k] === +m.dataset.k ? "right" : "wrong"));
        const ok = it.left.every((_, k) => +st.pairs[k] === k);
        // ошибся — сразу показываем правильные пары, чтобы было с чем сравнить
        if (!ok) box.insertAdjacentHTML("beforeend", `<div class="ck-expected"><small>Правильные пары:</small><ul class="ck-pairs">${it.left.map((l, k) => `<li>${l} → ${it.right[k]}</li>`).join("")}</ul></div>`);
        return ok;
      },
      blank(box, it) {
        let ok = true;
        box.querySelectorAll(".ck-blank").forEach((i) => {
          const k = +i.dataset.k, good = it.answers[k].some((a) => a === i.value.trim());
          i.classList.add(good ? "right" : "wrong"); i.readOnly = true;
          if (!good) { ok = false; i.title = "Правильно: " + it.answers[k][0]; i.value = i.value.trim() + " → " + it.answers[k][0]; i.style.width = i.value.length + 1 + "ch"; }
        });
        return ok;
      },
      predict(box, it) {
        // пробелы-разделители разрядов в ответе ученика прощаем («-2 147 483 648» = «-2147483648»),
        // но только если в правильном ответе таких групп нет (иначе «1 200» могло быть двумя числами)
        const exp = normOut(it.answer), thou = /(d)[   ](?=d{3}(?!d))/g;
        const t = box.querySelector(".ck-out");
        const mine = thou.test(exp) ? normOut(t.value) : normOut(t.value).replace(thou, "$1");
        const ok = mine === exp;
        t.classList.add(ok ? "right" : "wrong"); t.readOnly = true;
        if (!ok) box.insertAdjacentHTML("beforeend", `<div class="ck-expected"><small>Правильный вывод:</small><pre>${esc(it.answer)}</pre></div>`);
        return ok;
      },
      bug(box, it, st) {
        const ans = new Set(it.answer);
        box.querySelectorAll(".ck-line").forEach((b) => { const k = +b.dataset.k; b.classList.toggle("right", ans.has(k)); b.classList.toggle("wrong", st.picks.has(k) && !ans.has(k)); });
        return ans.size === st.picks.size && [...ans].every((k) => st.picks.has(k));
      },
      order(box, it, st) {
        st.list.classList.add("locked");
        // accept — другие верные порядки (например, независимые объявления переменных можно поменять местами)
        const cur = st.items.map((x) => x.t);
        const alt = (it.accept || []).find((a) => a.length === cur.length && a.every((t, i) => t === cur[i]));
        const ok = alt ? (markOrder(st.list, st.items, alt), true) : markOrder(st.list, st.items, it.lines) === 0;
        if (!ok) box.insertAdjacentHTML("beforeend", `<div class="ck-expected"><small>Правильный порядок:</small><pre>${esc(it.lines.join("\n"))}</pre></div>`);
        return ok;
      },
      build(box, it, st) {
        st.locked = true;
        const plain = (h) => h.replace(/<[^>]+>/g, "").replace(/\s+/g, " ").trim();
        const mine = st.picked.map((x) => plain(x.t));
        const variants = [it.answer, ...(it.accept || [])].map((v) => v.map(plain));
        const ok = variants.some((v) => v.length === mine.length && v.every((t, i) => t === mine[i]));
        box.querySelector(".bd-answer").classList.add(ok ? "right" : "wrong");
        box.querySelectorAll(".bd-pool .bd-chip").forEach((b) => { b.disabled = true; });
        if (!ok) box.insertAdjacentHTML("beforeend", `<div class="ck-expected"><small>Правильно:</small><p>${it.answer.join(" ")}</p></div>`);
        return ok;
      }
    };
    start();
  };

  // =====================================================================
  // 11. «Скрин» IntelliJ IDEA: точная копия окна (новый интерфейс, тёмная тема)
  //     с кодом, деревом проекта, консолью Run, контекстным меню / диалогом и выносками-номерами.
  //     Не интерактив — это картинка, собранная из данных, чтобы показывать любое место IDEA.
  // =====================================================================
  W.ide = function (el) {
    const d = readData(el) || {};
    const lm = ((document.querySelector("main[data-lesson]") || {}).dataset || {}).lesson || "";
    const mm = lm.match(/^m(\d\d)/);
    const defPath = mm && mm[1] !== "00" ? "module-" + mm[1] : "module-01";
    const lines = (d.code || "").split("\n");
    const gutterRun = new Set(d.gutterRun || []);
    const errors = new Set(d.errorLines || []);
    const dim = new Set(d.dimLines || []); // серые строки — как неиспользуемый импорт в IDEA
    const icon = { folder: "📁", src: "📂", class: "Ⓒ", file: "📄", lib: "📚", out: "📁", test: "Ⓣ" };
    const tree = d.tree === false ? null : (d.tree || [
      { name: d.project || "workspace", type: "folder", depth: 0, open: true },
      { name: defPath, type: "src", depth: 1, open: true },
      { name: "FixMe", type: "class", depth: 2 }
    ]);
    const treeHtml = tree ? `<div class="idea-tree" data-t="tree"><div class="idea-pane-h">Project <span>⌄</span></div>${tree.map((n) => `
        <div class="idea-node ${n.selected ? "sel" : ""} t-${n.type}" data-t="tree-${esc(n.name)}" style="padding-left:${10 + n.depth * 16}px">
          <span class="arr">${n.type === "class" || n.type === "file" || n.type === "test" ? "" : n.open ? "⌄" : "›"}</span><span class="ic">${icon[n.type] || "📄"}</span><span class="nm">${esc(n.name)}</span>${n.note ? `<span class="note">${esc(n.note)}</span>` : ""}
        </div>`).join("")}</div>` : "";
    // точечные ошибки: { line, text | col+len, msg, tip, fix } — красная волна под куском строки + всплывашка как в IDEA
    const errs = (d.errors || []).map((e) => Object.assign({ kind: "error" }, e));
    (d.warnings || []).forEach((w) => errs.push(Object.assign({ kind: "warn" }, w)));
    (d.weak || []).forEach((w) => errs.push(Object.assign({ kind: "weak" }, w))); // слабое предупреждение
    (d.unused || []).forEach((w) => errs.push(Object.assign({ kind: "unused" }, w))); // серое имя: не используется
    const bps = new Set(d.breakpoints || []);
    const hl = (s) => JC.highlightJava(s);
    function lineHtml(l, n) {
      const list = errs.filter((x) => x.line === n);
      if (!list.length) return hl(l) || " ";
      // каждая ошибка строки — свой кусок с волной; цели err-N, err-N-2, err-N-3…
      const spans = list.map((e, k) => {
        let from, to;
        if (e.text !== undefined) { from = l.indexOf(e.text); to = from + e.text.length; }
        else { from = (e.col || 1) - 1; to = from + (e.len || 1); }
        if (from < 0) { from = l.search(/\S/); to = l.length; }
        return { from, to, e, key: k ? `err-${n}-${k + 1}` : `err-${n}` };
      }).sort((a, b) => a.from - b.from);
      const maxTo = Math.max(...spans.map((x) => x.to));
      if (maxTo > l.length) l = l + " ".repeat(maxTo - l.length);
      let html = "", pos = 0;
      spans.forEach((sp) => {
        const from = Math.max(sp.from, pos);
        if (sp.to <= from) return;
        html += hl(l.slice(pos, from)) + `<span class="errw ${sp.e.kind}" data-t="${sp.key}">${hl(l.slice(from, sp.to)) || " "}</span>`;
        pos = sp.to;
      });
      return html + hl(l.slice(pos));
    }
    const codeHtml = d.code === undefined ? (d.welcome ? "" : `<div class="idea-empty">Открой файл двойным щелчком в дереве проекта</div>`) : lines.map((l, i) => {
      const n = i + 1;
      return `<div class="idea-line ${d.caretLine === n ? "caret" : ""} ${errors.has(n) ? "err" : ""} ${dim.has(n) ? "dim" : ""} ${d.execLine === n ? "exec" : ""} ${(d.selLines || []).includes(n) ? "selln" : ""}" data-t="line-${n}">
        <span class="g" data-t="gutter-${n}">${d.gutterIcons && d.gutterIcons[n] ? `<b class="gi" data-t="gicon-${n}">${esc(d.gutterIcons[n])}</b>` : ""}${bps.has(n) ? `<b class="bp" data-t="bp-${n}">●</b>` : gutterRun.has(n) ? `<b class="run">▶</b>` : ""}<i>${n + (d.firstLine ? d.firstLine - 1 : 0)}</i></span><code>${lineHtml(l, n)}</code>${d.execLine === n && d.inlineVals ? `<span class="inl">${esc(d.inlineVals)}</span>` : d.vals && d.vals[n] ? `<span class="inl">${esc(d.vals[n])}</span>` : ""}</div>`;
    }).join("");
    const nErr = errs.filter((e) => e.kind === "error").length, nWarn = errs.filter((e) => e.kind !== "error").length;
    const problemsHtml = d.code !== undefined && (errs.length || d.problems) ? `<span class="idea-problems" data-t="problems">${nErr ? `<b class="e">❗${nErr}</b>` : ""}${nWarn ? `<b class="w">⚠${nWarn}</b>` : ""}${!errs.length ? `<b class="ok">✓</b>` : ""} <i>⌃⌄</i></span>` : "";
    const tipErr = errs.find((e) => e.tip);
    const tipHtml = tipErr ? `<div class="idea-tip ${tipErr.kind}" data-t="tip"><div class="tm">${tipErr.msg ? esc(tipErr.msg) : ""}</div>${tipErr.fix !== false ? `<div class="tf"><span>${esc(tipErr.fix || "More actions…")}</span><i>Alt+Enter</i></div>` : ""}</div>` : "";
    const tabs = d.code === undefined ? "" : `<div class="idea-tabs">${(d.tabs || [d.file || "Main.java"]).map((t, i) => `<span class="tab ${i === 0 ? "on" : ""}" data-t="tab">${t.endsWith(".java") ? "Ⓒ " : ""}${esc(t)} <i>×</i></span>`).join("")}</div>`;
    // ссылки вида (Main.java:5) в stack trace — синие, кликабельные в IDEA; data-t="link-1", "link-2"…
    let linkN = 0;
    const linkify = (s) => esc(s).replace(/\(([\w$.]+\.java:\d+)\)/g, (m, x) => `(<u class="lnk" data-t="link-${++linkN}">${x}</u>)`);
    let errN = 0, outN = 0, inN = 0;
    const consoleHtml = d.console ? `<div class="idea-run" data-t="console">
        <div class="idea-run-h"><span class="rt">${esc(d.console.tool || "Run")}</span><span class="tab on">${esc(d.console.title || "Main")} <i>×</i></span></div>
        <div class="idea-run-b"><div class="idea-run-tools"><span>↻</span><span class="stop ${d.console.running ? "on" : ""}" data-t="stop">■</span><span>⇣</span></div>
          <div class="idea-run-out">${(() => {
            const rows = [];
            (d.console.lines || []).forEach((l) => {
              const t = l.t || "out";
              const key = t === "exit" ? "exit" : t === "out" ? (outN++ ? "output-" + outN : "output") : t === "in" ? (inN++ ? "input-" + inN : "input") : t === "err" ? (errN++ ? "error-" + errN : "error") : "cmd";
              const body = (t === "err" ? linkify(l.s) : esc(l.s)) || "\u00a0";
              // inline: продолжение предыдущей строки (print("Возраст: ") + ввод 25 на той же строке)
              if (l.inline && rows.length) rows[rows.length - 1] = rows[rows.length - 1].replace(/<\/div>$/, `<span class="o-${t}" data-t="${key}">${body}</span></div>`);
              else rows.push(`<div class="o-${t}" data-t="${key}">${body}</div>`);
            });
            return rows.join("");
          })()}${d.console.cursor ? `<div class="o-in cur">▌</div>` : ""}</div></div></div>` : "";
    // панель Build: дерево ошибок слева, вывод javac справа
    const bd = d.build;
    const buildHtml = bd ? (() => {
      const be = bd.errors || [];
      const ok = !be.length;
      const tree = [`<div class="bi ${ok ? "ok" : "fail"}" data-t="build-result"><b>${ok ? "✓" : "✖"}</b>${esc(bd.result || (ok ? "Build completed successfully" : `${(bd.file || "Main.java").replace(".java", "")}: build failed`))} <i>${esc(bd.time || "1 sec, 214 ms")}</i></div>`];
      if (!ok) {
        tree.push(`<div class="bi file" data-t="build-file" style="padding-left:24px"><b class="c">Ⓒ</b>${esc(bd.file || "Main.java")} <i>${esc(bd.path || (d.statusPath || defPath))} · ${be.length} ${be.length === 1 ? "error" : "errors"}</i></div>`);
        be.forEach((e, i) => tree.push(`<div class="bi err ${i === (bd.sel || 0) ? "sel" : ""}" data-t="build-err-${i + 1}" style="padding-left:40px"><b>✖</b>${esc(e.msg)} <i>:${e.line}</i></div>`));
      }
      const se = be[bd.sel || 0];
      const out = bd.out || (se ? [`${bd.fullPath || "C:\\Users\\Misha\\java-course\\workspace\\" + (d.statusPath || defPath) + "\\" + (bd.file || "Main.java")}:${se.line}:${se.col || 1}`, `java: ${se.msg}`] : []);
      return `<div class="idea-run idea-build" data-t="build">
        <div class="idea-run-h"><span class="rt">Build</span><span class="tab on">Build Output <i>×</i></span></div>
        <div class="idea-build-b"><div class="idea-build-tree" data-t="build-tree">${tree.join("")}</div>
          <div class="idea-run-out" data-t="build-out">${out.map((s, i) => `<div class="${i === 0 ? "o-path" : "o-err"}">${esc(s)}</div>`).join("")}</div></div></div>`;
    })() : "";
    // результаты тестов JUnit в панели Run: дерево ✓/✗ слева, итог и вывод справа
    const ts = d.tests;
    const testsHtml = ts ? (() => {
      const items = ts.tree || [];
      // считаем только «листья» — сами запуски тестов, а не классы и группы
      const leaf = (x, i) => !(items[i + 1] && (items[i + 1].depth || 0) > (x.depth || 0));
      const failed = items.filter((x, i) => x.status === "fail" && leaf(x, i)).length;
      const passed = items.filter((x, i) => x.status === "pass" && leaf(x, i)).length;
      const head = ts.summary || (failed ? `Tests failed: ${failed}, passed: ${passed} of ${failed + passed} tests – ${ts.time || "48 ms"}` : `Tests passed: ${passed} of ${passed} ${passed === 1 ? "test" : "tests"} – ${ts.time || "48 ms"}`);
      return `<div class="idea-run idea-tests" data-t="tests">
        <div class="idea-run-h"><span class="rt">Run</span><span class="tab on">${esc(ts.title || "Tests")} <i>×</i></span></div>
        <div class="idea-tests-b"><div class="idea-build-tree" data-t="test-tree">${items.map((x, i) => `<div class="bi ${x.status === "fail" ? "fail" : "ok"} ${x.sel ? "sel" : ""}" data-t="test-${i + 1}" style="padding-left:${10 + (x.depth || 0) * 16}px"><b>${x.status === "fail" ? "✗" : x.status === "skip" ? "⊘" : "✓"}</b>${esc(x.name)} <i>${esc(x.time || "")}</i></div>`).join("")}</div>
          <div class="idea-run-out" data-t="test-out"><div class="${failed ? "o-tfail" : "o-tok"}" data-t="test-summary">${failed ? "✗" : "✓"} ${esc(head)}</div>${(ts.out || []).map((l) => { const t = typeof l === "string" ? "out" : l.t || "out"; const str = typeof l === "string" ? l : l.s; return `<div class="o-${t}">${t === "err" ? linkify(str) : esc(str)}</div>`; }).join("")}</div></div></div>`;
    })() : "";
    // отладчик: кадры (Frames) слева, переменные справа, кнопки шагов сверху
    const dg0 = d.debug;
    const debugHtml = dg0 ? `<div class="idea-run idea-debug" data-t="debug">
        <div class="idea-run-h"><span class="rt">Debug</span><span class="tab on">${esc(dg0.title || "Main")} <i>×</i></span>
          <span class="dbg-btns"><span data-t="resume" title="Resume F9">▶</span><span data-t="stop-dbg" class="r">■</span><span class="sepv"></span><span data-t="step-over" title="Step Over F8">↷</span><span data-t="step-into" title="Step Into F7">↘</span><span data-t="step-out" title="Step Out Shift+F8">↗</span></span></div>
        <div class="idea-debug-b">
          <div class="idea-frames" data-t="frames"><div class="fh">Frames <i>"main"@1 in group "main": RUNNING</i></div>${(dg0.frames || []).map((f, i) => `<div class="fr ${i === (dg0.sel || 0) ? "sel" : ""}" data-t="frame-${i + 1}">${esc(f)}</div>`).join("")}</div>
          <div class="idea-vars" data-t="vars"><div class="fh">Variables</div>${(dg0.vars || []).map((v) => `<div class="vr" data-t="var-${esc(v.name)}" style="padding-left:${10 + (v.depth || 0) * 16}px"><span class="vi">${v.open === undefined ? "◆" : v.open ? "⌄" : "›"}</span><b>${esc(v.name)}</b> = <span class="vv">${v.type ? `<i>{${esc(v.type)}}</i> ` : ""}${esc(v.value)}</span></div>`).join("")}</div>
        </div></div>` : "";
    // контекстное меню / всплывающее окно / диалог
    let overlay = "";
    if (d.menu) {
      const item = (it, hl) => { if (it === "-") return `<div class="sep"></div>`; const sub = it.endsWith(" ›"); const [lab, key] = it.replace(" ›", "").split("	"); return `<div class="mi ${lab === hl || it.replace(" ›", "") === hl ? "hl" : ""}"><span>${esc(lab)}</span>${key ? `<i class="key">${esc(key)}</i>` : ""}${sub ? `<span>›</span>` : ""}</div>`; };
      overlay += `<div class="idea-menu" data-t="menu" data-at="${esc(d.menu.at || "")}">${d.menu.title ? `<div class="mt">${esc(d.menu.title)}</div>` : ""}${d.menu.items.map((it) => item(it, d.menu.hl)).join("")}</div>`;
      if (d.menu.sub) overlay += `<div class="idea-menu sub" data-t="submenu">${d.menu.sub.items.map((it) => item(it, d.menu.sub.hl)).join("")}</div>`;
    }
    if (d.popup) {
      overlay += `<div class="idea-popup" data-t="popup"><div class="ph">${esc(d.popup.title)}</div>
        <div class="pin">${esc(d.popup.input || "")}<span class="cur">▌</span></div>
        ${(d.popup.list || []).map((x) => `<div class="mi ${x === d.popup.hl ? "hl" : ""}"><span class="ic">${esc(d.popup.icon || "Ⓒ")}</span>${esc(x)}</div>`).join("")}</div>`;
    }
    if (d.dialog) {
      const dg = d.dialog;
      overlay += `<div class="idea-dialog" data-t="dialog"><div class="dh">${esc(dg.title)}<span>✕</span></div>
        <div class="db">${dg.side ? `<div class="dside">${dg.side.map((s) => `<div class="${s === dg.sideHl ? "hl" : ""}" data-t="side-${esc(s)}">${esc(s)}</div>`).join("")}</div>` : ""}
        <div class="dmain">${(dg.rows || []).map((r) => `<div class="drow" data-t="field-${esc(r.label)}"><label>${esc(r.label)}</label><span class="${r.type || "input"} ${r.hl ? "hl" : ""}">${esc(r.value || "")}${r.type === "select" ? " ⌄" : ""}</span></div>`).join("")}
        ${dg.note ? `<div class="dnote">${esc(dg.note)}</div>` : ""}</div></div>
        <div class="df">${(dg.buttons || ["Cancel", "OK"]).map((b) => `<span class="btn ${b === (dg.primary || "OK") ? "pri" : ""}" data-t="btn-${esc(b)}">${esc(b)}</span>`).join("")}</div></div>`;
    }
    if (d.welcome) {
      overlay += `<div class="idea-welcome"><div class="wl"><b>IntelliJ IDEA</b><div class="on">Projects</div><div>Customize</div><div>Plugins</div><div>Learn</div></div>
        <div class="wr"><h5>Welcome to IntelliJ IDEA</h5><div class="wb">${["＋|New Project", "📂|Open", "⤓|Clone Repository"].map((x) => { const [i, t] = x.split("|"); return `<span data-t="welcome-${t}" class="${t === d.welcome ? "hl" : ""}"><i>${i}</i>${t}</span>`; }).join("")}</div></div></div>`;
    }
    el.classList.add("idea-shot");
    el.innerHTML = `<div class="idea-scroll"><div class="idea ${d.welcome ? "is-welcome" : ""}">
      <div class="idea-top"><span class="hb" data-t="main-menu">☰</span><span class="pw"><b>${esc((d.project || "workspace")[0].toUpperCase())}</b>${esc(d.project || "workspace")} ⌄</span>${d.branch ? `<span class="br" data-t="branch">⑂ ${esc(d.branch)} ⌄</span>` : ""}
        <span class="sp"></span><span class="cfg" data-t="config">${d.runConfig ? `Ⓒ ${esc(d.runConfig)} ⌄` : "Current File ⌄"}</span>
        <span class="ib run" data-t="run-btn">▶</span><span class="ib" data-t="debug-btn">🐞</span><span class="ib">⋮</span><span class="ib">🔍</span><span class="ib">⚙</span></div>
      <div class="idea-body"><div class="idea-strip"><span class="on" data-t="project-btn">📁</span><span>⑂</span><span>⋯</span><span class="b" data-t="run-tool">▷</span><span class="b">⌨</span><span class="b">ⓘ</span></div>
        <div class="idea-cols ${tree ? "" : "no-tree"}">${treeHtml}<div class="idea-editor" data-t="editor">${tabs}${d.banner ? `<div class="idea-banner" data-t="banner"><span>${esc(d.banner.text || d.banner)}</span>${d.banner.action ? `<u data-t="banner-action">${esc(d.banner.action)}</u>` : ""}</div>` : ""}${problemsHtml}<div class="idea-code" ${d.codeHeight ? `style="max-height:${+d.codeHeight}px;overflow-y:auto"` : ""}>${codeHtml}</div></div></div></div>
      ${buildHtml}${debugHtml}${testsHtml}${consoleHtml}
      <div class="idea-status"><span data-t="status-path">${esc(d.status || `${d.project || "workspace"} › ${d.statusPath || defPath}`)}</span><span class="sp"></span><span data-t="status-pos">${d.caretLine ? d.caretLine + ":1" : ""}</span><span data-t="status-enc">UTF-8</span><span>4 spaces</span></div>
      ${overlay}${tipHtml}<div class="idea-marks"></div></div></div>
      ${d.marks && d.marks.length ? `<ol class="idea-legend">${d.marks.map((m) => `<li><b>${m.n}</b><span>${m.text}</span></li>`).join("")}</ol>` : ""}
      ${d.caption ? `<div class="idea-cap">${d.caption}</div>` : ""}`;
    const win = el.querySelector(".idea");
    const inlineDone = new Set();
    (d.marks || []).forEach((m, i) => {
      const t = win.querySelector(`[data-t="${CSS.escape(m.target)}"]`);
      if (!t) return;
      const mk = () => { const b = document.createElement("span"); b.className = "idea-mark mk-inl"; b.textContent = m.n; return b; };
      const tg = m.target;
      if (/^(line|err)-\d+/.test(tg)) ((t.closest(".idea-line") || t).querySelector("code") || t).appendChild(mk()); // в конец строки кода
      else if (/^(output|error|input|exit|cmd|test-summary)(-\d+)?$/.test(tg)) t.appendChild(mk()); // в конец строки консоли
      else if (/^link-\d+$/.test(tg)) t.after(mk());
      else if (/^(tree-|build-result$|build-file$|build-err-|test-\d|frame-|var-|side-)/.test(tg)) t.prepend(mk()); // перед пунктом списка
      else return;
      inlineDone.add(i);
    });
    const warned = new Set();
    function place() {
      const wr = win.getBoundingClientRect();
      // контекстное меню — рядом с целью (строкой дерева или полем кода)
      const menu = win.querySelector(".idea-menu:not(.sub)");
      if (menu && d.menu.at) {
        const t = win.querySelector(`[data-t="${CSS.escape(d.menu.at)}"]`);
        // главное меню ☰ — ровно под кнопкой, контекстное — чуть правее строки
        const dx = d.menu.at === "main-menu" ? 0 : 40;
        if (t) { const r = t.getBoundingClientRect(); menu.style.left = Math.min(r.left - wr.left + dx, wr.width - menu.offsetWidth - 8) + "px"; menu.style.top = (r.bottom - wr.top - 4) + "px"; }
      }
      const sub = win.querySelector(".idea-menu.sub");
      if (menu && sub) {
        const hl = menu.querySelector(".mi.hl") || menu;
        const r = hl.getBoundingClientRect();
        sub.style.left = Math.min(menu.offsetLeft + menu.offsetWidth - 4, wr.width - sub.offsetWidth - 4) + "px";
        sub.style.top = (r.top - wr.top - 6) + "px";
      }
      const tip = win.querySelector(".idea-tip");
      if (tip) {
        const t = win.querySelector(`[data-t="err-${tipErr.line}"]`);
        if (t) {
          const r = t.getBoundingClientRect();
          tip.style.left = Math.max(8, Math.min(r.left - wr.left, wr.width - tip.offsetWidth - 8)) + "px";
          let top = r.bottom - wr.top + 4;
          const busy = new Set(errs.filter((e) => e.line !== tipErr.line).map((e) => e.line));
          (d.marks || []).forEach((m) => { const k = /^(?:line|err)-(\d+)/.exec(m.target); if (k && +k[1] !== tipErr.line) busy.add(+k[1]); });
          const hidden = [...busy].some((n) => { const ln = win.querySelector(`[data-t="line-${n}"]`); if (!ln) return false; const lr = ln.getBoundingClientRect(); return lr.top - wr.top < top + tip.offsetHeight && lr.bottom - wr.top > top; });
          const above = r.top - wr.top - tip.offsetHeight - 4;
          if (hidden && above > 44) top = above;
          tip.style.top = top + "px";
        }
      }
      // всплывающее не влезает в окно — вытягиваем окно вниз (а не наезжаем на цель)
      let need = 0;
      win.querySelectorAll(".idea-menu, .idea-tip, .idea-popup, .idea-dialog").forEach((o) => { need = Math.max(need, o.offsetTop + o.offsetHeight); });
      const body = win.querySelector(".idea-body");
      const lack = need + 34 - win.offsetHeight;
      if (lack > 0) { body.style.minHeight = (body.offsetHeight + lack) + "px"; }
      const wr2 = win.getBoundingClientRect();
      const marks = win.querySelector(".idea-marks");
      marks.innerHTML = "";
      (d.marks || []).forEach((m, i) => {
        const t = win.querySelector(`[data-t="${CSS.escape(m.target)}"]`);
        if (!t) { if (!warned.has(m.target)) { warned.add(m.target); console.warn("ide: нет цели для выноски", m.target, "в", el.id); } return; }
        if (inlineDone.has(i)) {
          if (m.box) { const r = t.getBoundingClientRect(); const bx = document.createElement("span"); bx.className = "idea-box"; Object.assign(bx.style, { left: r.left - wr2.left - 3 + "px", top: r.top - wr2.top - 3 + "px", width: r.width + 6 + "px", height: r.height + 6 + "px" }); marks.appendChild(bx); }
          return;
        }
        // берём границы самого текста, а не всей строки-блока: номер встаёт сразу за словами
        let r = t.getBoundingClientRect();
        const rg = document.createRange(); rg.selectNodeContents(/^line-\d+$/.test(m.target) ? t.querySelector("code") : t);
        const tr = rg.getBoundingClientRect();
        if (tr.width > 0 && tr.height > 0 && !/^(console|editor|tree|build|debug|frames|vars|dialog|popup|menu|submenu|tip)$/.test(m.target)) r = tr;
        const b = document.createElement("span");
        b.className = "idea-mark"; b.textContent = m.n;
        const right = m.side !== "left" && !/^(gutter|bp|gicon)-/.test(m.target);
        b.style.left = Math.max(2, Math.min(wr2.width - 26, (right ? r.right - wr2.left + 4 : r.left - wr2.left - 28))) + "px";
        b.style.top = Math.max(2, r.top - wr2.top + r.height / 2 - 12) + "px";
        if (m.side === "top" || m.side === "bottom") { // над / под целью, по её центру — не закрывает код справа
          b.style.left = Math.max(2, Math.min(wr2.width - 26, r.left - wr2.left + r.width / 2 - 12)) + "px";
          b.style.top = Math.max(2, m.side === "top" ? r.top - wr2.top - 24 : r.bottom - wr2.top + 2) + "px";
        }
        marks.appendChild(b);
        if (m.box) { const bx = document.createElement("span"); bx.className = "idea-box"; Object.assign(bx.style, { left: r.left - wr2.left - 3 + "px", top: r.top - wr2.top - 3 + "px", width: r.width + 6 + "px", height: r.height + 6 + "px" }); marks.appendChild(bx); }
      });
    }
    // сразу (если элемент уже на странице), потом ещё раз на кадре и при каждом изменении размера
    try { if (win.isConnected) place(); } catch (e) { console.warn("ide: place", e); }
    requestAnimationFrame(place);
    new ResizeObserver(place).observe(win);
  };

  // =====================================================================
  // 13. «Раннер»: Джавик бежит, перепрыгивает жуков и пригибается под летучими ошибками.
  //     runnerhand — играешь сам (пробел/↑ — прыжок, ↓ — присесть), runnercode — пишешь decide на Java,
  //     сервер компилирует и прогоняет забег (правила — tasks/runner/engine/RunnerGame.java, здесь их копия для ручной игры).
  // =====================================================================
  const RUN_JUMP = 4, RUN_DUCK = 3, RUN_VIEW = 12;
  /**
   * Экран «Раннера» на canvas: плавный бег, прыжок по дуге, присед, параллакс-фон, пыль, звёздочки при ударе.
   * show(x, state, mood, opts): x — клетка после хода, state — run/jump/duck/crash, opts.phase — номер хода
   * внутри прыжка/приседа (0…), opts.ms — сколько длится анимация хода, opts.instant — без анимации.
   */
  function runnerView(host) {
    host.innerHTML = `<div class="rn-track"><canvas class="rn-canvas"></canvas></div>`;
    const track = host.querySelector(".rn-track"), cv = host.querySelector("canvas"), ctx = cv.getContext("2d");
    const COLS = 10, AT = 2;                    // сколько клеток видно, где стоит Джавик (в клетках от левого края)
    let W = 0, H = 0, cell = 40, groundY = 0, dpr = 1;
    let obs = [], len = 0;
    // анимация: из какого состояния в какое
    let from = { x: 0 }, to = { x: 0, state: "run", phase: 0 }, t0 = 0, dur = 1, mood = "happy";
    let dust = [], stars = [], flash = 0, finished = false;
    const imgs = {};
    function mascotImg(m) {
      if (!imgs[m]) { const i = new Image(); i.src = "data:image/svg+xml;charset=utf-8," + encodeURIComponent(JC.mascot(m).replace("<svg", '<svg xmlns="http://www.w3.org/2000/svg"')); imgs[m] = i; }
      return imgs[m];
    }
    ["happy", "oops", "wow"].forEach(mascotImg);
    function size() {
      dpr = window.devicePixelRatio || 1;
      W = track.clientWidth; cell = Math.max(26, W / COLS); H = Math.round(cell * 3.4);
      cv.width = Math.round(W * dpr); cv.height = Math.round(H * dpr); cv.style.height = H + "px";
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0); groundY = H - cell * 0.45;
    }
    size(); new ResizeObserver(size).observe(track);
    const ease = (k) => k < 0.5 ? 2 * k * k : 1 - Math.pow(-2 * k + 2, 2) / 2;
    // высота прыжка: непрерывная дуга через все ходы прыжка (4 хода — как в движке)
    const jumpH = (phase, k) => Math.sin(Math.PI * Math.min(1, (phase + k) / RUN_JUMP)) * cell * 1.25;
    // присед: быстро вниз в начале, быстро вверх в конце
    const duckK = (phase, k) => { const p = (phase + k) / RUN_DUCK; return p < 0.12 ? p / 0.12 : p > 0.88 ? (1 - p) / 0.12 : 1; };

    function drawBg(camX, now) {
      const sky = ctx.createLinearGradient(0, 0, 0, H);
      sky.addColorStop(0, "#1b2150"); sky.addColorStop(1, "#3a3f7a");
      ctx.fillStyle = sky; ctx.fillRect(0, 0, W, H);
      // звёзды и облака (дальний план движется медленно)
      ctx.fillStyle = "rgba(255,255,255,.55)";
      for (let i = 0; i < 18; i++) { const sx = ((i * 97 - camX * cell * 0.08) % W + W) % W, sy = (i * 53) % (H * 0.45); ctx.fillRect(sx, sy, 2, 2); }
      ctx.fillStyle = "rgba(255,255,255,.12)";
      for (let i = 0; i < 4; i++) { const cx = ((i * 260 - camX * cell * 0.2) % (W + 200) + W + 200) % (W + 200) - 100, cy = H * 0.18 + (i % 2) * 18; ctx.beginPath(); ctx.ellipse(cx, cy, 46, 12, 0, 0, 7); ctx.ellipse(cx + 30, cy - 8, 30, 12, 0, 0, 7); ctx.fill(); }
      // холмы (средний план)
      ctx.fillStyle = "#2b3170";
      ctx.beginPath(); ctx.moveTo(0, groundY);
      for (let px = 0; px <= W; px += 8) { const wx = px + camX * cell * 0.45; ctx.lineTo(px, groundY - 18 - Math.sin(wx / 90) * 14 - Math.sin(wx / 37) * 6); }
      ctx.lineTo(W, groundY); ctx.fill();
      // земля
      ctx.fillStyle = "#5a4326"; ctx.fillRect(0, groundY, W, H - groundY);
      ctx.fillStyle = "#6f5531";
      const off = (camX * cell) % 28;
      for (let px = -off; px < W; px += 28) ctx.fillRect(px, groundY + 6, 14, 4);
      ctx.fillStyle = "#3d8b3d"; ctx.fillRect(0, groundY - 3, W, 4);
    }
    function drawObstacles(camX, now) {
      const base = (wx) => (wx - camX + AT) * cell;
      ctx.textAlign = "center"; ctx.textBaseline = "middle";
      for (const o of obs) {
        const sx = base(o[0]);
        if (sx > W + cell * 2 || sx + o[1] * cell < -cell) continue;
        if (o[2] === 0) {
          for (let k = 0; k < o[1]; k++) {
            const wig = Math.sin(now / 120 + k + o[0]) * 2;
            ctx.font = `${cell * 0.78}px serif`;
            ctx.fillText("🐛", sx + (k + 0.5) * cell + wig, groundY - cell * 0.38);
          }
        } else {
          const bob = Math.sin(now / 160 + o[0]) * cell * 0.08;
          ctx.save(); ctx.translate(sx + cell * 0.5, groundY - cell * 1.45 + bob);
          ctx.scale(1, 0.85 + Math.abs(Math.sin(now / 90)) * 0.25); // машет крыльями
          ctx.font = `${cell * 0.8}px serif`; ctx.fillText("🦇", 0, 0); ctx.restore();
        }
      }
      const fx = base(len);
      if (fx < W + cell) { ctx.fillStyle = "rgba(255,255,255,.35)"; ctx.fillRect(fx, groundY - cell * 2.6, 3, cell * 2.6); ctx.font = `${cell * 0.9}px serif`; ctx.fillText("🏁", fx + cell * 0.4, groundY - cell * 2.3); }
    }
    function drawJavik(x, lift, squash, run, now) {
      const cx = AT * cell + cell * 0.5, bottom = groundY - lift;
      // тень
      ctx.fillStyle = `rgba(0,0,0,${0.35 - Math.min(0.25, lift / cell * 0.18)})`;
      ctx.beginPath(); ctx.ellipse(cx, groundY + 2, cell * (0.42 - Math.min(0.2, lift / cell * 0.12)), cell * 0.09, 0, 0, 7); ctx.fill();
      const w = cell * 1.2 * (1 + (1 - squash) * 0.35), h = cell * 1.35 * squash;
      const bob = run ? Math.abs(Math.sin(now / 90)) * cell * 0.07 : 0;
      // ножки (бег — перебирает, в воздухе — поджаты)
      ctx.strokeStyle = "#e9e6ff"; ctx.lineWidth = Math.max(2, cell * 0.07); ctx.lineCap = "round";
      const legY = bottom - bob, swing = run ? Math.sin(now / 70) * cell * 0.16 : 0, tuck = lift > 4 ? cell * 0.08 : 0;
      if (squash > 0.75) {
        ctx.beginPath();
        ctx.moveTo(cx - cell * 0.15, legY - cell * 0.18); ctx.lineTo(cx - cell * 0.15 + swing, legY - tuck);
        ctx.moveTo(cx + cell * 0.15, legY - cell * 0.18); ctx.lineTo(cx + cell * 0.15 - swing, legY - tuck);
        ctx.stroke();
      }
      const img = mascotImg(mood);
      const top = bottom - bob - (squash > 0.75 ? cell * 0.14 : 0) - h;
      if (img.complete && img.naturalWidth) ctx.drawImage(img, cx - w / 2, top, w, h);
      else { ctx.fillStyle = "#ff7a1a"; ctx.fillRect(cx - w / 2, top, w, h); }
    }
    function frame(now) {
      const k = Math.min(1, (now - t0) / dur), e = ease(k);
      const camX = from.x + (to.x - from.x) * (to.state === "crash" ? Math.min(1, e * 1.4) * 0.85 : k);
      const shake = to.state === "crash" && k < 1 ? Math.sin(now / 25) * cell * 0.08 * (1 - k) : 0;
      ctx.save(); ctx.translate(shake, 0);
      drawBg(camX, now); drawObstacles(camX, now);
      let lift = 0, squash = 1;
      if (to.state === "jump") lift = jumpH(to.phase, k);
      if (to.state === "duck") squash = 1 - 0.45 * duckK(to.phase, k);
      if (to.state === "crash" && to.was === "jump") lift = jumpH(to.phase, Math.min(k, 0.5));
      // приземление — пружинка и пыль
      if (to.landed && k < 0.35) squash = Math.min(squash, 1 - Math.sin(k / 0.35 * Math.PI) * 0.18);
      drawJavik(camX, lift, squash, to.state === "run" || to.state === "duck", now);
      track.dataset.lift = Math.round(lift); track.dataset.state = to.state; // для проверки анимации
      // частицы
      dust = dust.filter((p) => (p.life -= 1 / 40) > 0);
      for (const p of dust) { p.x += p.vx; p.y += p.vy; ctx.fillStyle = `rgba(230,210,170,${p.life * 0.7})`; ctx.beginPath(); ctx.arc(p.x, p.y, p.r * (1.5 - p.life), 0, 7); ctx.fill(); }
      stars = stars.filter((p) => (p.life -= 1 / 60) > 0);
      ctx.font = `${cell * 0.4}px serif`;
      for (const p of stars) { p.a += 0.08; ctx.fillText("⭐", AT * cell + cell * 0.5 + Math.cos(p.a) * cell * 0.55, groundY - cell * 1.6 + Math.sin(p.a) * cell * 0.25 - lift); }
      if (flash > 0) { ctx.fillStyle = `rgba(255,80,80,${flash})`; ctx.fillRect(0, 0, W, H); flash -= 0.04; }
      ctx.restore();
      requestAnimationFrame(frame);
    }
    requestAnimationFrame(frame);
    function puff() { const cx = AT * cell + cell * 0.5; for (let i = 0; i < 8; i++) dust.push({ x: cx + (Math.random() - 0.5) * cell * 0.6, y: groundY - 2, vx: (Math.random() - 0.7) * 1.6, vy: -Math.random() * 1.1, r: 2 + Math.random() * 3, life: 1 }); }
    return {
      setTrack(obstacles, length) { obs = obstacles; len = length; finished = false; },
      show(x, state, m, opts = {}) {
        const now = performance.now();
        const prev = to;
        from = { x: opts.instant ? x : (prev.x !== undefined ? prev.x : x) };
        if (opts.instant) from.x = x;
        to = { x, state, phase: opts.phase || 0, was: opts.was, landed: prev.state === "jump" && state !== "jump" && !opts.instant };
        t0 = now; dur = opts.instant ? 1 : (opts.ms || 300);
        mood = m || "happy";
        if (to.landed) puff();
        if (state === "crash" && !opts.instant) { flash = 0.35; stars = [0, 2.1, 4.2].map((a) => ({ a, life: 1.6 })); }
        if (state === "jump" && (opts.phase || 0) === 0 && !opts.instant) puff();
        if (m === "wow" && !finished && !opts.instant) { finished = true; if (JC.confetti) JC.confetti(40); }
      }
    };
  }
  /** Ручная игра — те же правила, что у сервера. */
  function runnerSim(obstacles, length, speedUpAt) {
    const g = { x: 0, tick: 0, air: 0, duck: 0, over: null };
    const at = (c, type) => obstacles.some((o) => o[2] === type && c >= o[0] && c <= o[0] + o[1] - 1);
    g.next = () => obstacles.find((o) => o[0] + o[1] - 1 > g.x);
    g.radar = () => { const o = g.next(); const d = !o || o[0] - g.x > RUN_VIEW ? 99 : Math.max(0, o[0] - g.x); return { d, low: d !== 99 && o[2] === 0, speed: g.x >= speedUpAt ? 2 : 1 }; };
    g.state = () => g.air > 0 ? "jump" : g.duck > 0 ? "duck" : "run";
    g.step = (cmd) => {
      if (g.over) return;
      const busy = g.air > 0 || g.duck > 0;
      if (!busy && cmd === "JUMP") g.air = RUN_JUMP;
      if (!busy && cmd === "DUCK") g.duck = RUN_DUCK;
      g.during = g.state();
      const s = g.x >= speedUpAt ? 2 : 1;
      for (let k = 0; k < s; k++) {
        g.x++;
        if (at(g.x, 0) && g.air === 0) { g.over = "crash"; g.why = "врезался в жука"; return; }
        if (at(g.x, 1) && g.duck === 0) { g.over = "crash"; g.why = g.air > 0 ? "в прыжке врезался в летучую ошибку" : "врезался в летучую ошибку"; return; }
        if (g.x >= length) break;
      }
      g.tick++;
      if (g.air > 0 && --g.air === 0 && at(g.x, 0)) { g.over = "crash"; g.why = "приземлился прямо на жука — прыгнул слишком рано"; return; }
      if (g.duck > 0 && --g.duck === 0 && at(g.x, 1)) { g.over = "crash"; g.why = "выпрямился под летучей ошибкой — пригнулся слишком рано"; return; }
      if (g.x >= length) g.over = "finish";
    };
    return g;
  }

  W.runnerhand = function (el) {
    const { body, goal } = frame(el, { icon: "🏃", kind: "Мини-игра", title: "Беги сам: пробел — прыжок, ↓ — присесть", goals: ["Добеги до финиша", "Посмотри, на каком расстоянии ты прыгаешь"], xp: 20 });
    // фиксированная трасса для ручной игры
    const OBS = [[10, 1, 0], [18, 2, 0], [26, 1, 1], [33, 1, 0], [40, 1, 1], [47, 2, 0], [55, 1, 1], [61, 1, 0]], LEN = 68;
    body.innerHTML = `
      <p class="pd-help">Джавик бежит сам — ты только жмёшь: <kbd>Пробел</kbd> или <kbd>↑</kbd> — прыжок через жука 🐛, <kbd>↓</kbd> — присесть под летучей ошибкой 🦇. Справа — что видит радар. Следи, <b>на каком расстоянии</b> ты нажимаешь.</p>
      <div class="rn-main"><div class="rn-view"></div>
        <div class="pd-radar"><small>Радар</small><div class="rn-rv"></div></div></div>
      <div class="pd-keys"><button class="btn small green rn-start">▶ Старт</button><button class="btn small ghost" data-c="JUMP">⬆ Прыжок</button><button class="btn small ghost" data-c="DUCK">⬇ Присесть</button>
        <label class="rn-slow"><input type="checkbox" checked> медленно</label></div>
      <div class="pd-log" aria-live="polite">Нажми «Старт». Кнопки работают и мышкой.</div>
      <div class="rn-hist"></div>`;
    const $ = (s) => body.querySelector(s);
    const view = runnerView($(".rn-view"));
    view.setTrack(OBS, LEN);
    let g, timer = null, queued = "RUN";
    const presses = [];
    function radarHtml() {
      const r = g ? g.radar() : { d: OBS[0][0], low: true, speed: 1 };
      $(".rn-rv").innerHTML = `<div><code>distance()</code> <b>${r.d}</b></div><div><code>isLow()</code> <b class="${r.low ? "y" : "n"}">${r.low}</b></div><div><code>speed()</code> <b>${r.speed}</b></div>`;
    }
    function reset() { clearInterval(timer); timer = null; g = runnerSim(OBS, LEN, 999); queued = "RUN"; presses.length = 0; view.show(0, "run", "happy", { instant: true }); radarHtml(); $(".rn-hist").textContent = ""; }
    function tick() {
      const r = g.radar();
      if (queued !== "RUN" && g.air === 0 && g.duck === 0) presses.push(`${queued === "JUMP" ? "прыжок" : "присел"} при distance = ${r.d}`);
      g.step(queued); queued = "RUN";
      const ms = $(".rn-slow input").checked ? 420 : 260;
      const phase = g.over === "crash" ? (g.during === "jump" ? RUN_JUMP - g.air : g.during === "duck" ? RUN_DUCK - g.duck : 0)
        : g.during === "jump" ? RUN_JUMP - g.air - 1 : g.during === "duck" ? RUN_DUCK - g.duck - 1 : 0;
      view.show(g.x, g.over === "crash" ? "crash" : g.during, g.over === "crash" ? "oops" : g.over === "finish" ? "wow" : "happy", { phase, ms, was: g.during });
      radarHtml();
      $(".rn-hist").textContent = presses.length ? "Твои нажатия: " + presses.slice(-6).join(" · ") : "";
      if (g.over) {
        clearInterval(timer); timer = null;
        $(".pd-log").textContent = g.over === "finish"
          ? "🏁 Добежал! Посмотри на свои нажатия ниже: при каком distance ты прыгал? Это число и пригодится в коде."
          : `💥 Ой: ${g.why}. Жми «Старт» — ещё раз.`;
        if (g.over === "finish") { goal(0); if (presses.length) goal(1); }
      }
    }
    function start() { reset(); $(".pd-log").textContent = "Беги! Пробел/↑ — прыжок, ↓ — присесть."; timer = setInterval(tick, $(".rn-slow input").checked ? 420 : 260); el.focus({ preventScroll: true }); }
    $(".rn-start").addEventListener("click", start);
    body.querySelectorAll("[data-c]").forEach((b) => b.addEventListener("click", () => { queued = b.dataset.c; }));
    el.tabIndex = 0;
    el.addEventListener("keydown", (e) => {
      if (!timer) return;
      if (e.key === " " || e.key === "ArrowUp") { e.preventDefault(); queued = "JUMP"; }
      if (e.key === "ArrowDown") { e.preventDefault(); queued = "DUCK"; }
    });
    reset();
  };

  W.runnercode = function (el) {
    const LEVELS = [
      { id: "m04-r1", name: "1. Жуки", tip: "Только жуки 🐛. Подбери, за сколько клеток прыгать." },
      { id: "m04-r2", name: "2. Жуки и летучие", tip: "Добавились летучие ошибки 🦇 — под ними пригибайся (DUCK)." },
      { id: "m04-r3", name: "3. Ускорение ★★★", tip: "С середины трассы скорость 2. Выведи distance() на каждом ходу и посмотри, какие числа видит радар." }
    ];
    const { body } = frame(el, { icon: "🧑‍💻", kind: "Пишешь код", title: "Научи Джавика: метод decide" });
    body.innerHTML = `
      <div class="pd-vars rn-levels">${LEVELS.map((l, i) => `<button class="pd-var ${i ? "" : "on"}" data-i="${i}">${l.name} <span class="rn-st"></span></button>`).join("")}</div>
      <p class="pd-help rn-tip"></p>
      <div class="rn-editor"><div class="rn-file">Javik.java <span>— сохраняется в workspace/module-04, его можно открыть и в IDEA</span></div>
        <textarea class="rn-code" spellcheck="false" autocomplete="off" autocapitalize="off" aria-label="Код класса Javik"></textarea></div>
      <div class="pd-keys"><button class="btn green rn-run">▶ Запустить</button><span class="rn-hint"><kbd>Ctrl</kbd>+<kbd>Enter</kbd> — тоже запуск · <kbd>Tab</kbd> — отступ</span></div>
      <div class="rn-result"></div>
      <div class="rn-replay" hidden>
        <div class="rn-maps"></div>
        <div class="rn-view"></div>
        <div class="pd-keys"><button class="btn small ghost rn-prev">◀ Ход</button><button class="btn small indigo rn-play">▶ Смотреть</button><button class="btn small ghost rn-next">Ход ▶</button><input type="range" class="rn-slider" min="0" value="0"></div>
        <div class="rn-frame"></div>
      </div>`;
    const $ = (s) => body.querySelector(s);
    const ta = $(".rn-code");
    let level = 0, replays = [], cur = 0, fi = 0, timer = null;
    const view = runnerView($(".rn-view"));
    function setLevel(i) { level = i; body.querySelectorAll(".rn-levels .pd-var").forEach((b) => b.classList.toggle("on", +b.dataset.i === i)); $(".rn-tip").innerHTML = LEVELS[i].tip; }
    body.querySelectorAll(".rn-levels .pd-var").forEach((b) => b.addEventListener("click", () => setLevel(+b.dataset.i)));
    setLevel(0);
    fetch("/api/code/" + LEVELS[0].id).then((r) => r.json()).then((j) => { ta.value = j.code || ""; }).catch(() => { $(".rn-result").innerHTML = `<div class="result-banner bad">Сервер курса не отвечает. Запусти его: docker compose up -d</div>`; });
    // редактор: Tab — 4 пробела, Enter — тот же отступ, что у строки выше
    ta.addEventListener("keydown", (e) => {
      if (e.key === "Tab") { e.preventDefault(); ta.setRangeText("    ", ta.selectionStart, ta.selectionEnd, "end"); }
      else if (e.key === "Enter" && (e.ctrlKey || e.metaKey)) { e.preventDefault(); run(); }
      else if (e.key === "Enter") {
        const before = ta.value.slice(0, ta.selectionStart), line = before.slice(before.lastIndexOf("\n") + 1);
        let ind = (line.match(/^\s*/) || [""])[0]; if (/\{\s*$/.test(line)) ind += "    ";
        e.preventDefault(); ta.setRangeText("\n" + ind, ta.selectionStart, ta.selectionEnd, "end");
      }
    });
    async function run() {
      const L = LEVELS[level];
      $(".rn-run").disabled = true; $(".rn-result").innerHTML = `<div class="result-banner info">⏳ Компилирую и запускаю забег на трёх трассах…</div>`; $(".rn-replay").hidden = true;
      try {
        await fetch("/api/code/" + L.id, { method: "POST", body: ta.value });
        const r = await fetch("/api/check/" + L.id, { method: "POST" }).then((x) => x.json());
        show(r);
      } catch (e) { $(".rn-result").innerHTML = `<div class="result-banner bad">Сервер курса не отвечает. Запусти его: docker compose up -d</div>`; }
      $(".rn-run").disabled = false;
    }
    $(".rn-run").addEventListener("click", run);
    function show(r) {
      let html = "";
      const st = body.querySelector(`.rn-levels .pd-var[data-i="${level}"] .rn-st`);
      if (r.status === "compile_error") {
        html += `<div class="result-banner bad">🛠 Код не компилируется — javac нашёл ошибки. Начни с первой: номер строки после «Javik.java:».</div><pre class="terminal"><span class="err">${esc(r.compilerOutput || "")}</span></pre>`;
      } else {
        html += `<div class="result-banner ${r.status === "passed" ? "ok" : "bad"}">${r.status === "passed" ? "🎉 " : ""}${esc(JC.msg(r.message || ""))}</div>`;
        if (st) st.textContent = r.status === "passed" ? "✓" : "";
      }
      const hints = [...(r.hints || []), ...(r.ruleFails || [])];
      if (hints.length) html += `<ul class="hints">${hints.map((h) => `<li>💡 ${esc(h)}</li>`).join("")}</ul>`;
      $(".rn-result").innerHTML = html;
      replays = r.replays || [];
      if (!replays.length) return;
      $(".rn-replay").hidden = false;
      $(".rn-maps").innerHTML = replays.map((p, i) => `<button class="pd-var ${i ? "" : "on"}" data-i="${i}">Трасса ${i + 1} ${p && p.result === "finish" ? "✅" : "❌"}</button>`).join("");
      body.querySelectorAll(".rn-maps .pd-var").forEach((b) => b.addEventListener("click", () => pick(+b.dataset.i)));
      const firstBad = replays.findIndex((p) => !p || p.result !== "finish");
      pick(firstBad >= 0 ? firstBad : 0);
    }
    function pick(i) {
      cur = i; stop();
      body.querySelectorAll(".rn-maps .pd-var").forEach((b) => b.classList.toggle("on", +b.dataset.i === i));
      const p = replays[i]; if (!p) { $(".rn-frame").textContent = "Эту трассу запустить не удалось."; return; }
      view.setTrack(p.obstacles, p.length);
      $(".rn-slider").max = p.frames.length;
      // трасса с ошибкой — сразу к моменту за 3 хода до столкновения
      go(p.result === "finish" ? 0 : Math.max(0, p.frames.length - 4), true);
    }
    function go(k, instant) {
      const p = replays[cur]; if (!p) return;
      const n = p.frames.length;
      fi = Math.max(0, Math.min(n, k));
      $(".rn-slider").value = fi;
      if (fi === 0) {
        view.show(0, "run", "happy", { instant: true });
        $(".rn-frame").innerHTML = "Старт. Жми «▶ Смотреть» или «Ход ▶».";
        return;
      }
      const f = p.frames[fi - 1];
      let phase = 0; for (let j = fi - 2; j >= 0 && p.frames[j].state === f.state; j--) phase++;
      const end = fi === n && p.result !== "finish";
      view.show(f.to, end ? "crash" : f.state, end ? "oops" : fi === n ? "wow" : "happy", { phase, ms: timer ? 330 : 280, instant, was: f.state });
      $(".rn-frame").innerHTML = `<b>Ход ${fi}.</b> Радар: <code>distance()</code> = <b>${f.d}</b>, <code>isLow()</code> = <b>${f.low}</b>, <code>speed()</code> = <b>${f.speed}</b> → твой <code>decide</code> вернул <span class="pg-action">${esc(f.action)}</span>`
        + (f.log && f.log.length ? `<div class="rn-log"><small>Вывод и подсказки:</small>${f.log.map((l) => `<div>${esc(l)}</div>`).join("")}</div>` : "")
        + (fi === n ? `<div style="margin-top:6px"><b>${esc(p.message)}</b></div>` : "");
    }
    function stop() { clearInterval(timer); timer = null; $(".rn-play").textContent = "▶ Смотреть"; }
    $(".rn-prev").addEventListener("click", () => { stop(); go(fi - 1); });
    $(".rn-next").addEventListener("click", () => { stop(); go(fi + 1); });
    $(".rn-slider").addEventListener("input", (e) => { stop(); go(+e.target.value); });
    $(".rn-play").addEventListener("click", () => {
      if (timer) return stop();
      const p = replays[cur]; if (!p) return;
      if (fi >= p.frames.length) go(0, true);
      $(".rn-play").textContent = "⏸ Пауза";
      timer = setInterval(() => { if (fi >= replays[cur].frames.length) return stop(); go(fi + 1); }, 330);
    });
  };

  // =====================================================================
  // 14. «Проверить» прямо на странице ★-задания: та же проверка, что в «Практике».
  //     <div data-widget="taskcheck" data-task="m01-star"></div>
  // =====================================================================
  // =====================================================================
  // Карта коллекций: иерархия интерфейсов и классов, клик → панель слева
  // Данные в уроке: { title, nodes: [{ id, label, kind: "iface"|"class", x, y, use: "hot"|"ok"|"rare"|"legacy",
  //   parents: [{ to, rel: "extends"|"implements" }], what, props: {Порядок, Дубликаты, null, Скорость}, methods: [[код, что делает]],
  //   when, lesson: "m13-04" }], start: "Collection" }
  // =====================================================================
  W.collmap = function (el) {
    const d = readData(el);
    const legacyIds = d.nodes.filter((n) => n.use === "legacy").map((n) => n.id);
    const { body, goal } = frame(el, { icon: "🗺️", kind: "Карта", title: d.title,
      goals: ["Открой 5 разных классов", `Найди все устаревшие классы (их ${legacyIds.length})`], xp: 25 });
    const USE = { hot: ["🔥", "используют постоянно"], ok: ["👍", "используют иногда"], rare: ["💤", "редко, для особых случаев"], legacy: ["⛔", "устарел — в новом коде не бери"] };
    const byId = Object.fromEntries(d.nodes.map((n) => [n.id, n]));
    const W_ = (n) => Math.round(n.label.length * 8.7 + (n.kind === "class" ? 36 : 22));
    const H = 30;
    const lessonLink = (id) => {
      for (const m of (window.COURSE || {}).modules || []) {
        const i = m.lessons.findIndex((l) => l.id === id);
        if (i >= 0) return `<a href="${m.lessons[i].url}">урок ${m.num}.${i + 1} «${esc(m.lessons[i].title)}»</a>`;
      }
      return "";
    };
    // стрелки: от ребёнка к родителю
    const edges = [];
    d.nodes.forEach((n) => (n.parents || []).forEach((p) => {
      const a = n, b = byId[p.to]; if (!b) return;
      const y1 = a.y - H / 2, y2 = b.y + H / 2;
      edges.push(`<path class="cm-edge ${p.rel}" data-from="${esc(a.id)}" data-to="${esc(b.id)}" d="M${a.x} ${y1} C ${a.x} ${(y1 + y2) / 2}, ${b.x} ${(y1 + y2) / 2}, ${b.x} ${y2 + 4}" marker-end="url(#cm-arrow-${p.rel})"/>`);
    }));
    const nodes = d.nodes.map((n) => {
      const w = W_(n), u = USE[n.use];
      return `<g class="cm-node ${n.kind} use-${n.use || "none"}" data-id="${esc(n.id)}" tabindex="0" role="button" aria-label="${esc(n.label)}">
        <rect x="${n.x - w / 2}" y="${n.y - H / 2}" width="${w}" height="${H}" rx="${n.kind === "iface" ? 15 : 7}"/>
        <text x="${n.x + (u ? 8 : 0)}" y="${n.y + 5}" text-anchor="middle">${esc(n.label)}</text>
        ${u ? `<text class="cm-badge" x="${n.x - w / 2 + 13}" y="${n.y + 5}" text-anchor="middle">${u[0]}</text>` : ""}
      </g>`;
    }).join("");
    const vw = Math.max(...d.nodes.map((n) => n.x + W_(n) / 2)) + 12, vh = Math.max(...d.nodes.map((n) => n.y)) + 30;
    body.innerHTML = `
      <div class="cm-filters"><span>Подсветить:</span>
        <button class="btn small ghost on" data-f="all">все</button>
        <button class="btn small ghost" data-f="hot">🔥 используют постоянно</button>
        <button class="btn small ghost" data-f="legacy">⛔ устаревшие</button>
        <button class="btn small ghost" data-f="iface">только интерфейсы</button></div>
      <div class="cm-wrap">
        <aside class="cm-panel" aria-live="polite"></aside>
        <div class="cm-map"><svg viewBox="0 0 ${vw} ${vh}" role="img" aria-label="${esc(d.title)}">
          <defs>
            <marker id="cm-arrow-extends" viewBox="0 0 10 10" refX="8" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse"><path d="M0 0 L10 5 L0 10 z" class="cm-ah"/></marker>
            <marker id="cm-arrow-implements" viewBox="0 0 10 10" refX="8" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse"><path d="M0 0 L10 5 L0 10 z" class="cm-ah"/></marker>
          </defs>
          ${edges.join("")}${nodes}
        </svg></div>
      </div>
      <ul class="cm-legend">
        <li><span class="lg iface"></span>интерфейс — контракт «что умеет»</li>
        <li><span class="lg class"></span>класс — готовая реализация, её создают через <code>new</code></li>
        <li><span class="lg arr ext"></span>наследует (extends)</li>
        <li><span class="lg arr imp"></span>реализует (implements)</li>
        <li>🔥 постоянно · 👍 иногда · 💤 редко · ⛔ устарел</li>
      </ul>`;
    const panel = body.querySelector(".cm-panel");
    const opened = new Set(), legacySeen = new Set();
    function show(id) {
      const n = byId[id]; if (!n) return;
      body.querySelectorAll(".cm-node").forEach((g) => g.classList.toggle("sel", g.dataset.id === id));
      // родители выбранного — подсвечиваем цепочку до самого верха
      const chain = new Set([id]); const walk = (x) => (byId[x].parents || []).forEach((p) => { if (!chain.has(p.to) && byId[p.to]) { chain.add(p.to); walk(p.to); } }); walk(id);
      body.querySelectorAll(".cm-node").forEach((g) => g.classList.toggle("chain", chain.has(g.dataset.id) && g.dataset.id !== id));
      body.querySelectorAll(".cm-edge").forEach((e) => e.classList.toggle("chain", chain.has(e.dataset.from) && chain.has(e.dataset.to)));
      const u = USE[n.use];
      const parents = (n.parents || []).map((p) => `${p.rel === "extends" ? "наследует" : "реализует"} <code>${esc(byId[p.to] ? byId[p.to].label : p.to)}</code>`).join(", ");
      panel.innerHTML = `
        <div class="cm-kind">${n.kind === "iface" ? "Интерфейс" : "Класс"}${u ? ` · ${u[0]} ${u[1]}` : ""}</div>
        <h4 class="cm-name"><code>${esc(n.label)}</code></h4>
        ${parents ? `<p class="cm-par">${parents}</p>` : ""}
        <p>${n.what || ""}</p>
        ${n.props ? `<table class="cm-props">${Object.entries(n.props).map(([k, v]) => `<tr><th>${esc(k)}</th><td>${v}</td></tr>`).join("")}</table>` : ""}
        ${n.methods && n.methods.length ? `<div class="cm-sub">Главные методы</div><ul class="cm-methods">${n.methods.map(([c, t]) => `<li><code>${esc(c)}</code><span>${t}</span></li>`).join("")}</ul>` : ""}
        ${n.when ? `<div class="cm-sub">Когда брать</div><p>${n.when}</p>` : ""}
        ${n.lesson ? `<p class="cm-lesson">📖 ${lessonLink(n.lesson)}</p>` : ""}`;
      if (n.kind === "class") { opened.add(id); if (opened.size >= 5) goal(0); }
      if (n.use === "legacy") { legacySeen.add(id); if (legacySeen.size >= legacyIds.length) goal(1); }
    }
    body.querySelectorAll(".cm-node").forEach((g) => {
      g.addEventListener("click", () => show(g.dataset.id));
      g.addEventListener("keydown", (e) => { if (e.key === "Enter" || e.key === " ") { e.preventDefault(); show(g.dataset.id); } });
    });
    body.querySelectorAll("[data-f]").forEach((b) => b.addEventListener("click", () => {
      body.querySelectorAll("[data-f]").forEach((x) => x.classList.toggle("on", x === b));
      const f = b.dataset.f;
      body.querySelectorAll(".cm-node").forEach((g) => {
        const n = byId[g.dataset.id];
        const match = f === "all" || (f === "iface" ? n.kind === "iface" : n.use === f);
        g.classList.toggle("dim", !match);
      });
    }));
    show(d.start || d.nodes[0].id);
  };

  W.taskcheck = function (el) {
    const id = el.dataset.task;
    el.classList.add("taskcheck");
    el.innerHTML = `<div class="tc-head"><b>★ Проверка задания</b><span class="tc-file"></span></div>
      <button class="btn green tc-run">▶ Проверить</button><div class="tc-result"></div>`;
    const $ = (q) => el.querySelector(q);
    fetch("/api/tasks").then((r) => r.json()).then((list) => {
      const t = list.find((x) => x.id === id);
      if (t) $(".tc-file").textContent = "файл: " + t.file;
    }).catch(() => {});
    const inputLine = (s) => s.split("\n").join(" ⏎ ");
    function render(r) {
      let html = "";
      if (r.status === "passed") html += `<div class="result-banner ok">🎉 ${esc(JC.msg(r.message))}</div>`;
      else if (r.status === "missing" || r.status === "manual") html += `<div class="result-banner info">📄 ${esc(JC.msg(r.message))}</div>`;
      else if (r.status === "compile_error") html += `<div class="result-banner bad">🛠 ${esc(JC.msg(r.message))}</div><pre class="terminal"><span class="err">${esc(r.compilerOutput || "")}</span></pre>`;
      else html += `<div class="result-banner bad">${esc(JC.msg(r.message || "Что-то пошло не так"))}</div>`;
      const hints = [...(r.hints || []), ...(r.ruleFails || [])];
      if (hints.length) html += `<ul class="hints">${hints.map((h) => `<li>💡 ${esc(h)}</li>`).join("")}</ul>`;
      for (const t of r.tests || []) {
        html += `<div class="test-row ${t.passed ? "ok" : "bad"}"><div>${t.passed ? "✓" : "✗"} ${esc(t.name)}${t.input ? ` <small>(ввод: ${esc(inputLine(t.input))})</small>` : ""}</div>`;
        if (!t.passed) {
          if (t.error) html += `<div style="padding:8px 14px">⚠️ ${esc(t.error)}</div>`;
          if (t.diff) html += `<div style="padding:8px 14px">🔎 ${esc(t.diff)}</div>`;
          html += `<div class="diff"><div><small>Ожидалось (примерно так)</small><pre>${esc(t.expected || "")}</pre></div><div><small>Твоя программа вывела</small><pre>${esc(t.actual) || "(пусто)"}</pre></div></div>`;
        }
        html += "</div>";
      }
      return html;
    }
    $(".tc-run").addEventListener("click", async () => {
      const b = $(".tc-run");
      b.disabled = true; b.textContent = "⏳ Компилирую и запускаю…";
      try {
        const r = await fetch("/api/check/" + id, { method: "POST" }).then((x) => x.json());
        $(".tc-result").innerHTML = render(r);
        if (r.status === "passed") { try { if (JC.confetti) JC.confetti(50); } catch (e) { /* без конфетти — не страшно */ } }
      } catch (e) {
        $(".tc-result").innerHTML = `<div class="result-banner bad">Сервер курса не отвечает. Запусти его: docker compose up -d</div>`;
      }
      b.disabled = false; b.textContent = "▶ Проверить ещё раз";
    });
  };
})();
