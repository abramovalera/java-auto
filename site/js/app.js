// Ядро сайта: шапка, меню-дорожка, шаги урока, XP/серия, маскот, квизы, подсветка Java.
(function () {
  "use strict";
  const COURSE = window.COURSE;

  // ---------------- хранилище ----------------
  // Источник правды для прогресса — файл на сервере курса (workspace/.progress/progress.json).
  // localStorage — только быстрый кэш; если он недоступен, работаем из памяти и всё равно сохраняем на сервер.
  const SYNC_KEY = /^(jc\.(read|awards|days|xp|step\..+)|w:.+)$/;
  const mem = {};
  let lsOk = true;
  try { const t = "__jc_test"; localStorage.setItem(t, "1"); localStorage.removeItem(t); } catch (e) { lsOk = false; }
  // профиль: у каждого ученика свой прогресс. Ключи в браузере — с приставкой «p:<id>:», кроме общих настроек.
  const PROFILE_KEY = "jc.profile";
  const GLOBAL_KEY = /^(jc\.profile$|jc\.ui\.|jc\.theme$|__)/;
  let profileId = null;
  try { profileId = localStorage.getItem(PROFILE_KEY); } catch (e) { /* без localStorage профиль живёт до перезагрузки */ }
  const prefix = () => "p:" + (profileId || "none") + ":";
  const phys = (k) => (GLOBAL_KEY.test(k) ? k : prefix() + k);
  const rawGet = (k) => { const p = phys(k); if (lsOk) { try { return localStorage.getItem(p); } catch (e) { /* fallthrough */ } } return p in mem ? mem[p] : null; };
  const rawSet = (k, v) => { const p = phys(k); mem[p] = v; if (lsOk) { try { localStorage.setItem(p, v); } catch (e) { /* место кончилось — останется в памяти */ } } };
  const allKeys = () => {
    const keys = new Set(Object.keys(mem));
    if (lsOk) { try { for (let i = 0; i < localStorage.length; i++) keys.add(localStorage.key(i)); } catch (e) { /* ignore */ } }
    const pre = prefix();
    return [...keys].filter((k) => k.startsWith(pre)).map((k) => k.slice(pre.length));
  };
  // все запросы прогресса и проверок уходят с профилем
  const withProfile = (url) => (profileId && /^\/api\/(progress|status|check\/)/.test(url) ? url + (url.includes("?") ? "&" : "?") + "profile=" + encodeURIComponent(profileId) : url);
  const nativeFetch = window.fetch.bind(window);
  window.fetch = (input, init) => nativeFetch(typeof input === "string" ? withProfile(input) : input, init);
  const store = {
    get(k, d) { const v = rawGet(k); if (v === null) return d; try { return JSON.parse(v); } catch (e) { return d; } },
    set(k, v) { rawSet(k, JSON.stringify(v)); if (SYNC_KEY.test(k)) schedulePush(); }
  };
  const JC = (window.JC = { store, widgets: {} });

  // ---------------- синхронизация прогресса с сервером ----------------
  // Слияние «только прибавление»: списки объединяются, числа — максимум. Так же делает сервер.
  function mergeVal(a, b) {
    if (a === undefined || a === null) return b;
    if (b === undefined || b === null) return a;
    if (Array.isArray(a) && Array.isArray(b)) {
      const seen = new Set(a.map((x) => JSON.stringify(x))), out = a.slice();
      b.forEach((x) => { const s = JSON.stringify(x); if (!seen.has(s)) { seen.add(s); out.push(x); } });
      return out;
    }
    if (typeof a === "number" && typeof b === "number") return Math.max(a, b);
    if (typeof a === "object" && typeof b === "object" && !Array.isArray(a) && !Array.isArray(b)) {
      const out = { ...a }; for (const k in b) out[k] = mergeVal(a[k], b[k]); return out;
    }
    return b;
  }
  function snapshot() {
    const o = {};
    allKeys().forEach((k) => { if (SYNC_KEY.test(k)) { const v = store.get(k, null); if (v !== null) o[k] = v; } });
    return o;
  }
  let pushTimer = null, dirty = false;
  function schedulePush() { dirty = true; clearTimeout(pushTimer); pushTimer = setTimeout(pushNow, 700); }
  async function pushNow() {
    clearTimeout(pushTimer);
    if (!dirty) return;
    dirty = false;
    try {
      const r = await fetch("/api/progress", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(snapshot()), keepalive: true });
      if (!r.ok) throw new Error("HTTP " + r.status);
    } catch (e) {
      dirty = true; // сервер недоступен — попробуем ещё раз позже
      pushTimer = setTimeout(pushNow, 5000);
    }
  }
  // уходим со страницы — досылаем несохранённое
  const flush = () => {
    if (!dirty) return;
    try {
      const ok = navigator.sendBeacon && navigator.sendBeacon(withProfile("/api/progress"), new Blob([JSON.stringify(snapshot())], { type: "application/json" }));
      if (ok) dirty = false; else pushNow();
    } catch (e) { pushNow(); }
  };
  addEventListener("pagehide", flush);
  document.addEventListener("visibilitychange", () => { if (document.visibilityState === "hidden") flush(); });

  async function syncFromServer() {
    let server;
    try {
      const ctrl = new AbortController();
      const t = setTimeout(() => ctrl.abort(), 2500);
      const r = await fetch("/api/progress", { cache: "no-store", signal: ctrl.signal });
      clearTimeout(t);
      if (!r.ok) return;
      server = await r.json();
    } catch (e) { return; } // сервер недоступен — работаем на локальном кэше, отправим позже
    const local = snapshot();
    const keys = new Set([...Object.keys(server), ...Object.keys(local)]);
    let localHasMore = false;
    keys.forEach((k) => {
      if (!SYNC_KEY.test(k)) return;
      const merged = mergeVal(server[k], local[k]);
      rawSet(k, JSON.stringify(merged));
      if (JSON.stringify(merged) !== JSON.stringify(server[k])) localHasMore = true;
    });
    if (localHasMore) { dirty = true; pushNow(); }
  }
  // сообщения сервера про наставника — в режиме без подписки заменяем на совет обсудить решение с ИИ
  JC.msg = (m) => (JC.mode === "free" && m ? String(m)
    .replace(/Покажи[^.:]*наставнику: \/check \S+/g, "Обсуди решение с ИИ — готовый промпт «Проверь решение» есть в уроке")
    .replace(/Эту задачу проверяет наставник: напиши в чате \/check \S+/g, "Эту задачу проверяешь с ИИ: в уроке есть готовый промпт «Проверь решение»") : m);
  const esc = (s) => String(s ?? "").replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;");
  JC.esc = esc;

  // ---------------- тема ----------------
  const savedTheme = store.get("jc.theme", null);
  if (savedTheme) document.documentElement.dataset.theme = savedTheme;
  function toggleTheme() {
    const cur = document.documentElement.dataset.theme || (matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light");
    const next = cur === "dark" ? "light" : "dark";
    document.documentElement.dataset.theme = next;
    store.set("jc.theme", next);
  }

  // ---------------- маскот Джавик: кружка кофе ----------------
  JC.mascot = function (mood = "happy") {
    const eyes = {
      happy: `<circle cx="25" cy="37" r="4.6" fill="#fff"/><circle cx="39" cy="37" r="4.6" fill="#fff"/><circle cx="25.8" cy="38" r="2.5" fill="#1e2033"/><circle cx="39.8" cy="38" r="2.5" fill="#1e2033"/><circle cx="26.6" cy="37" r=".9" fill="#fff"/><circle cx="40.6" cy="37" r=".9" fill="#fff"/>`,
      think: `<circle cx="25" cy="37" r="4.6" fill="#fff"/><circle cx="39" cy="37" r="4.6" fill="#fff"/><circle cx="26.5" cy="35.5" r="2.5" fill="#1e2033"/><circle cx="40.5" cy="35.5" r="2.5" fill="#1e2033"/><path d="M20 30.5l8-1.5M36 29l8 1.5" stroke="#5a2a10" stroke-width="2" stroke-linecap="round"/>`,
      wow: `<circle cx="25" cy="37" r="5.4" fill="#fff"/><circle cx="39" cy="37" r="5.4" fill="#fff"/><circle cx="25" cy="37" r="2.2" fill="#1e2033"/><circle cx="39" cy="37" r="2.2" fill="#1e2033"/>`,
      oops: `<circle cx="25" cy="37" r="4.6" fill="#fff"/><circle cx="39" cy="37" r="4.6" fill="#fff"/><circle cx="25" cy="38.3" r="2.4" fill="#1e2033"/><circle cx="39" cy="38.3" r="2.4" fill="#1e2033"/><path d="M20 31l8 2M36 33l8-2" stroke="#5a2a10" stroke-width="2" stroke-linecap="round"/>`,
      cool: `<rect x="17" y="32" width="14" height="8" rx="3" fill="#1e2033"/><rect x="33" y="32" width="14" height="8" rx="3" fill="#1e2033"/><path d="M31 35h2" stroke="#1e2033" stroke-width="2"/><path d="M20 34l5-1" stroke="#fff" stroke-width="1.4" opacity=".7"/>`
    }[mood] || "";
    const mouth = {
      happy: `<path d="M27.5 45q4.5 4.5 9 0" stroke="#1e2033" stroke-width="2.3" fill="#8a2b0f" stroke-linecap="round"/>`,
      think: `<path d="M29 47h6" stroke="#1e2033" stroke-width="2.3" stroke-linecap="round"/><text x="50" y="16" font-size="13" font-weight="900" fill="#5b5bd6" font-family="Nunito,sans-serif">?</text>`,
      wow: `<ellipse cx="32" cy="47" rx="3.4" ry="4" fill="#8a2b0f" stroke="#1e2033" stroke-width="2"/>`,
      oops: `<path d="M28 48q4-3.5 8 0" stroke="#1e2033" stroke-width="2.3" fill="none" stroke-linecap="round"/><path d="M47 27q2 4 0 6q-2-2 0-6z" fill="#7cc6ff"/>`,
      cool: `<path d="M27 45q5 4 10-1" stroke="#1e2033" stroke-width="2.3" fill="none" stroke-linecap="round"/>`
    }[mood] || "";
    return `<svg viewBox="0 0 64 64" aria-hidden="true">
      <path d="M22 9c-3 4 3 5 0 9M32 6c-3 4 3 5 0 9M42 9c-3 4 3 5 0 9" stroke="#c9bfdc" stroke-width="2.6" fill="none" stroke-linecap="round"/>
      <ellipse cx="32" cy="58" rx="24" ry="4.5" fill="#d9d4ea"/>
      <path d="M50 29h3.5a8 8 0 0 1 0 16H49" fill="none" stroke="#d9530a" stroke-width="4.5" stroke-linecap="round"/>
      <path d="M11 23h42v19a15 15 0 0 1-15 15H26a15 15 0 0 1-15-15z" fill="#ff6b1a"/>
      <path d="M14 26h6v16a10 10 0 0 0 4 8" stroke="#fff" stroke-width="2.5" fill="none" opacity=".35" stroke-linecap="round"/>
      <ellipse cx="32" cy="23" rx="21" ry="4" fill="#6b3a1e"/>
      <ellipse cx="32" cy="22.2" rx="17" ry="2.2" fill="#8a5130"/>
      <circle cx="19.5" cy="44" r="3" fill="#ff9fb0" opacity=".8"/><circle cx="44.5" cy="44" r="3" fill="#ff9fb0" opacity=".8"/>
      ${eyes}${mouth}</svg>`;
  };

  // ---------------- XP, серия дней, тосты, конфетти ----------------
  const fmtDay = (d) => d.getFullYear() + "-" + String(d.getMonth() + 1).padStart(2, "0") + "-" + String(d.getDate()).padStart(2, "0");
  function touchDay() { const s = new Set(store.get("jc.days", [])); s.add(fmtDay(new Date())); store.set("jc.days", [...s].slice(-500)); }
  JC.streak = function () {
    const s = new Set(store.get("jc.days", [])); const d = new Date(); let n = 0;
    if (!s.has(fmtDay(d))) d.setDate(d.getDate() - 1);
    while (s.has(fmtDay(d))) { n++; d.setDate(d.getDate() - 1); }
    return n;
  };
  JC.xp = () => store.get("jc.xp", 0);
  JC.award = function (key, xp, label) {
    const got = new Set(store.get("jc.awards", []));
    if (got.has(key)) return false;
    got.add(key); store.set("jc.awards", [...got]);
    store.set("jc.xp", JC.xp() + xp); touchDay();
    JC.toast("✓", label);
    return true;
  };
  let toastTimer;
  JC.toast = function (big, text) {
    let t = document.querySelector(".toast");
    if (!t) { t = document.createElement("div"); t.className = "toast"; document.body.appendChild(t); }
    t.innerHTML = `<b>${esc(big)}</b><span>${esc(text || "")}</span>`;
    requestAnimationFrame(() => t.classList.add("show"));
    clearTimeout(toastTimer); toastTimer = setTimeout(() => t.classList.remove("show"), 2600);
  };
  JC.confetti = function (n = 70) {
    if (matchMedia("(prefers-reduced-motion: reduce)").matches) return;
    const colors = ["#ff6b1a", "#5b5bd6", "#1fb35a", "#f2a20c", "#e5488a", "#2f80ed"];
    for (let i = 0; i < n; i++) {
      const c = document.createElement("i");
      c.className = "confetti";
      c.style.left = Math.random() * 100 + "vw";
      c.style.background = colors[i % colors.length];
      c.style.animationDuration = 1.6 + Math.random() * 1.6 + "s";
      c.style.animationDelay = Math.random() * .4 + "s";
      c.style.transform = `rotate(${Math.random() * 360}deg)`;
      document.body.appendChild(c);
      setTimeout(() => c.remove(), 3800);
    }
  };
  function paintStats(bump) {
    const xp = document.querySelector(".pill.xp .v"); if (xp) xp.textContent = JC.xp();
    const fire = document.querySelector(".pill.fire .v"); if (fire) fire.textContent = JC.streak();
    if (bump) { const p = document.querySelector(".pill.xp"); if (p) { p.classList.remove("bump"); void p.offsetWidth; p.classList.add("bump"); } }
  }

  // ---------------- прогресс: уроки и задачи ----------------
  const readSet = () => new Set(store.get("jc.read", []));
  JC.readLessons = readSet;
  function setRead(id) { const s = readSet(); s.add(id); store.set("jc.read", [...s]); }
  const taskStatus = () => store.get("jc.tasks", {});
  function moduleProgress(m) {
    const read = readSet();
    const lessonsDone = m.lessons.filter((l) => read.has(l.id)).length;
    const ts = taskStatus();
    const tasksDone = Object.keys(ts).filter((id) => id.startsWith(m.id + "-") && (ts[id] === "PASSED" || ts[id] === "MANUAL")).length;
    const total = m.lessons.length + (m.taskCount || 0);
    return { lessonsDone, tasksDone, total, done: lessonsDone + tasksDone, pct: total ? (lessonsDone + tasksDone) / total : 0 };
  }
  JC.moduleProgress = moduleProgress;
  async function refreshTasks() {
    try {
      const r = await fetch("/api/status", { cache: "no-store" });
      if (!r.ok) return;
      const data = await r.json();
      const map = {}; for (const k in data) map[k] = data[k].status;
      store.set("jc.tasks", map);
      renderSidebar(); document.dispatchEvent(new CustomEvent("jc:tasks"));
    } catch (e) { /* сервер не запущен — используем сохранённое */ }
  }

  /**
   * В уроках примеры путей и логов сделаны на условного «Misha»: C:\Users\Misha\IdeaProjects\…, автор коммита Misha.
   * Подставляем имя профиля ученика — так скриншоты «про него». Настоящий путь к папке курса показывает только урок 0.3.
   */
  function personalize(main) {
    const name = ((JC.profile && JC.profile.name) || "").trim();
    if (!name || name === "Misha") return;
    const json = JSON.stringify(name).slice(1, -1);
    // JSON скриншотов IDEA (виджеты ещё не нарисованы)
    main.querySelectorAll('script[type="application/json"]').forEach((sc) => {
      if (sc.textContent.includes("Misha")) sc.textContent = sc.textContent.split("Misha").join(json);
    });
    // обычный текст урока
    const walker = document.createTreeWalker(main, NodeFilter.SHOW_TEXT);
    const nodes = [];
    while (walker.nextNode()) if (walker.currentNode.nodeValue.includes("Misha") && !walker.currentNode.parentElement.closest("script")) nodes.push(walker.currentNode);
    nodes.forEach((n) => { n.nodeValue = n.nodeValue.split("Misha").join(name); });
  }

  /** Куда вести с «/» и с логотипа: последний открытый урок; если его нет — первый непрочитанный; всё прочитано — шпаргалка. */
  function resumeUrl() {
    const lessons = flatLessons().filter((l) => l.index !== undefined);
    const last = lessons.find((l) => l.id === store.get("jc.lastLesson", null));
    if (last) return last.url;
    const read = readSet();
    const next = lessons.find((l) => !read.has(l.id));
    if (next) return next.url;
    return read.size ? "/cheatsheet.html" : "/lessons/m00/01-how-it-works.html";
  }

  // ---------------- поиск текущего урока ----------------
  function flatLessons() {
    const list = [];
    for (const m of COURSE.modules) {
      if (m.status !== "open") continue;
      m.lessons.forEach((l, i) => {
        if (m.tasks && i === m.lessons.length - 1) list.push({ id: "tasks-" + m.id, title: "Практика: задачи модуля " + m.num, url: m.tasks, module: m });
        list.push({ ...l, module: m, index: i });
      });
    }
    return list;
  }
  let current = "", currentModule = null;

  // ---------------- шапка и меню ----------------
  const ringSvg = (pct, color) => {
    const r = 19, c = 2 * Math.PI * r;
    return `<svg viewBox="0 0 46 46"><circle cx="23" cy="23" r="${r}" fill="none" stroke="var(--surface-2)" stroke-width="6"/>
      <circle cx="23" cy="23" r="${r}" fill="none" stroke="${color}" stroke-width="6" stroke-linecap="round" stroke-dasharray="${c}" stroke-dashoffset="${c * (1 - pct)}"/></svg>`;
  };
  JC.ringSvg = ringSvg;

  /** Меню слева в разделе «Тренинг код»: темы с прогрессом и уровнями — как отдельное приложение. */
  function renderTrainingSidebar(side) {
    const ts = taskStatus();
    const curTopic = document.querySelector("main.content").dataset.topic || "";
    let html = `<a class="side-cheat ${!curTopic ? "active" : ""}" href="/training.html">🏋️ Все темы тренинга</a>`;
    for (const t of window.TRAINING || []) {
      const solved = Object.keys(ts).filter((id) => id.startsWith(t.id + "-") && ts[id] === "PASSED").length;
      const pct = t.taskCount ? solved / t.taskCount : 0, complete = t.taskCount && solved >= t.taskCount;
      const after = t.after && COURSE.modules.find((m) => m.id === t.after);
      html += `<a class="mod tr-side ${t.id === curTopic ? "current" : ""} ${complete ? "complete" : ""}" href="/tasks.html?module=${t.id}">
          <span class="mod-head"><span class="ring">${ringSvg(pct, complete ? "var(--green)" : "var(--green)")}<b>${t.icon}</b></span>
          <span><span class="mod-title">${esc(t.title)}</span>
            <span class="mod-sub"><span class="tr-lvs"><span>🟢 ${t.levels[1] || 0}</span><span>🟡 ${t.levels[2] || 0}</span><span>✓ ${solved}/${t.taskCount}</span></span>${after ? `после модуля ${after.num}` : ""}
            <span class="bar"><i style="width:${Math.round(pct * 100)}%"></i></span></span></span></span></a>`;
    }
    side.innerHTML = html;
  }

  function renderSidebar() {
    const side = document.querySelector(".sidebar");
    if (!side) return;
    if (current === "training") return renderTrainingSidebar(side);
    const read = readSet(), ts = taskStatus(), openState = store.get("jc.open", {});
    let html = ""; // шпаргалка — вкладка «📌 Шпаргалка» в шапке
    for (const m of COURSE.modules) {
      if (m.status !== "open") continue;
      const p = moduleProgress(m);
      const complete = p.done === p.total;
      const isCur = currentModule === m;
      const open = openState[m.id] !== undefined ? openState[m.id] : isCur;
      html += `<div class="mod ${open ? "open" : ""} ${isCur ? "current" : ""} ${complete ? "complete" : ""}" data-mod="${m.id}">
        <button class="mod-head" aria-expanded="${open}">
          <span class="ring">${ringSvg(p.pct, complete ? "var(--green)" : "var(--primary)")}<b>${m.num}</b></span>
          <span><span class="mod-title">${esc(m.title)}</span>
            <span class="mod-sub">уроки ${p.lessonsDone}/${m.lessons.length}${m.taskCount ? ` · задачи ${p.tasksDone}/${m.taskCount}` : ""}
            <span class="bar"><i style="width:${Math.round(p.pct * 100)}%"></i></span></span></span>
          <span class="chev">▶</span>
        </button><div class="mod-body">`;
      m.lessons.forEach((l, i) => {
        if (m.tasks && i === m.lessons.length - 1) {
          const allDone = m.taskCount && p.tasksDone >= m.taskCount;
          html += `<a class="node task ${allDone ? "done" : ""} ${current === "tasks-" + m.id ? "active" : ""}" href="${m.tasks}"><span class="dot">${allDone ? "✓" : "⚑"}</span><span>Практика: задачи <small>(${p.tasksDone}/${m.taskCount || "?"})</small></span></a>`;
        }
        const d = read.has(l.id);
        html += `<a class="node ${d ? "done" : ""} ${l.id === current ? "active" : ""}" href="${l.url}"><span class="dot">${d ? "✓" : i + 1}</span><span>${esc(l.title)}</span></a>`;
      });
      html += `</div></div>`;
    }
    const soon = COURSE.modules.filter((m) => m.status !== "open");
    if (soon.length) html += `<div class="soon-list"><h5>Скоро откроются</h5>` + soon.map((m) => `<div class="soon-item"><span>🔒</span>${m.num}. ${esc(m.title)}</div>`).join("") + `</div>`;
    side.innerHTML = html;
    side.querySelectorAll(".mod-head").forEach((b) => b.addEventListener("click", () => {
      const mod = b.closest(".mod"); mod.classList.toggle("open");
      if (mod.classList.contains("open")) side.scrollTo({ top: mod.offsetTop - 8, behavior: "smooth" });
      b.setAttribute("aria-expanded", mod.classList.contains("open"));
      const st = store.get("jc.open", {}); st[mod.dataset.mod] = mod.classList.contains("open"); store.set("jc.open", st);
    }));
  }

  function buildShell(main) {
    current = main.dataset.lesson || main.dataset.page || "";
    const flat = flatLessons();
    const me = flat.find((l) => l.id === current);
    currentModule = me ? me.module : (current.startsWith("tasks-") ? COURSE.modules.find((m) => "tasks-" + m.id === current) : null);

    const top = document.createElement("header");
    top.className = "topbar";
    let crumb = "";
    if (me && me.index !== undefined) crumb = `Модуль ${me.module.num} · ${me.module.title} · урок ${me.index + 1} из ${me.module.lessons.length}`;
    else if (currentModule) crumb = `Модуль ${currentModule.num} · практика`;
    else if (current === "cheatsheet") crumb = "Шпаргалка и собеседование";
    else if (current === "training") crumb = "Тренинг код — набей руку";
    top.innerHTML = `
      <button class="icon-btn menu-btn" aria-label="Меню курса">☰</button>
      <a class="brand" href="/">${JC.mascot("happy")}</a>
      <nav class="sect-switch" aria-label="Раздел">
        <a class="${current !== "training" && current !== "cheatsheet" ? "on" : ""}" href="/">☕ <span>${esc(COURSE.title)}</span></a>
        ${(window.TRAINING || []).length ? `<a class="${current === "training" ? "on" : ""}" href="/training.html" title="Задачи, чтобы набить руку">🏋️ <span>Тренинг код</span></a>` : ""}
        <a class="${current === "cheatsheet" ? "on" : ""}" href="/cheatsheet.html" title="Шпаргалка и вопросы с собеседований по всем модулям">📌 <span>Шпаргалка</span></a>
      </nav>
      <div class="top-center"><div class="top-crumb">${esc(crumb)}</div><div class="top-progress"></div></div>
      <div class="stats">
        <button class="upd-btn" hidden title="Вышла новая версия курса">🔄 <span>Обновление</span></button>
        <button class="icon-btn theme-btn" title="Светлая / тёмная тема" aria-label="Сменить тему">◐</button>
      </div>`;
    top.querySelector(".theme-btn").addEventListener("click", toggleTheme);
    watchUpdates(top.querySelector(".upd-btn"));
    // ☰: на широком экране прячет/показывает карту курса (выбор запоминается), на узком — выдвигает её поверх
    const wideScreen = () => matchMedia("(min-width: 1101px)").matches;
    try { if (localStorage.getItem("jc.ui.sideHidden") === "1") document.body.classList.add("side-hidden"); } catch (e) {}
    const menuBtn = top.querySelector(".menu-btn");
    const syncMenuTitle = () => { menuBtn.title = wideScreen() ? (document.body.classList.contains("side-hidden") ? "Показать карту курса" : "Спрятать карту курса") : "Карта курса"; };
    syncMenuTitle();
    menuBtn.addEventListener("click", () => {
      if (wideScreen()) {
        const hidden = document.body.classList.toggle("side-hidden");
        try { localStorage.setItem("jc.ui.sideHidden", hidden ? "1" : "0"); } catch (e) {}
      } else document.body.classList.toggle("menu-open");
      syncMenuTitle();
    });

    const side = document.createElement("aside");
    side.className = "sidebar";
    const layout = document.createElement("div");
    layout.className = "layout";
    main.parentNode.insertBefore(layout, main);
    layout.append(side, main);
    document.body.insertBefore(top, layout);
    document.addEventListener("click", (e) => { if (document.body.classList.contains("menu-open") && !e.target.closest(".sidebar,.menu-btn")) document.body.classList.remove("menu-open"); });
    renderSidebar();
    paintStats();
    // карта курса открывается так, чтобы текущий модуль начинался с самого верха панели
    const curMod = side.querySelector(".mod.current") || (side.querySelector(".node.active") || {}).closest?.(".mod");
    if (curMod) side.scrollTop = curMod.offsetTop - 8;
    return { me, flat };
  }

  // ---------------- шаги урока: весь урок виден сразу; полоска сверху — оглавление и прогресс чтения ----------------
  function setupSteps(main, id) {
    const steps = [...main.querySelectorAll("section.step")];
    const bar = document.querySelector(".top-progress");
    if (!steps.length) { if (bar) bar.remove(); return null; }
    const key = "jc.step." + id;
    let reached = readSet().has(id) ? steps.length - 1 : Math.min(store.get(key, 0), steps.length - 1);
    let cur = -1;
    steps.forEach((s, i) => {
      s.id = s.id || "step-" + (i + 1);
      const badge = document.createElement("div");
      badge.className = "step-badge";
      badge.textContent = `Шаг ${i + 1} из ${steps.length}`;
      s.prepend(badge);
    });
    // заголовок шага без варианта для другого режима курса (span.ai-only / span.free-only)
    const headText = (h) => { if (!h) return ""; const c = h.cloneNode(true); c.querySelectorAll(JC.mode === "free" ? ".ai-only" : ".free-only").forEach((x) => x.remove()); return c.textContent; };
    bar.innerHTML = steps.map((s, i) => `<span title="Шаг ${i + 1}: ${esc(headText(s.querySelector("h2")))}"></span>`).join("");
    const segs = [...bar.children];
    const goStep = (i) => {
      const target = steps[i];
      const top = target.getBoundingClientRect().top + scrollY - 76; // под шапкой
      const lack = top + innerHeight - document.documentElement.scrollHeight;
      if (lack > 0) main.style.paddingBottom = (parseFloat(getComputedStyle(main).paddingBottom) + lack + 8) + "px";
      scrollTo({ top, behavior: "smooth" });
      // если по пути что-то сдвинулось (картинки, виджеты дорисовались) — довести точно
      setTimeout(() => { const d = target.getBoundingClientRect().top - 76; if (Math.abs(d) > 4) scrollBy({ top: d, behavior: "instant" }); }, 900);
    };
    segs.forEach((seg, i) => seg.addEventListener("click", () => goStep(i)));
    // урок пройден — вся полоска зелёная, текущий шаг отмечен обводкой; иначе текущий — оранжевый
    const paint = () => {
      const done = readSet().has(id);
      segs.forEach((seg, i) => { seg.className = done ? (i === cur ? "on here" : "on") : i === cur ? "cur" : i <= reached ? "on" : ""; });
    };
    document.addEventListener("jc:done", paint);
    function update() {
      // текущий шаг — последний, чей верх уже поднялся выше 40% экрана; внизу страницы — последний шаг
      const line = innerHeight * 0.4;
      let c = 0;
      steps.forEach((s, i) => { if (s.getBoundingClientRect().top < line) c = i; });
      if (innerHeight + scrollY >= document.documentElement.scrollHeight - 4) c = steps.length - 1;
      if (c === cur) return;
      cur = c;
      if (c > reached) { reached = c; store.set(key, reached); }
      paint();
    }
    let ticking = false;
    addEventListener("scroll", () => { if (!ticking) { ticking = true; requestAnimationFrame(() => { ticking = false; update(); }); } }, { passive: true });
    update();
    return { steps };
  }

  // ---------------- финиш урока ----------------
  function buildFinish(main, me, flat) {
    const idx = flat.findIndex((l) => l.id === me.id);
    const prev = flat[idx - 1], next = flat[idx + 1];
    const fin = document.createElement("section");
    fin.className = "finish";
    main.appendChild(fin);
    const paint = () => {
      const done = readSet().has(me.id);
      fin.innerHTML = `
        <div class="fin-art">${JC.mascot(done ? "cool" : "happy")}</div>
        <h2>${done ? "Урок пройден! 🎉" : "Ты дошёл до конца урока"}</h2>
        <p>${done ? "Отметка стоит в меню слева. Можно идти дальше." : "Нажми кнопку — урок засчитается и отметится в меню слева."}</p>
        <div class="fin-actions">${done ? "" : `<button class="btn big green fin-btn">✓ Завершить урок</button>`}</div>
        <nav class="pager">
          ${prev ? `<a class="prev" href="${prev.url}"><span class="arrow">←</span><span><small>Назад</small><span class="t">${esc(prev.title)}</span></span></a>` : "<span></span>"}
          ${next ? `<a class="next" href="${next.url}"><span><small>Дальше</small><span class="t">${esc(next.title)}</span></span><span class="arrow">→</span></a>` : ""}
        </nav>`;
      const btn = fin.querySelector(".fin-btn");
      if (btn) btn.addEventListener("click", () => {
        setRead(me.id); JC.award("lesson:" + me.id, 30, "Урок пройден"); JC.confetti(); paint(); renderSidebar();
        document.dispatchEvent(new CustomEvent("jc:done"));
      });
    };
    paint();
  }

  // ---------------- шапка урока: картинка-маскот ----------------
  function decorateHead(main) {
    const head = main.querySelector(".lesson-head");
    if (!head || head.querySelector(".head-art")) return;
    const art = document.createElement("div");
    art.className = "head-art";
    art.innerHTML = JC.mascot(head.dataset.mood || "happy");
    head.classList.add("has-art");
    head.appendChild(art);
  }

  // ---------------- реплики маскота: <div class="say" data-mood="think">…</div> ----------------
  function sayBlocks(root) {
    root.querySelectorAll(".say").forEach((el) => {
      if (el.querySelector(".bubble")) return;
      const bubble = document.createElement("div");
      bubble.className = "bubble";
      bubble.innerHTML = `<span class="who">${esc(el.dataset.who || "Джавик")}</span>` + el.innerHTML;
      el.innerHTML = `<div class="ava">${JC.mascot(el.dataset.mood || "happy")}</div>`;
      el.appendChild(bubble);
    });
  }

  // ---------------- подсветка Java ----------------
  const KEYWORDS = "public|private|protected|static|final|void|class|interface|enum|record|extends|implements|new|return|if|else|for|while|do|switch|case|default|break|continue|try|catch|finally|throw|throws|import|package|this|super|var|int|long|short|byte|double|float|char|boolean|true|false|null";
  const TOKEN_RE = new RegExp(String.raw`(\/\/[^\n]*|\/\*[\s\S]*?\*\/)|("(?:\\.|[^"\\\n])*")|('(?:\\.|[^'\\\n])')|\b(\d+(?:\.\d+)?[LlFfDd]?)\b|\b(` +
    KEYWORDS + String.raw`)\b|(?<![\wА-Яа-яЁё])([A-ZА-ЯЁ][\wА-Яа-яЁё]*)|(?<![\wА-Яа-яЁё])([a-zа-яё_][\wА-Яа-яЁё]*)(?=\s*\()`, "gu");
  function highlightJava(src) {
    let out = "", last = 0, m;
    TOKEN_RE.lastIndex = 0;
    while ((m = TOKEN_RE.exec(src))) {
      out += esc(src.slice(last, m.index));
      const cls = m[1] ? "c" : (m[2] || m[3]) ? "s" : m[4] ? "n" : m[5] ? "k" : m[6] ? "t" : "f";
      out += `<span class="tok-${cls}">${esc(m[0])}</span>`;
      last = TOKEN_RE.lastIndex;
    }
    return out + esc(src.slice(last));
  }
  JC.highlightJava = highlightJava;
  function highlightAll(root) {
    root.querySelectorAll("pre code.java, .annot-row > code, code.java").forEach((el) => {
      if (el.dataset.hl) return;
      el.innerHTML = highlightJava(el.textContent.replace(/^\n/, ""));
      el.dataset.hl = "1";
    });
  }
  JC.highlightAll = highlightAll;

  function terminalCopy() {
    document.querySelectorAll("pre.terminal").forEach((pre) => {
      const cmds = [...pre.querySelectorAll(".cmd")].map((c) => c.textContent);
      if (!cmds.length || pre.querySelector(".copy-btn")) return;
      const b = document.createElement("button");
      b.className = "copy-btn"; b.textContent = "копировать";
      b.addEventListener("click", async () => {
        try { await navigator.clipboard.writeText(cmds.join("\n")); b.textContent = "скопировано ✓"; } catch (e) { b.textContent = "не вышло :("; }
        setTimeout(() => (b.textContent = "копировать"), 1500);
      });
      pre.appendChild(b);
    });
  }

  // ---------------- код нельзя скопировать: набираем руками ----------------
  const CODE_SEL = "pre:not(.terminal), .annot-row > code, .ps-line.code, code";
  function blockCodeCopy() {
    const inCode = (n) => { const el = n && (n.nodeType === 1 ? n : n.parentElement); return el && el.closest(CODE_SEL) && !el.closest("pre.terminal"); };
    const nudge = () => JC.toast("⌨️", "Код набирай руками — так пальцы и голова его запомнят");
    document.addEventListener("copy", (e) => {
      const sel = document.getSelection();
      if (!sel || sel.isCollapsed) return;
      const range = sel.getRangeAt(0);
      const hasCode = inCode(sel.anchorNode) || inCode(sel.focusNode) ||
        [...document.querySelectorAll(CODE_SEL)].some((c) => !c.closest("pre.terminal") && range.intersectsNode(c));
      if (hasCode) { e.preventDefault(); nudge(); }
    });
    document.addEventListener("contextmenu", (e) => { if (inCode(e.target)) { e.preventDefault(); nudge(); } });
    document.addEventListener("dragstart", (e) => { if (inCode(e.target)) e.preventDefault(); });
    // попытка выделить код мышкой — сразу объясняем, почему не выделяется (не чаще раза в 8 секунд)
    let last = 0;
    document.addEventListener("mousedown", (e) => {
      if (!e.target.closest || !e.target.closest("pre:not(.terminal)") || e.target.closest(".widget .ps-line, button, input")) return;
      const now = Date.now(); if (now - last > 8000) { last = now; nudge(); }
    });
    // метка на каждом блоке кода: КУДА его набирать
    // до установки IDEA (модуль 0 и уроки 1.1–1.3) набирать ещё негде — метку не ставим
    const lid = (document.querySelector("main[data-lesson]") || {}).dataset?.lesson || "";
    if (/^m00-|^m01-0[123]$/.test(lid)) return;
    // песочница Playground и папка training появляются в уроке 1.6 (m01-07); раньше — просто «набери»
    const sandbox = !/^m01-0[46]$/.test(lid);
    let opened = false;
    document.querySelectorAll("main pre:not(.terminal)").forEach((pre) => {
      if (pre.closest(".widget") || pre.closest(".diff") || pre.closest("details.solution") || pre.querySelector(".type-hint")) return;
      const file = pre.dataset.file || "";
      const text = pre.textContent;
      const isJava = !file || /\.java$/.test(file);
      const cls = (text.match(/\bclass\s+(\w+)/) || [])[1];
      const hasMain = /static\s+void\s+main/.test(text);
      if (!sandbox || !isJava) {
        const t = document.createElement("span"); t.className = "type-hint"; t.textContent = "✍️ набери руками";
        pre.appendChild(t);
        return;
      }
      let label, how;
      if (!cls) {
        // кусочек без класса — это строчки для main песочницы; подпись файла не должна путать
        if (file) pre.dataset.file = "Playground.java · внутри main";
        label = "✍️ Куда набирать?";
        how = `<b>Это кусочек, а не целая программа</b> — набери его внутри <code>main</code> песочницы:
          <ol><li>Открой класс <code>Playground</code> в папке <code>training</code> (<a href="/lessons/m01/07-console-output.html">урок 1.6</a>).</li>
          <li>Внутри <code>main</code> — между его <code>{</code> и <code>}</code> — сотри старое и набери этот код.</li>
          <li>Нажми ▶ рядом с <code>main</code> и сравни вывод с уроком.</li></ol>
          Почему в <code>main</code>: это <b>точка входа</b> — с неё Java начинает выполнять программу (<a href="/lessons/m01/04-first-program-idea.html">урок 1.4</a>). Код вне метода Java выполнить не может.`;
      } else if (hasMain) {
        label = `✍️ Куда набирать? Новый класс ${cls}`;
        how = `<b>Это целая программа: класс <code>${esc(cls)}</code> со своей точкой входа <code>main</code>.</b>
          <ol><li>Правый клик по папке <code>training</code> → <b>New → Java Class</b> → <code>${esc(cls)}</code>. Если такой класс уже есть — открой его и замени код.</li>
          <li>Набери код целиком — IDEA уже написала первую и последнюю строку класса, их не дублируй.</li>
          <li>Нажми ▶ рядом с <code>main</code>.</li></ol>`;
      } else {
        label = `✍️ Куда набирать? Класс ${cls}`;
        how = `<b>Это класс <code>${esc(cls)}</code> без <code>main</code></b> — сам по себе он не запускается, им пользуется другой класс.
          Создай его в папке <code>training</code> (<b>New → Java Class</b> → <code>${esc(cls)}</code>), а запускай класс с <code>main</code> из этого же урока — он будет рядом.`;
      }
      const btn = document.createElement("button");
      btn.type = "button"; btn.className = "type-hint where-btn"; btn.textContent = label;
      pre.appendChild(btn);
      const panel = document.createElement("div");
      panel.className = "where-panel"; panel.innerHTML = how; panel.hidden = opened;
      pre.after(panel);
      opened = true; // на странице сразу раскрыта только первая подсказка
      btn.addEventListener("click", (e) => { e.preventDefault(); panel.hidden = !panel.hidden; });
    });
  }

  // в каждой плашке «поговори с наставником» — ссылка, что это такое
  function aiLinks() {
    document.querySelectorAll(".callout.ai").forEach((c) => {
      if (c.querySelector(".ai-what") || location.pathname.includes("01-how-it-works")) return;
      const a = document.createElement("a");
      a.className = "ai-what"; a.href = "/lessons/m00/01-how-it-works.html"; a.textContent = "Что такое наставник и как его открыть? →";
      c.appendChild(a);
    });
  }

  // ---------------- квизы ----------------
  function quizzes(lessonId) {
    document.querySelectorAll(".quiz").forEach((q, qi) => {
      const items = [...q.querySelectorAll(":scope > ul > li")];
      const fb = document.createElement("div");
      fb.className = "quiz-fb"; fb.setAttribute("aria-live", "polite");
      q.appendChild(fb);
      let tries = 0;
      items.forEach((li, i) => {
        li.tabIndex = 0; li.setAttribute("role", "button");
        li.innerHTML = `<span class="letter">${"АБВГДЕ"[i]}</span><span>${li.innerHTML}</span>`;
        const pick = () => {
          tries++;
          items.forEach((x) => x.classList.remove("right", "wrong"));
          const ok = li.hasAttribute("data-correct");
          li.classList.add(ok ? "right" : "wrong");
          fb.className = "quiz-fb " + (ok ? "right" : "wrong");
          fb.textContent = (ok ? "✓ Верно! " : "✗ Не совсем. ") + (li.dataset.why || (ok ? "" : "Попробуй ещё раз."));
          if (ok && tries === 1) JC.award(`quiz:${lessonId || location.pathname}:${qi}`, 5, "С первой попытки!");
        };
        li.addEventListener("click", pick);
        li.addEventListener("keydown", (e) => { if (e.key === "Enter" || e.key === " ") { e.preventDefault(); pick(); } });
      });
    });
  }

  // ---------------- словарик: первое упоминание термина на странице → подсказка ----------------
  function glossary(main) {
    const s = document.createElement("script");
    s.src = "/js/glossary.js";
    s.onload = () => {
      const list = (window.JC_GLOSSARY || []).map(([src, def]) => ({ re: new RegExp(`(?<![\\p{L}\\d_-])(?:${src})(?![\\p{L}\\d_])`, "iu"), def, used: false }));
      const SKIP = "pre, code, a, h1, h2, h3, h4, .kicker, .widget, .quiz, figure, .gl, button, kbd, .say .who, .callout-title, script, dt, th";
      const walker = document.createTreeWalker(main, NodeFilter.SHOW_TEXT, {
        acceptNode: (n) => (n.nodeValue.trim().length > 2 && !n.parentElement.closest(SKIP)) ? NodeFilter.FILTER_ACCEPT : NodeFilter.FILTER_REJECT
      });
      const nodes = []; while (walker.nextNode()) nodes.push(walker.currentNode);
      for (const node of nodes) {
        let cur = node;
        for (const g of list) {
          if (g.used || !cur) continue;
          const m = g.re.exec(cur.nodeValue);
          if (!m) continue;
          g.used = true;
          const after = cur.splitText(m.index);
          const rest = after.splitText(m[0].length);
          const span = document.createElement("span");
          span.className = "gl"; span.tabIndex = 0; span.dataset.def = g.def; span.textContent = after.nodeValue;
          after.replaceWith(span);
          cur = rest;
        }
      }
      main.addEventListener("click", (e) => {
        const g = e.target.closest(".gl");
        document.querySelectorAll(".gl.show").forEach((x) => x !== g && x.classList.remove("show"));
        if (g) g.classList.toggle("show");
      });
    };
    document.head.appendChild(s);
  }

  // ---------------- виджеты подгружаются, только если есть на странице ----------------
  function loadWidgets() {
    if (!document.querySelector("[data-widget]")) return;
    const s = document.createElement("script");
    s.src = "/js/widgets.js";
    s.onload = () => document.querySelectorAll("[data-widget]").forEach((el) => {
      const fn = JC.widgets[el.dataset.widget];
      if (fn && !el.dataset.ready) { el.dataset.ready = "1"; try { fn(el); } catch (e) { console.error(e); el.textContent = "Не удалось загрузить мини-игру: " + e.message; } }
    });
    document.head.appendChild(s);
  }

  // ---------------- профили и режим курса ----------------
  // Режим «ai» — есть подписка на Claude Code: наставник в чате (/check, /hint, /exam).
  // Режим «free» — без подписки: вместо наставника бесплатный ИИ (DeepSeek) или ChatGPT, и к каждому вопросу — готовый полный промпт.
  const TOOLS = {
    deepseek: { name: "DeepSeek", url: "https://chat.deepseek.com" },
    chatgpt: { name: "ChatGPT", url: "https://chatgpt.com" },
    claude: { name: "Claude Code", url: "" }
  };
  let profiles = [];
  function setActive(id) {
    try { if (id) localStorage.setItem(PROFILE_KEY, id); else localStorage.removeItem(PROFILE_KEY); } catch (e) { /* ignore */ }
    location.reload();
  }
  function applyProfile(p) {
    JC.profile = p;
    JC.mode = p.mode === "free" ? "free" : "ai";
    JC.tool = TOOLS[p.tool] && p.tool !== "claude" ? p.tool : "deepseek";
    document.body.classList.add("mode-" + JC.mode);
  }
  async function ensureProfile() {
    try {
      const r = await nativeFetch("/api/profiles", { cache: "no-store" });
      profiles = await r.json();
    } catch (e) { JC.mode = "ai"; return true; } // сервер недоступен — работаем как раньше, без профилей
    const p = profiles.find((x) => x.id === profileId);
    if (p) { applyProfile(p); return true; }
    showWelcome(profiles.length ? "pick" : "name");
    return false;
  }
  const modeLabel = (p) => p.mode === "free"
    ? `без подписки · ${TOOLS[p.tool] && p.tool !== "claude" ? TOOLS[p.tool].name : "DeepSeek"}`
    : "с наставником Claude Code";

  /** Экран «Привет»: выбрать профиль или завести новый (имя → подписка на ИИ → готово). */
  function showWelcome(step, editProfile) {
    const ov = document.createElement("div");
    ov.className = "welcome-overlay";
    ov.innerHTML = `<div class="welcome-card" role="dialog" aria-modal="true"><div class="welcome-mascot">${JC.mascot ? JC.mascot("happy") : "☕"}</div><div class="welcome-body"></div></div>`;
    document.body.appendChild(ov);
    const body = ov.querySelector(".welcome-body");
    const draft = { name: editProfile ? editProfile.name : "", mode: "ai", tool: "claude" };
    const go = (st) => {
      if (st === "pick") {
        body.innerHTML = `<h2>С возвращением! Кто учится?</h2>
          <div class="welcome-list">${profiles.map((p) => `<div class="welcome-row" data-id="${esc(p.id)}">
              <button class="welcome-pick" data-id="${esc(p.id)}"><b>${esc(p.name)}</b><small>${esc(modeLabel(p))}</small></button>
              <button class="welcome-del" data-id="${esc(p.id)}" title="Удалить профиль «${esc(p.name)}»" aria-label="Удалить профиль «${esc(p.name)}»">🗑</button>
            </div>`).join("")}</div>
          <button class="btn ghost welcome-new">＋ Новый профиль</button>`;
        body.querySelectorAll(".welcome-pick").forEach((b) => b.addEventListener("click", () => setActive(b.dataset.id)));
        body.querySelector(".welcome-new").addEventListener("click", () => go("name"));
        // 🗑 → в строке появляется подтверждение; прогресс сервер не стирает, а переносит в workspace/.progress/deleted/
        body.querySelectorAll(".welcome-del").forEach((b) => b.addEventListener("click", () => {
          const row = b.closest(".welcome-row"), p = profiles.find((x) => x.id === b.dataset.id);
          if (!p) return;
          row.innerHTML = `<div class="welcome-confirm">
              <p>Удалить профиль <b>«${esc(p.name)}»</b>? Пропадут его пройденные уроки и задачи. Код в папке <code>workspace</code> останется.</p>
              <div class="w-row"><button class="btn small red welcome-yes">Удалить</button><button class="btn small ghost welcome-no">Отмена</button></div>
            </div>`;
          row.querySelector(".welcome-no").addEventListener("click", () => go("pick"));
          row.querySelector(".welcome-yes").addEventListener("click", async (e) => {
            e.target.disabled = true;
            try {
              const r = await nativeFetch("/api/profiles/" + encodeURIComponent(p.id), { method: "DELETE" });
              if (!r.ok && r.status !== 404) throw new Error("HTTP " + r.status);
              profiles = profiles.filter((x) => x.id !== p.id);
              if (profileId === p.id) { try { localStorage.removeItem(PROFILE_KEY); } catch (err) { /* ignore */ } profileId = null; }
              go(profiles.length ? "pick" : "name");
            } catch (err) {
              e.target.disabled = false;
              row.insertAdjacentHTML("beforeend", `<p class="welcome-err">Не получилось удалить: сервер курса не отвечает.</p>`);
            }
          });
        }));
      } else if (st === "name") {
        body.innerHTML = `<h2>👋 Привет! Как тебя зовут?</h2>
          <p>Под этим именем будет сохраняться твой прогресс: пройденные уроки, задачи, ответы в проверках.</p>
          <input class="w-input welcome-name" maxlength="40" placeholder="Например, Миша" autocomplete="off">
          <p class="welcome-err" hidden>Впиши имя — хотя бы одну букву.</p>
          <button class="btn green welcome-next" disabled>Дальше →</button>`;
        const inp = body.querySelector(".welcome-name"), nx = body.querySelector(".welcome-next");
        inp.value = draft.name;
        const valid = () => inp.value.trim().length > 0;
        const sync = () => { nx.disabled = !valid(); body.querySelector(".welcome-err").hidden = valid() || !inp.dataset.touched; };
        inp.addEventListener("input", () => { inp.dataset.touched = "1"; sync(); });
        inp.addEventListener("keydown", (e) => { if (e.key === "Enter" && valid()) nx.click(); });
        nx.addEventListener("click", () => { draft.name = inp.value.trim().slice(0, 40); go("sub"); });
        sync(); inp.focus();
      } else if (st === "sub") {
        body.innerHTML = `<h2>${editProfile ? "Режим курса" : `Приятно познакомиться, ${esc(draft.name)}!`}</h2>
          <p>Есть ли у тебя <b>платная подписка на ИИ</b>? От этого зависит, как курс будет помогать тебе с вопросами.</p>
          <div class="welcome-options">
            <button class="welcome-opt" data-mode="ai" data-tool="claude"><b>Да, Claude Code</b><small>Подписка Claude Pro или Max. Курс с ИИ-наставником: он видит твой код, проверяет задачи (<code>/check</code>), подсказывает (<code>/hint</code>) и принимает экзамены.</small></button>
            <button class="welcome-opt" data-mode="free" data-tool="chatgpt"><b>Да, ChatGPT Plus или другая</b><small>Курс без встроенного наставника. К каждому вопросу — готовый промпт: копируешь и вставляешь в ChatGPT.</small></button>
            <button class="welcome-opt" data-mode="free" data-tool="deepseek"><b>Нет подписки</b><small>Бесплатно: вопросы задаёшь DeepSeek. Он быстро забывает разговор, поэтому для каждого вопроса курс даст полный промпт с правилами «учителя».</small></button>
          </div>`;
        body.querySelectorAll(".welcome-opt").forEach((b) => b.addEventListener("click", () => { draft.mode = b.dataset.mode; draft.tool = b.dataset.tool; go("done"); }));
      } else if (st === "done") {
        const p = { name: draft.name, mode: draft.mode, tool: draft.tool };
        body.innerHTML = `<h2>Готово!</h2>
          <p class="welcome-mode">Выбран курс: <b>${esc(modeLabel(p))}</b></p>
          <p>${draft.mode === "ai"
            ? "В уроках будут блоки «Поговори с наставником» — пиши это в Claude Code, открытом в папке курса."
            : `В уроках вместо наставника будут блоки «Спроси ${esc(draft.tool === "chatgpt" ? "ChatGPT" : "DeepSeek")}» с кнопкой «📋 Скопировать промпт». Всё остальное в курсе то же самое.`}</p>
          <p class="welcome-small">Режим можно поменять позже в меню профиля — справа вверху.</p>
          <button class="btn green welcome-start">${editProfile ? "Сохранить" : "Начать →"}</button>`;
        body.querySelector(".welcome-start").addEventListener("click", async (e) => {
          e.target.disabled = true;
          try {
            const url = editProfile ? "/api/profiles/" + encodeURIComponent(editProfile.id) : "/api/profiles";
            const saved = await nativeFetch(url, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(p) }).then((r) => r.json());
            setActive(saved.id || (editProfile && editProfile.id));
          } catch (err) {
            e.target.disabled = false;
            body.insertAdjacentHTML("beforeend", `<p class="welcome-err">Сервер курса не отвечает. Запусти его: docker compose up -d</p>`);
          }
        });
      }
    };
    go(step);
  }

  /** Имя профиля справа вверху: режим курса, сменить профиль, новый профиль, выйти. */
  function profileChip() {
    const p = JC.profile, stats = document.querySelector(".topbar .stats");
    if (!p || !stats || stats.querySelector(".profile-btn")) return;
    const wrap = document.createElement("div");
    wrap.className = "profile-wrap";
    wrap.innerHTML = `<button class="icon-btn profile-btn" title="Профиль"><span class="pf-ava">${esc(p.name.slice(0, 1).toUpperCase())}</span><span class="pf-name">${esc(p.name)}</span></button>
      <div class="profile-menu" hidden>
        <div class="pf-head"><b>${esc(p.name)}</b><small>Курс: ${esc(modeLabel(p))}</small></div>
        <button data-act="mode">⚙️ Поменять режим курса</button>
        <button data-act="switch">👥 Сменить профиль</button>
        <button data-act="new">＋ Новый профиль</button>
        <button data-act="logout">🚪 Выйти</button>
      </div>`;
    stats.prepend(wrap);
    const menu = wrap.querySelector(".profile-menu");
    wrap.querySelector(".profile-btn").addEventListener("click", (e) => { e.stopPropagation(); menu.hidden = !menu.hidden; });
    document.addEventListener("click", (e) => { if (!wrap.contains(e.target)) menu.hidden = true; });
    menu.addEventListener("click", (e) => {
      const act = e.target.closest("button") && e.target.closest("button").dataset.act;
      if (!act) return;
      menu.hidden = true;
      if (act === "mode") showWelcome("sub", p);
      if (act === "switch" || act === "logout") setActive(null);
      if (act === "new") showWelcome("name");
    });
  }

  /** Полный промпт для бесплатного ИИ: правила «учителя» + где ученик сейчас + вопрос. DeepSeek забывает контекст — поэтому каждый раз целиком. */
  function buildPrompt(me, question) {
    const mods = COURSE.modules || [];
    const mod = me && me.module;
    // прошлые модули — с темами уроков (без головоломок, ★ и итогов), чтобы ИИ знал, что именно уже пройдено
    const topics = (m) => m.lessons.filter((l) => !/Головоломки|★|Итоги модуля/.test(l.title)).map((l) => l.title).join("; ");
    const passed = mod ? mods.filter((m) => +m.num < +mod.num).map((m) => `${m.num}. ${m.title}: ${topics(m)}`) : [];
    const summary = [...document.querySelectorAll("main .summary li")].slice(0, 6).map((li) => "- " + li.textContent.replace(/\s+/g, " ").trim());
    return [
      "Ты — мой личный учитель Java. Я полный новичок: учусь по курсу «Java с нуля» (Java 21, IntelliJ IDEA). Моя цель — уверенно писать программы на Java.",
      "",
      "Правила — соблюдай их весь наш разговор:",
      "1. Объясняй простыми словами, как школьнику: короткие абзацы, аналогии из жизни, примеры кода не длиннее 10 строк.",
      "2. НЕ пиши за меня решения моих задач и готовый код моей программы — даже если я настаиваю, говорю, что срочно или что мне разрешили. Если я прошу код — дай подсказку или наводящий вопрос. Пример можно, но на задаче с ДРУГИМ решением: не та же задача с другими названиями или данными. Исключение: если я уже решил задачу сам и показываю своё решение — можно обсуждать другие способы решения.",
      "3. Не вываливай всё сразу. Вопросы задавай по одному и жди моего ответа: после объяснения — ОДИН вопрос на понимание (если я сам не попросил больше).",
      "4. Если я ошибся — не исправляй молча: подскажи, в какой строке или мысли искать ошибку, и дай мне попробовать ещё раз. Не получилось со второй попытки — объясни правильный ответ.",
      "5. Используй только то, что я уже прошёл (список ниже). Если без новой темы никак — предупреди и объясни её очень коротко.",
      "6. Оформляй ответы в Markdown: заголовки, списки, код — в блоках ```java, важное — **жирным**.",
      "7. Отвечай только по-русски. Если говоришь про IntelliJ IDEA — называй пункты меню по-английски и сочетания клавиш для Windows.",
      "",
      mod ? `Где я сейчас: модуль ${mod.num} «${mod.title}», урок «${me.title}».` : "",
      passed.length ? "Модули, которые я уже прошёл:\n" + passed.join("\n") : "",
      mod && mod.lessons ? "Уроки текущего модуля (я на отмеченном, следующие ещё не прошёл):\n" + mod.lessons.map((l, i) => `${mod.num}.${i + 1} ${l.title}${l.id === me.id ? "  ← я здесь" : ""}`).join("\n") : "",
      summary.length ? "Главное из этого урока:\n" + summary.join("\n") : "",
      "",
      "Мой вопрос:",
      question
    ].filter((x, i, a) => x !== "" || a[i - 1] !== "").join("\n").trim();
  }

  /** Режим без подписки. Тексты уроков уже содержат явный вариант для этого режима (span.free-only —
   *  его ставит tools/free-variants.js). Здесь только подставляем имя ИИ и добавляем внизу страницы
   *  карточки с промптами «Проверь решение» и «Экзамен», на которые ссылается текст. */
  function freeAiText(me) {
    const tool = TOOLS[JC.tool] || TOOLS.deepseek;
    const main = document.querySelector("main.content");
    if (!main) return;
    main.querySelectorAll(".ai-name").forEach((s) => { s.textContent = tool.name; });
    const freeText = [...main.querySelectorAll(".free-only")].map((e) => e.textContent).join(" ");
    const last = [...main.querySelectorAll("section.step")].pop() || main;
    const add = (cls, title, text) => {
      if (main.querySelector("." + cls)) return;
      const box = document.createElement("div");
      box.className = "callout ai " + cls;
      box.innerHTML = `<span class="callout-title">${title}</span><p>${text}</p>`;
      last.appendChild(box);
    };
    if (/«Проверь решение»/.test(freeText)) add("free-check", "Проверь решение", "free-check");
    if (/«Экзамен»/.test(freeText)) {
      // карточка экзамена уже есть в уроке — переносим её вниз, куда указывает текст, а не дублируем
      const own = [...main.querySelectorAll(".callout.ai")].find((c) => !c.classList.contains("free-check") && /\/exam\b/.test(c.textContent));
      if (own) { own.classList.add("free-exam"); last.appendChild(own); } else add("free-exam", "Экзамен", "/exam");
    }
  }

  /** Режим без подписки: блоки «Поговори с наставником» превращаются в «Спроси DeepSeek» с готовым промптом. */
  function freeAiBlocks(me) {
    const tool = TOOLS[JC.tool] || TOOLS.deepseek;
    document.querySelectorAll(".callout.ai, .callout.free-prompt").forEach((c) => {
      if (c.classList.contains("ai-only")) return;
      const titleEl = c.querySelector(".callout-title");
      if (c.classList.contains("free-prompt")) {
        // «Хорошие вопросы к ИИ»: примеры оставляем, а копировать — промпт с правилами учителя и местом для своего вопроса
        if (titleEl) titleEl.remove();
        const examples = c.innerHTML;
        const prompt = buildPrompt(me, "(вставь сюда свой вопрос — например, один из примеров выше)");
        c.classList.add("free-ai");
        c.innerHTML = `<span class="callout-title">Спроси ${esc(tool.name)}: хорошие вопросы</span>${examples}
          <p>Скопируй промпт, вставь в ${esc(tool.name)} и допиши в конце свой вопрос — правила учителя в промпте уже есть.</p>
          <div class="prompt-actions"><button class="btn small green copy-prompt">📋 Скопировать промпт</button>
            <a class="btn small ghost" href="${tool.url}" target="_blank" rel="noopener">Открыть ${esc(tool.name)} ↗</a></div>
          <details class="prompt-preview"><summary>Что внутри промпта</summary><div class="prompt-text"></div></details>`;
        c.querySelector(".prompt-text").textContent = prompt;
        copyButton(c, prompt);
        return;
      }
      const raw = c.textContent.replace(titleEl ? titleEl.textContent : "", "").replace(/\s+/g, " ").trim();
      const generic = c.classList.contains("free-check");
      const check = generic ? [null, ""] : raw.match(/\/check\s+([\w-]+)/), hint = raw.match(/\/hint\s+([\w-]+)/), exam = /\/exam\b/.test(raw);
      // progress.md ведёт только наставник в Claude Code — в режиме без подписки упоминание убираем
      const quotes = [...raw.matchAll(/«([^»]{8,})»/g)].map((q) => q[1].replace(/,? (но )?без отметок в progress\.md/g, ""));
      const star = me && /-star$/.test(me.id);
      const where = star ? "(оно выше, на этой странице)" : "(со страницы «Практика»)";
      const codeBlock = "\n\nМой код:\n```java\n(вставь сюда свой код)\n```";
      let shown, question;
      if (check) {
        const name = check[1] ? ` ${check[1]}` : "";
        shown = check[1]
          ? `Проверь своё решение задачи <code>${esc(check[1])}</code>: скопируй промпт, вставь в ${esc(tool.name)} и допиши туда условие задачи ${where} и свой код.`
          : `Разбери с ${esc(tool.name)} любую задачу модуля, которую уже решил: скопируй промпт и допиши туда условие задачи (со страницы «Практика») и свой код.`;
        question = `Проверь моё решение задачи${name}. Не переписывай мой код и не давай готовое решение. Скажи: 1) правильна ли идея, 2) что можно улучшить и почему, 3) задай мне 2 вопроса на понимание моего же кода — по одному, жди ответа на каждый.\n\nУсловие задачи:\n(вставь сюда условие)` + codeBlock;
      } else if (hint) {
        shown = `Застрял на задаче <code>${esc(hint[1])}</code>? Скопируй промпт — ${esc(tool.name)} даст подсказку, а не решение.`;
        question = `Я застрял на задаче ${hint[1]}. Дай ОДНУ подсказку: наводящий вопрос или на что посмотреть. Код решения не пиши. Если после подсказки я всё ещё не пойму — дай следующую, чуть конкретнее.\n\nУсловие задачи ${where}:\n(вставь сюда условие)\n\nЧто я уже попробовал:\n(вставь свой код или опиши, что не получается)`;
      } else if (exam) {
        shown = `Устрой себе мини-экзамен по модулю с помощью ${esc(tool.name)}. Лучше в новом чате.`;
        question = "Устрой мне мини-экзамен по этому модулю: задавай по одному вопросу (объясни своими словами / что выведет код / найди ошибку) и жди моего ответа. Если я ошибся — сначала дай подсказку и вторую попытку, а если опять не так — объясни правильный ответ. Всего 8 вопросов, в конце — оценка и какие уроки повторить.";
      } else if (quotes.length) {
        const showCode = /решени|сво[йеё] код|покажи/i.test(raw);
        const dots = quotes.some((q) => /…|\.\.\./.test(q));
        shown = `Спроси ${esc(tool.name)}: ${quotes.map((q) => "«" + esc(q) + "»").join(" ")}`
          + (showCode ? " В промпте есть место для твоего кода — вставь его." : "")
          + (dots ? " Перед отправкой замени многоточия «…» своими словами." : "")
          + (/скриншот/i.test(raw) ? ` Можно приложить скриншот — ${esc(tool.name)} понимает картинки.` : "");
        question = quotes.map((q) => q.replace(/наставник\w*/gi, "учитель")).join("\n") + (showCode ? codeBlock : "");
      } else {
        shown = esc(raw.replace(/\/\w+(\s+[\w-]+)?/g, "").trim() || "Обсуди этот урок с ИИ.");
        question = "Обсуди со мной этот урок: " + raw.replace(/\/\w+(\s+[\w-]+)?/g, "").trim();
      }
      const prompt = buildPrompt(me, question);
      const cardTitle = exam ? `Экзамен у ${tool.name}` : check ? `Проверь решение с ${tool.name}` : hint ? `Подсказка от ${tool.name}` : `Спроси ${tool.name}`;
      c.classList.add("free-ai");
      c.innerHTML = `<span class="callout-title">${esc(cardTitle)}</span>
        <p>${shown}</p>
        <div class="prompt-actions"><button class="btn small green copy-prompt">📋 Скопировать промпт</button>
          <a class="btn small ghost" href="${tool.url}" target="_blank" rel="noopener">Открыть ${esc(tool.name)} ↗</a></div>
        <details class="prompt-preview"><summary>Что внутри промпта</summary><div class="prompt-text"></div></details>`;
      c.querySelector(".prompt-text").textContent = prompt;
      copyButton(c, prompt);
    });
  }

  /** Кнопка «📋 Скопировать промпт» внутри карточки. */
  function copyButton(c, prompt) {
    c.querySelector(".copy-prompt").addEventListener("click", async (e) => {
      const b = e.currentTarget;
      try { await navigator.clipboard.writeText(prompt); }
      catch (err) {
        const ta = document.createElement("textarea"); ta.value = prompt; document.body.appendChild(ta); ta.select();
        try { document.execCommand("copy"); } catch (e2) { /* в крайнем случае — текст виден в «Что внутри промпта» */ }
        ta.remove();
      }
      b.textContent = "✓ Скопировано — вставь в чат"; setTimeout(() => { b.textContent = "📋 Скопировать промпт"; }, 2500);
    });
  }

  /** Значок «🔄 Обновление»: сервер раз в 30 минут сверяет коммит папки курса с GitHub (/api/update).
   *  Страница спрашивает сервер при открытии и потом каждые 5 минут — это дёшево, к GitHub ходит только сервер. */
  function watchUpdates(btn) {
    if (!btn) return;
    let info = null;
    const fmtDate = (iso) => { const d = new Date(iso); return isNaN(d) ? "" : d.toLocaleDateString("ru-RU", { day: "numeric", month: "long" }); };
    const check = async () => {
      try { info = await (await fetch("/api/update")).json(); } catch (e) { return; } // сервер недоступен — молчим
      const behind = info && info.status === "behind";
      btn.hidden = !behind;
      if (behind) btn.querySelector("span").textContent = `Обновление (${info.newCommits})`;
    };
    btn.addEventListener("click", async () => {
      if (!info) return;
      // Полный путь к папке курса: сервер знает, где на диске лежит workspace (/api/setup) — курс на уровень выше.
      // Одна команда: перейти в папку, скачать обновление и, только если скачалось без ошибок, пересобрать сайт.
      // «; if ($?) {…}» работает и в Windows PowerShell 5, и в PowerShell 7 (а «&&» в старом не работает).
      let dir = "$HOME\\IdeaProjects\\java-course";
      try {
        const s = await (await fetch("/api/setup")).json();
        if (s && s.hostPath) dir = s.hostPath.replace(/[\\/]workspace[\\/]?$/, "");
      } catch (e) { /* оставим путь по умолчанию */ }
      const oneLine = `cd "${dir}"; git pull; if ($?) { docker compose up -d --build }`;
      const cmd = (c) => `<div class="upd-cmd"><code>${esc(c)}</code><button class="btn small ghost" data-copy="${esc(c)}">Скопировать</button></div>`;
      const list = (info.commits || []).map((c) => `<li><b>${esc(c.title)}</b>${c.date ? ` <small>· ${fmtDate(c.date)}</small>` : ""}${c.body ? `<div class="upd-body">${esc(c.body).replace(/\n/g, "<br>")}</div>` : ""}</li>`).join("");
      const ov = document.createElement("div");
      ov.className = "welcome-overlay";
      ov.innerHTML = `<div class="welcome-card upd-card" role="dialog" aria-modal="true" aria-label="Вышло обновление курса">
        <div class="welcome-mascot">${JC.mascot ? JC.mascot("wow") : "🔄"}</div>
        <div class="welcome-body">
          <h2>Вышло обновление курса</h2>
          <p>На GitHub появилось новых изменений: <b>${info.newCommits}</b>. Что нового:</p>
          <ul class="upd-list">${list}</ul>
          <p><b>Как обновить:</b> открой PowerShell (Пуск → набери <code>PowerShell</code> → Enter), вставь эту команду и нажми Enter. Она сама перейдёт в папку курса, скачает обновление и перезапустит сайт.</p>
          ${cmd(oneLine)}
          <p class="meta">Займёт 1–3 минуты. Когда в конце появится строчка со словом «Started», обнови эту страницу (F5). Если вместо этого появится красная ошибка — сайт не трогаем, просто покажи её наставнику или ИИ-помощнику. Твой код, прогресс и история проверок лежат в папке <code>workspace</code> — обновление их не трогает.</p>
          <div class="w-row"><button class="btn green upd-close">Понятно</button></div>
        </div></div>`;
      document.body.appendChild(ov);
      const close = () => ov.remove();
      ov.querySelector(".upd-close").addEventListener("click", close);
      ov.addEventListener("click", (e) => { if (e.target === ov) close(); });
      document.addEventListener("keydown", function esc1(e) { if (e.key === "Escape") { close(); document.removeEventListener("keydown", esc1); } });
      ov.querySelectorAll("[data-copy]").forEach((b) => b.addEventListener("click", async () => {
        try { await navigator.clipboard.writeText(b.dataset.copy); b.textContent = "✓ Скопировано"; } catch (e) { b.textContent = "Выдели и скопируй"; }
        setTimeout(() => { b.textContent = "Скопировать"; }, 2000);
      }));
    });
    check();
    setInterval(check, 5 * 60 * 1000);
  }

  /** В итогах модулей 2–17 — напоминание про «Тренинг код» и про практику каждый день. */
  function trainingReminder(main, me) {
    const num = +((me.id.match(/^m(\d\d)/) || [])[1] || 0);
    if (num < 2 || !/Итоги модуля/.test(me.title || "")) return;
    const topics = (window.TRAINING || []).filter((t) => t.after === "m" + String(num).padStart(2, "0"));
    const where = topics.length
      ? "К этому модулю там есть своя тема: " + topics.map((t) => `<a href="/tasks.html?module=${t.id}">${t.icon} «${esc(t.title)}»</a> — ${t.taskCount} задач`).join(", ") + ". Начни с 🟢 лёгких, потом переходи к 🟡 средним."
      : `Своей темы у этого модуля нет, зато можно повторить любую пройденную: <a href="/training.html">все темы тренинга</a>.`;
    const box = document.createElement("div");
    box.className = "callout tip tr-remind";
    box.innerHTML = `<span class="callout-title">🏋️ Не забудь про «Тренинг код»</span>
      <p>Модуль позади — самое время закрепить его руками. Нажми кнопку <b>«🏋️ Тренинг код»</b> в шапке сайта:</p>
      <img src="/img/training-button.png" alt="Кнопка «Тренинг код» в шапке сайта, между «Java с нуля» и «Шпаргалка»" width="635" height="64">
      <p>${where}</p>
      <p><b>Лучше каждый день по 20–30 минут, чем раз в неделю 5 часов подряд.</b> Язык программирования забывается очень быстро, как иностранный: без практики через пару недель синтаксис вылетает из головы. Пара задач в день — и всё держится в памяти.</p>`;
    const steps = main.querySelectorAll("section.step");
    (steps.length ? steps[steps.length - 1] : main).appendChild(box);
  }

  document.addEventListener("DOMContentLoaded", async () => {
    const main = document.querySelector("main.content");
    if (!main) return;
    if (!(await ensureProfile())) return; // нет профиля — показан экран «Привет», страница перезагрузится после выбора
    await syncFromServer(); // сначала подтягиваем прогресс с сервера — потом рисуем меню и галочки
    if (main.dataset.page === "home") { location.replace(resumeUrl()); return; } // отдельной главной нет
    personalize(main); // «Misha» в путях и примерах → имя профиля ученика
    const { me, flat } = buildShell(main);
    JC.lessonId = me && me.id;
    if (me && me.index !== undefined) store.set("jc.lastLesson", me.id); // логотип и «/» вернут сюда
    if (main.dataset.wide !== undefined) main.parentNode.classList.add("wide");
    if (me && me.index !== undefined) {
      decorateHead(main);
      trainingReminder(main, me);
      buildFinish(main, me, flat);
      setupSteps(main, me.id);
    } else {
      const bar = document.querySelector(".top-progress"); if (bar) bar.remove();
    }
    sayBlocks(document);
    highlightAll(document);
    terminalCopy();
    blockCodeCopy();
    if (JC.mode === "free") { freeAiText(me); freeAiBlocks(me); } else aiLinks();
    profileChip();
    quizzes(me && me.id);
    loadWidgets();
    if (me && me.index !== undefined) glossary(main);
    refreshTasks();
    const t = main.querySelector("h1");
    if (t && !document.title) document.title = t.textContent + " · " + COURSE.title;
    JC.ready = true;
    document.dispatchEvent(new CustomEvent("jc:ready"));
  });
})();
