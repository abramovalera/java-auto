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
  // 13. «Джавик-курьер» — сквозная игра (модули 4, 5, 6, 7, 13). Код ученик пишет в IDEA,
  //     сервер прогоняет его на картах уровня (движок tasks/courier/engine) и отдаёт журнал ходов.
  //     JC.courierReplay(host, result) — просмотр поездки; его же зовут страница «Практики» и taskcheck.
  //     <div data-widget="courier" data-tasks="m04-c1,m04-c2,m04-c3"></div> — блок «запусти свой код» в уроке.
  // =====================================================================
  const CR_ARROW = ["↑", "→", "↓", "←"];

  JC.courierReplay = function (host, r, opts = {}) {
    const replays = (r && r.replays) || [];
    let dead = false;
    if (!replays.length) { host.innerHTML = ""; return; }
    const tests = r.tests || [];
    // код ученика рядом с картой: на каждом ходу подсвечиваем, какие строки спросили датчики и какая дала команду
    const code = opts.compact ? null : (opts.code || r.source || null);
    const stepMode = !!code && /\bvoid\s+step\s*\(/.test(code);
    const speeds = code ? [1100, 650, 250] : [500, 250, 90];
    // карты уровня — маленькой строкой под плеером: «Карты: ✓1 ✓2 ✗3» (одна карта — строки нет)
    const mapTabs = replays.length < 2 ? "" : `<div class="cr-tabs" role="tablist" aria-label="Карты уровня"><span class="cr-tabs-label">Карты:</span>${replays.map((p, i) => {
      const ok = p && p.result === "finish";
      return `<button class="cr-tab ${ok ? "ok" : "bad"}" data-i="${i}" role="tab" title="${esc((tests[i] && tests[i].name) || "Карта " + (i + 1))}${ok ? " — доехал" : " — не получилось"}">${ok ? "✓" : "✗"} ${i + 1}</button>`;
    }).join("")}</div>`;
    const info = `<div class="cr-side">
          <div class="cr-step"></div>
          <div class="cr-cmd"></div>
          <div class="cr-carry"></div>
          <div class="cr-ask-title">Перед ходом Джавик спросил:</div>
          <ul class="cr-ask"></ul>
          <div class="cr-note"></div>
        </div>`;
    host.innerHTML = `<div class="cr${code ? " with-code" : ""}">
      <div class="cr-main">${code
        // два равных экрана: слева поле и что происходит на ходу, справа — код ученика
        ? `<div class="cr-left"><div class="cr-board-wrap"><div class="cr-board"></div></div>${info}</div>
          <div class="cr-codecol"><div class="cr-code-head"></div><div class="cr-code">${code.replace(/\s+$/, "").split("\n").map((l, k) =>
            `<div class="cr-line" data-l="${k + 1}"><span class="no">${k + 1}</span><code>${JC.highlightJava(l) || " "}</code><span class="cr-badges"></span></div>`).join("")}</div></div>`
        : `<div class="cr-board-wrap"><div class="cr-board"></div></div>${info}`}
      </div>
      <div class="cr-ctrl">
        <button class="btn ghost cr-first" title="К началу">⏮</button>
        <button class="btn ghost cr-prev" title="Ход назад">◀</button>
        <button class="btn cr-play">▶ Смотреть</button>
        <button class="btn ghost cr-next" title="Ход вперёд">▶</button>
        <button class="btn ghost cr-last" title="К концу">⏭</button>
        <input class="cr-range" type="range" min="0" max="0" value="0" aria-label="Ход">
        <select class="cr-speed" aria-label="Скорость"><option value="${speeds[0]}">медленно</option><option value="${speeds[1]}" selected>обычно</option><option value="${speeds[2]}">быстро</option></select>
      </div>
      ${mapTabs}
      <div class="cr-msg"></div>
      <div class="cr-out"></div>
    </div>`;
    const $ = (q) => host.querySelector(q);
    if (opts.compact) $(".cr").classList.add("cr-compact"); // только поле: демо и карта уровня до запуска
    let cur = -1, states = [], fi = 0, timer = null, replay = null;

    function build(p) {
      // состояния до кадра 0 и после каждого кадра
      const parcels = {}, done = {};
      for (const it of p.items || []) if (it.p) parcels[it.x + ":" + it.y] = it.p;
      const s0 = { x: p.start[0], y: p.start[1], d: p.start[2], c: p.carry0 || 0, parcels: { ...parcels }, done: {}, cmd: "", ask: [], note: "" };
      const list = [s0];
      let prev = s0;
      for (const f of p.frames || []) {
        const st = { x: f.x, y: f.y, d: f.d, c: f.c, parcels: { ...prev.parcels }, done: { ...prev.done }, cmd: f.cmd, ask: f.ask || [], note: f.note || "", night: !!f.night,
          al: f.al || [], ln: f.ln == null ? -1 : f.ln };
        for (const [x, y, n] of f.p || []) {
          if (n < 0) st.done[x + ":" + y] = (st.done[x + ":" + y] || 0) + 1; else st.parcels[x + ":" + y] = n;
        }
        list.push(st); prev = st;
      }
      return list;
    }

    function drawBoard(p) {
      const board = $(".cr-board");
      board.style.setProperty("--cw", p.w); board.style.setProperty("--ch", p.h);
      let html = "";
      for (let y = 0; y < p.h; y++) for (let x = 0; x < p.w; x++) {
        html += `<div class="cr-cell ${p.grid[y][x] === "#" ? "wall" : "road"}" style="--x:${x};--y:${y}"></div>`;
      }
      html += `<div class="cr-item depot" style="--x:${p.start[0]};--y:${p.start[1]}" title="Склад">🏁</div>`;
      for (const it of p.items || []) {
        if (it.t) html += `<div class="cr-item target" data-k="${it.x}:${it.y}" style="--x:${it.x};--y:${it.y}" title="${esc(it.n || "Адрес доставки")}">🏠${it.n ? `<span class="cr-name${it.x % 2 ? " below" : ""}">${esc(it.n)}</span>` : ""}</div>`;
        if (it.pit) html += `<div class="cr-item pit" style="--x:${it.x};--y:${it.y}" title="Яма: move() сюда — падение, перепрыгнуть — jump()"></div>`;
      }
      html += `<div class="cr-parcels"></div><div class="cr-robot"><div class="cr-bot">${JC.mascot("happy")}</div><div class="cr-dir"></div><div class="cr-load"></div></div>`;
      board.innerHTML = html;
      fit();
    }

    function fit() {
      const wrap = $(".cr-board-wrap"), board = $(".cr-board");
      if (!replay || !wrap) return;
      const c = Math.max(16, Math.min(code ? 38 : 40, Math.floor((wrap.clientWidth - 4) / replay.w)));
      board.style.setProperty("--c", c + "px");
    }

    function show(i, instant) {
      fi = Math.max(0, Math.min(states.length - 1, i));
      const s = states[fi], board = $(".cr-board"), robot = board.querySelector(".cr-robot");
      robot.classList.toggle("instant", !!instant);
      robot.style.setProperty("--x", s.x); robot.style.setProperty("--y", s.y);
      robot.querySelector(".cr-dir").style.transform = `rotate(${s.d * 90}deg)`;
      robot.querySelector(".cr-load").textContent = s.c ? "📦" + (s.c > 1 ? s.c : "") : "";
      const last = fi === states.length - 1;
      const crashed = last && replay.result !== "finish";
      robot.classList.toggle("crash", crashed);
      robot.classList.toggle("win", last && replay.result === "finish");
      // прыжок через яму — дугой; падение в яму — Джавик проваливается
      robot.classList.remove("hop");
      if (!instant && /^JUMP/.test(s.cmd || "")) { void robot.offsetWidth; robot.classList.add("hop"); }
      robot.classList.toggle("fall", crashed && /яму/.test(s.note || ""));
      robot.querySelector(".cr-bot").innerHTML = JC.mascot(crashed ? "oops" : last && replay.result === "finish" ? "cool" : "happy");
      board.querySelector(".cr-parcels").innerHTML = Object.entries(s.parcels).filter(([, n]) => n > 0).map(([k, n]) => {
        const [x, y] = k.split(":");
        return `<div class="cr-item parcel" style="--x:${x};--y:${y}">📦${n > 1 ? `<b>${n}</b>` : ""}</div>`;
      }).join("");
      board.querySelectorAll(".cr-item.target").forEach((t) => t.classList.toggle("got", !!s.done[t.dataset.k]));
      $(".cr-step").textContent = fi === 0 ? "Старт" : `Ход ${fi} из ${states.length - 1}`;
      $(".cr-cmd").innerHTML = fi === 0 ? `<span class="muted">Джавик на складе, смотрит ${["на север", "на восток", "на юг", "на запад"][s.d]} ${CR_ARROW[s.d]}</span>` : `<code>${esc(s.cmd)}</code>`;
      board.classList.toggle("night", !!s.night);
      $(".cr-carry").textContent = (s.c ? `Везёт посылок: ${s.c}` : "Руки свободны") + (s.night ? " · 🌙 ночь: датчики стен не работают" : "");
      // с кодом вопросы видны прямо у строк — в списке остаются только те, что задал не код ученика (автопилот)
      const listed = code ? s.ask.filter((a, k) => !(s.al[k] > 0)) : s.ask;
      $(".cr-ask").innerHTML = listed.length ? listed.map((a) => `<li><code>${esc(a)}</code></li>`).join("") : `<li class="muted">ничего</li>`;
      const hideAsk = fi === 0 || (code && !listed.length);
      $(".cr-ask-title").style.display = hideAsk ? "none" : "";
      $(".cr-ask").style.display = hideAsk ? "none" : "";
      if (code) paintCode(s, instant);
      $(".cr-note").textContent = s.note || (crashed ? replay.message : "");
      $(".cr-note").classList.toggle("bad", !!(s.note || crashed));
      $(".cr-range").value = fi;
    }

    const srcLines = code ? code.replace(/\s+$/, "").split("\n") : [];
    const indent = (n) => (srcLines[n - 1] || "").match(/^\s*/)[0].length;
    const blank = (n) => !(srcLines[n - 1] || "").trim() || /^\s*\/\//.test(srcLines[n - 1]);
    /** Строки-условия цепочки if / else if / else над строкой n: [[номер, это ли сработавшая ветка], ...]. */
    function chainOf(n) {
      if (!(n > 0)) return [];
      let c = n - 1;
      while (c >= 1 && (blank(c) || indent(c) >= indent(n))) c--; // условие, в чьей ветке стоит команда
      const isCond = (k) => /^\s*(}\s*)?(else\s+)?if\s*\(|^\s*}\s*else\s*\{?\s*$/.test(srcLines[k - 1] || "");
      if (c < 1 || !isCond(c)) return [];
      const out = [[c, true]], lvl = indent(c);
      if (/^\s*if\s*\(/.test(srcLines[c - 1])) return out; // первое звено цепочки
      for (let k = c - 1; k >= 1; k--) {
        if (blank(k) || indent(k) > lvl) continue;
        if (indent(k) < lvl) break;
        if (/^\s*}\s*else\s+if\s*\(/.test(srcLines[k - 1])) out.push([k, false]);
        else if (/^\s*if\s*\(/.test(srcLines[k - 1])) { out.push([k, false]); break; }
        else if (!/^\s*}\s*$/.test(srcLines[k - 1])) break;
      }
      return out;
    }
    /** Задержка анимации строки цепочки: в момент вопроса на этой строке, иначе — между соседними вопросами. */
    function chainDelay(n, events, gap) {
      const k = events.findIndex((e) => e.n >= n);
      return (k < 0 ? events.length : k) * gap;
    }

    /** Подсветка кода на ходу: строки загораются по очереди — в том порядке, в каком их выполнила программа. */
    function paintCode(s, instant) {
      const box = $(".cr-code"), head = $(".cr-code-head");
      const line = (n) => (n > 0 ? box.querySelector(`.cr-line[data-l="${n}"]`) : null);
      box.querySelectorAll(".cr-line").forEach((l) => { l.className = "cr-line"; l.style.removeProperty("--d"); l.querySelector(".cr-badges").innerHTML = ""; });
      void box.offsetWidth; // перезапуск анимации у тех же строк на следующем ходу
      if (fi === 0) {
        box.classList.remove("active");
        head.textContent = stepMode ? "Твой код. На каждом ходу игра вызывает step — и код выполняется сверху вниз." : "Твой код. Жми «Смотреть» — строки будут загораться по ходу программы.";
        return;
      }
      box.classList.add("active");
      const entry = stepMode ? [...box.querySelectorAll(".cr-line")].find((l) => /\bvoid\s+step\s*\(/.test(l.textContent)) : null;
      if (entry) entry.classList.add("entry");
      const events = s.ask.map((a, k) => ({ n: s.al[k], a })).filter((e) => e.n > 0);
      const speed = opts.speed || +$(".cr-speed").value;
      const gap = instant ? 0 : Math.min(160, (speed * 0.55) / (events.length + 1));
      const perLine = {};
      events.forEach((e) => { perLine[e.n] = (perLine[e.n] || 0) + 1; });
      events.forEach((e, k) => {
        const L = line(e.n); if (!L) return;
        if (!L.classList.contains("asked")) { L.classList.add("asked"); L.style.setProperty("--d", Math.round(k * gap) + "ms"); }
        const i = e.a.lastIndexOf(" = "), call = i > 0 ? e.a.slice(0, i) : e.a, val = i > 0 ? e.a.slice(i + 3) : "";
        const cls = val === "true" ? "yes" : val === "false" ? "no" : "val";
        // коротко: сам вызов и так виден в строке; если вызовов в строке несколько — с именем
        const label = !val ? call : perLine[e.n] > 1 ? call.replace(/\(\)$/, "") + ": " + val : val;
        L.querySelector(".cr-badges").insertAdjacentHTML("beforeend", `<span class="cr-b ${cls}" title="${esc(e.a)}" style="--d:${Math.round(k * gap)}ms">${esc(label)}</span>`);
      });
      const R = line(s.ln);
      // цепочка if / else if, по которой программа дошла до сработавшей ветки: условия выше — «не подошло», своё — «подошло».
      // Так видны и проверки без робота (sign.equals("RIGHT"), sign == "LEFT").
      for (const [n, okHere] of chainOf(s.ln)) {
        const L = line(n); if (!L) continue;
        if (!L.classList.contains("asked")) { L.classList.add("asked"); L.style.setProperty("--d", Math.round(chainDelay(n, events, gap)) + "ms"); }
        const bare = !L.querySelector(".cr-b"); // в строке не спрашивали робота — пишем словом, иначе хватит значка
        if (/\bif\s*\(/.test(L.textContent)) L.querySelector(".cr-badges").insertAdjacentHTML("beforeend",
          `<span class="cr-b ${okHere ? "go" : "stop"}" title="${okHere ? "условие подошло — выполняется эта ветка" : "условие не подошло — идём к следующему"}" style="--d:${Math.round(chainDelay(n, events, gap))}ms">${okHere ? "✓" : "✗"}${bare ? (okHere ? " да" : " нет") : ""}</span>`);
      }
      if (R) {
        R.classList.remove("asked"); R.classList.add("ran");
        const d = Math.round(events.length * gap) + "ms";
        R.style.setProperty("--d", d);
        R.querySelector(".cr-badges").insertAdjacentHTML("beforeend", `<span class="cr-b cmd" style="--d:${d}">▶ ${esc(s.cmd)}</span>`);
      }
      head.textContent = stepMode
        ? (R ? `Ход ${fi}: игра вызвала step. Жёлтым — что проверил код (✓ условие подошло, ✗ нет), зелёным — команда хода.` : `Ход ${fi}: игра вызвала step, но ни одна строка не дала команду.`)
        : (R ? `Ход ${fi}: жёлтым — проверки перед ходом (✓ подошло, ✗ нет), зелёным — строка, откуда пришла команда.` : `Ход ${fi}`);
      const target = R || line(events.length ? events[events.length - 1].n : -1);
      if (target) {
        const top = target.offsetTop - box.clientHeight / 2 + target.offsetHeight / 2;
        const out = target.offsetTop < box.scrollTop || target.offsetTop + target.offsetHeight > box.scrollTop + box.clientHeight;
        if (out) box.scrollTo({ top: Math.max(0, top), behavior: instant ? "auto" : "smooth" });
      }
    }

    function stop() { clearInterval(timer); timer = null; $(".cr-play").textContent = fi >= states.length - 1 ? "↻ Ещё раз" : "▶ Смотреть"; }
    function play() {
      if (fi >= states.length - 1) show(0, true);
      $(".cr-play").textContent = "⏸ Пауза";
      clearInterval(timer);
      timer = setInterval(() => {
        if (dead) return clearInterval(timer);
        if (fi >= states.length - 1) {
          if (opts.loop) { clearInterval(timer); timer = null; setTimeout(() => { if (!dead && host.isConnected) { show(0, true); play(); } }, 1600); return; }
          return stop();
        }
        show(fi + 1);
      }, opts.speed || +$(".cr-speed").value);
    }

    function select(i) {
      stop();
      cur = i; replay = replays[i];
      host.querySelectorAll(".cr-tab").forEach((b) => b.classList.toggle("on", +b.dataset.i === i));
      if (!replay) { $(".cr-board").innerHTML = ""; $(".cr-msg").innerHTML = `<div class="result-banner bad">${esc((tests[i] && tests[i].error) || "Поездки нет")}</div>`; return; }
      states = build(replay);
      drawBoard(replay);
      $(".cr-range").max = states.length - 1;
      const ok = replay.result === "finish";
      $(".cr-msg").innerHTML = `<div class="result-banner ${ok ? "ok" : "bad"}">${ok ? "✓ " : ""}${esc(replay.message || "")}</div>`;
      const out = (replay.output || "").trim();
      $(".cr-out").innerHTML = out || replay.expected
        ? `<div class="cr-printed"><small>Программа напечатала</small><pre>${esc(out) || "(ничего)"}</pre></div>${replay.expected ? `<div class="cr-printed"><small>Ожидалось</small><pre>${esc(replay.expected)}</pre></div>` : ""}`
        : "";
      show(0, true);
      if (states.length > 1) setTimeout(play, 350);
    }

    host.querySelectorAll(".cr-tab").forEach((b) => b.addEventListener("click", () => select(+b.dataset.i)));
    $(".cr-play").addEventListener("click", () => (timer ? stop() : play()));
    $(".cr-prev").addEventListener("click", () => { stop(); show(fi - 1); });
    $(".cr-next").addEventListener("click", () => { stop(); show(fi + 1); });
    $(".cr-first").addEventListener("click", () => { stop(); show(0, true); });
    $(".cr-last").addEventListener("click", () => { stop(); show(states.length - 1, true); });
    $(".cr-range").addEventListener("input", (e) => { stop(); show(+e.target.value, true); });
    $(".cr-speed").addEventListener("change", () => { if (timer) play(); });
    new ResizeObserver(fit).observe($(".cr-board-wrap"));
    const firstBad = replays.findIndex((p) => !p || p.result !== "finish");
    select(firstBad >= 0 ? firstBad : 0);
    return { stop() { dead = true; clearInterval(timer); timer = null; } };
  };

  /** Демо в начале урока: Джавик сам проезжает карту по кругу (только поле). Данные — JSON поездки внутри элемента. */
  W.courierdemo = function (el) {
    let data = null;
    try { data = JSON.parse(el.querySelector("script").textContent); } catch (e) { el.textContent = "Не удалось загрузить демо"; return; }
    el.classList.add("courier-demo");
    el.innerHTML = '<div class="cd-host"></div>';
    JC.courierReplay(el.querySelector(".cd-host"), { replays: [data] }, { compact: true, loop: true, speed: 330 });
  };

  /**
   * Игровая панель: вкладки уровней со звёздами (★ … ★★★), карта уровня видна сразу, кнопка запуска, три подсказки.
   * Данные: { "levels": [ { "task": "m04-c1", "name": "★ Коридор", "goal": "html", "hints": ["html", "html", "html"] } ] }
   */
  W.couriergame = function (el) {
    let data;
    try { data = JSON.parse(el.querySelector("script").textContent); } catch (e) { el.textContent = "Не удалось загрузить игру"; return; }
    const L = data.levels || [];
    el.classList.add("courier-game");
    el.innerHTML = `<div class="cg-levels" role="tablist" aria-label="Уровни"></div>
      <div class="cg-goal"></div>
      <div class="cg-board"></div>
      <div class="cg-run"><div class="cg-file"></div><button class="btn green cg-go">▶ Запустить мой код</button></div>
      <div class="cg-result"></div>
      <div class="cg-hints"></div>`;
    const $ = (q) => el.querySelector(q);
    let cur = 0, player = null, tasks = [], status = {};
    const preview = {};
    const passed = (id) => status[id] && status[id].status === "PASSED";
    const fileOf = (id) => { const t = tasks.find((x) => x.id === id); return t ? (/^workspace\//.test(t.file) ? t.file : "workspace/" + t.file) : ""; };

    // вкладка уровня: звёзды = сложность, затем название; пройденный — с ✓
    function dots() {
      $(".cg-levels").innerHTML = L.map((lv, i) => {
        const m = lv.name.match(/^(★+)s*(.*)$/), stars = m ? m[1] : "★".repeat(i + 1), name = m ? m[2] : lv.name;
        return `<button class="cg-level ${i === cur ? "on" : ""} ${passed(lv.task) ? "ok" : ""}" role="tab" aria-selected="${i === cur}" data-i="${i}">`
          + `<span class="cg-stars">${stars}</span> ${esc(name)}${passed(lv.task) ? ' <span class="cg-done">✓</span>' : ""}</button>`;
      }).join("");
      el.querySelectorAll(".cg-level").forEach((b) => b.addEventListener("click", () => go(+b.dataset.i)));
    }
    function showBoard(r, opts) {
      if (player) player.stop();
      player = JC.courierReplay($(".cg-board"), r, opts);
    }
    async function loadPreview(id) {
      if (!preview[id]) preview[id] = fetch("/api/courier/preview/" + id).then((x) => x.json()).catch(() => null);
      const p = await preview[id];
      if (L[cur].task !== id) return;
      if (p && p.grid) showBoard({ replays: [p] }, { compact: true });
      else $(".cg-board").innerHTML = `<div class="result-banner bad">Карта не загрузилась — сервер курса запущен? (docker compose up -d)</div>`;
    }
    function go(i) {
      cur = Math.max(0, Math.min(L.length - 1, i));
      const lv = L[cur];
      el.dataset.task = lv.task; // карточка «Проверь решение» внизу урока берёт текущий уровень
      $(".cg-goal").innerHTML = lv.goal;
      $(".cg-file").innerHTML = `📄 Пиши код в IntelliJ IDEA: <code>${esc(fileOf(lv.task) || "…")}</code> → сохрани (Ctrl+S) → жми кнопку`;
      $(".cg-result").innerHTML = "";
      $(".cg-go").textContent = "▶ Запустить мой код";
      $(".cg-hints").innerHTML = (lv.hints || []).map((h, k) => `<details class="solution cg-hint"><summary>💡 Подсказка ${k + 1}${["  — с чего начать", "  — начало кода", "  — почти готовый код"][k] || ""}</summary><div>${h}</div></details>`).join("");
      JC.highlightAll && JC.highlightAll($(".cg-hints"));
      dots();
      if (player) { player.stop(); player = null; }
      $(".cg-board").innerHTML = `<div class="cg-loading">🗺 Загружаю карту уровня…</div>`;
      loadPreview(lv.task);
    }
    /** Под неудачным запуском — «🤖 Спросить ИИ»: промпт собирается сам (правила игры, цель, что случилось, код ученика). */
    function askAi(lv, r) {
      const box = document.createElement("div");
      box.className = "cg-ask";
      if (JC.mode !== "free") {
        box.innerHTML = `🤖 Застрял? Спроси наставника в Claude Code: <code>/hint ${esc(lv.task)}</code> — он сам посмотрит твой код.`;
        $(".cg-result").appendChild(box);
        return;
      }
      const tool = JC.aiTool || { name: "ИИ", url: "#" };
      box.innerHTML = `<button class="btn small green cg-ask-copy">🤖 Скопировать вопрос для ${esc(tool.name)}</button>
        <a class="btn small ghost" href="${esc(tool.url)}" target="_blank" rel="noopener">Открыть ${esc(tool.name)} ↗</a>
        <span class="muted">цель уровня, что пошло не так и твой код уже внутри — просто вставь в чат</span>`;
      $(".cg-result").appendChild(box);
      box.querySelector(".cg-ask-copy").addEventListener("click", async (e) => {
        const b = e.currentTarget;
        const text = JC.aiPrompt ? JC.aiPrompt(await gameQuestion(lv, r)) : await gameQuestion(lv, r);
        await JC.copyText(text);
        b.textContent = text.length > 6000 ? `✓ Скопировано, но длинно: ${text.length} символов` : "✓ Скопировано — вставь в чат";
        setTimeout(() => { b.textContent = `🤖 Скопировать вопрос для ${tool.name}`; }, 3000);
      });
    }
    const PART_OF = { m04: 1, m05: 2, m06: 3, m07: 4, m13: 5 };
    async function gameQuestion(lv, r) {
      const part = PART_OF[lv.task.slice(0, 3)] || 1;
      const text = (h) => { const d = document.createElement("div"); d.innerHTML = String(h || "").replace(/<\/p>/g, " </p>"); return d.textContent.replace(/\s+/g, " ").trim().replace(/^Цель:\s*/, ""); };
      const src = await fetch("/api/source/" + lv.task, { cache: "no-store" }).then((x) => x.json()).catch(() => ({}));
      const lines = [`Я прохожу игру «Джавик-курьер» из курса, часть ${part}, уровень «${lv.name}».`,
        "Как устроена игра: робот Джавик ездит по клеточной карте, я пишу для него код в IntelliJ IDEA, сайт запускает его на нескольких картах.",
        part === 1 ? "Игра на каждом ходу сама вызывает мой метод step(robot). За один вызов должна сработать ровно одна команда — один ход."
          : "Игра один раз вызывает мой метод run(robot), дальше я сам веду Джавика циклами до конца уровня.",
        "Датчики робота: wallAhead(), wallLeft(), wallRight() — стена впереди / слева / справа от Джавика; parcelHere() — тут лежит посылка; carrying() — везёт посылку; atTarget() — стоит у адреса; atDepot() — на складе."
          + (part === 1 ? " pitAhead() — прямо впереди яма (яма — не стена: wallAhead() перед ней false)." : ""),
        "Команды: move() — клетка вперёд, turnLeft() и turnRight() — поворот на месте, take() — взять посылку, drop() — отдать."
          + (part === 1 ? " jump() — перепрыгнуть яму впереди (move() в яму — падение, jump() без ямы — ошибка)." : "")];
      if (part >= 4) lines.push("Ещё: houses() — массив, на скольких клетках от склада стоит каждый дом.");
      if (part === 5) lines.push("Ещё: orders() — список адресов заказов, newOrders() — новые заказы (приходят после доставок), houseName() — адрес дома, у которого стоит Джавик, goTo(адрес) — автопилот до дома.");
      lines.push("", "Цель уровня: " + text(lv.goal));
      if (r.status === "compile_error") lines.push("", "Что случилось: код не компилируется. Ошибка javac:", (r.compilerOutput || "").split("\n").slice(0, 8).join("\n"));
      else {
        const bad = (r.replays || []).find((x) => x && x.result !== "finish");
        const why = bad ? bad.message : r.message;
        lines.push("", "Что случилось: " + (why || "уровень не засчитан"));
        const fr = bad && bad.frames && bad.frames[bad.frames.length - 1];
        if (fr && bad.frames.length > 1) lines.push(`Последний ход (${bad.frames.length}):Джавик спросил ${(fr.ask || []).join(", ") || "ничего"} и сделал ${fr.cmd}.`);
        const rf = (r.ruleFails || []).concat(r.hints || []);
        if (rf.length) lines.push("Сайт подсказывает: " + rf.join(" "));
      }
      lines.push("", "Подскажи, куда смотреть в моём коде. Готовый код не пиши.", "",
        `Мой код${src.file ? " (" + src.file + ")" : ""}:`, "```java", src.status === "ok" ? src.code.trim() : "(не удалось прочитать файл — вставь код сам)", "```");
      return lines.join("\n");
    }
    $(".cg-go").addEventListener("click", async () => {
      const lv = L[cur], b = $(".cg-go");
      b.disabled = true; b.textContent = "⏳ Джавик выезжает…";
      try {
        const r = await fetch("/api/check/" + lv.task, { method: "POST" }).then((x) => x.json());
        let html = "";
        if (r.status === "passed") {
          status[lv.task] = { status: "PASSED" };
          html = `<div class="result-banner ok">🎉 Уровень пройден на всех картах!${cur < L.length - 1 ? ' <button class="btn cg-next-level">Следующий уровень ▶</button>' : " Это последний уровень части."}</div>`;
          try { JC.confetti(50); } catch (e) { /* без конфетти — не страшно */ }
        } else if (r.status === "missing") html = `<div class="result-banner info">📄 ${esc(JC.msg(r.message))}</div>`;
        else if (r.status === "compile_error") html = `<div class="result-banner bad">🛠 Код не компилируется — посмотри на первую ошибку (номер строки после имени файла):</div><pre class="terminal"><span class="err">${esc(r.compilerOutput || "")}</span></pre>`;
        else if (!r.replays) html = `<div class="result-banner bad">${esc(JC.msg(r.message || "Не получилось"))}</div>`; // у поездки своё сообщение с причиной
        else if ((r.ruleFails || []).length && r.replays.every((x) => x && x.result === "finish"))
          html = `<div class="result-banner bad">🚚 Джавик доехал, но уровень не засчитан — код не выполнил условие задания:</div>`;
        const hints = [...(r.hints || []), ...(r.ruleFails || [])];
        if (hints.length) html += `<ul class="hints">${hints.map((h) => `<li>💡 ${esc(h)}</li>`).join("")}</ul>`;
        $(".cg-result").innerHTML = html;
        if (r.status !== "passed" && r.status !== "missing") askAi(lv, r);
        const nx = el.querySelector(".cg-next-level");
        if (nx) nx.addEventListener("click", () => { go(cur + 1); el.scrollIntoView({ behavior: "smooth", block: "start" }); });
        if (r.replays) showBoard(r, {});
        else loadPreview(lv.task); // код не запустился — на доске снова чистая карта, а не прошлая поездка
        dots();
      } catch (e) {
        $(".cg-result").innerHTML = `<div class="result-banner bad">Сервер курса не отвечает. Запусти его: docker compose up -d</div>`;
      }
      b.disabled = false; b.textContent = "▶ Запустить ещё раз";
    });
    Promise.all([fetch("/api/tasks").then((x) => x.json()).catch(() => []), fetch("/api/status", { cache: "no-store" }).then((x) => x.json()).catch(() => ({}))])
      .then(([t, st]) => {
        tasks = t; status = st || {};
        const open = L.findIndex((lv) => !passed(lv.task)); // открываем первый нерешённый уровень
        go(open < 0 ? cur : open);
      });
    // карты всех уровней — заранее, чтобы стрелки переключали мгновенно
    L.forEach((lv) => { if (!preview[lv.task]) preview[lv.task] = fetch("/api/courier/preview/" + lv.task).then((x) => x.json()).catch(() => null); });
    go(0);
  };

  /** Блок в уроке: уровни части, файл и кнопка «▶ Запустить мой код». */
  W.courier = function (el) {
    const ids = (el.dataset.tasks || "").split(",").map((s) => s.trim()).filter(Boolean);
    el.classList.add("courier-run");
    el.innerHTML = `<div class="cw-head"><b>🚚 Запусти своего Джавика</b><span class="muted">код пишешь в IntelliJ IDEA, смотришь здесь</span></div>
      <div class="cw-levels"></div><div class="cw-info"></div>
      <button class="btn green cw-run">▶ Запустить мой код</button><div class="cw-result"></div>`;
    const $ = (q) => el.querySelector(q);
    const stars = ["★", "★★", "★★★"];
    let tasks = [], sel = 0;
    function paint() {
      $(".cw-levels").innerHTML = ids.map((id, i) => {
        const t = tasks.find((x) => x.id === id);
        return `<button class="cw-level ${i === sel ? "on" : ""}" data-i="${i}">${stars[i] || ""} ${esc(t ? t.title.replace(/^.*?:\s*/, "") : "уровень " + (i + 1))}</button>`;
      }).join("");
      const t = tasks.find((x) => x.id === ids[sel]);
      $(".cw-info").innerHTML = t ? `📄 Файл: <code>${esc(/^workspace\//.test(t.file) ? t.file : "workspace/" + t.file)}</code> · класс <code>${esc(t.className)}</code> · <a href="/tasks.html?module=${esc(ids[sel].slice(0, 3))}#${esc(ids[sel])}">условие в «Практике»</a>` : "";
      el.querySelectorAll(".cw-level").forEach((b) => b.addEventListener("click", () => { sel = +b.dataset.i; $(".cw-result").innerHTML = ""; paint(); }));
    }
    fetch("/api/tasks").then((r) => r.json()).then((list) => { tasks = list; paint(); })
      .catch(() => { $(".cw-info").innerHTML = `<div class="result-banner bad">Сервер курса не отвечает. Запусти его: docker compose up -d</div>`; });
    paint();
    $(".cw-run").addEventListener("click", async () => {
      const b = $(".cw-run");
      b.disabled = true; b.textContent = "⏳ Компилирую и запускаю…";
      try {
        const r = await fetch("/api/check/" + ids[sel], { method: "POST" }).then((x) => x.json());
        let html = "";
        if (r.status === "passed") html = `<div class="result-banner ok">🎉 ${esc(JC.msg(r.message))}</div>`;
        else if (r.status === "missing") html = `<div class="result-banner info">📄 ${esc(JC.msg(r.message))}</div>`;
        else if (r.status === "compile_error") html = `<div class="result-banner bad">🛠 ${esc(JC.msg(r.message))}</div><pre class="terminal"><span class="err">${esc(r.compilerOutput || "")}</span></pre>`;
        else html = `<div class="result-banner bad">${esc(JC.msg(r.message || "Что-то пошло не так"))}</div>`;
        const hints = [...(r.hints || []), ...(r.ruleFails || [])];
        if (hints.length) html += `<ul class="hints">${hints.map((h) => `<li>💡 ${esc(h)}</li>`).join("")}</ul>`;
        $(".cw-result").innerHTML = html + `<div class="cw-replay"></div>`;
        JC.courierReplay($(".cw-replay"), r);
        if (r.status === "passed") { try { JC.confetti(50); } catch (e) { /* без конфетти — не страшно */ } }
      } catch (e) {
        $(".cw-result").innerHTML = `<div class="result-banner bad">Сервер курса не отвечает. Запусти его: docker compose up -d</div>`;
      }
      b.disabled = false; b.textContent = "▶ Запустить ещё раз";
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
      for (const t of r.replays ? [] : r.tests || []) {
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
        if (r.replays) { $(".tc-result").insertAdjacentHTML("beforeend", "<div class=\"tc-replay\"></div>"); JC.courierReplay($(".tc-replay"), r); }
        if (r.status === "passed") { try { if (JC.confetti) JC.confetti(50); } catch (e) { /* без конфетти — не страшно */ } }
      } catch (e) {
        $(".tc-result").innerHTML = `<div class="result-banner bad">Сервер курса не отвечает. Запусти его: docker compose up -d</div>`;
      }
      b.disabled = false; b.textContent = "▶ Проверить ещё раз";
    });
  };
})();
