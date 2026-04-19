# ByteAndBuy

ByteAndBuy je backendová aplikácia pre multiplayerovú ekonomickú deskovú hru inšpirovanú Monopoly. Projekt je zameraný na návrh herného engine, spracovanie herných pravidiel a prípravu architektúry pre budúci real-time multiplayer.

---

## 🎮 O projekte

Hra simuluje ťahovú deskovú hru pre 2–6 hráčov, kde hráči:

- hádžu kockou a pohybujú sa po hernej doske
- kupujú firmy a spravujú svoj majetok
- platia rôzne poplatky ostatným hráčom
- ťahajú karty s rôznymi efektmi
- reagujú na špeciálne polia (start, väzenie, server, dielňa)

Backend zabezpečuje kompletnú hernú logiku a stav hry.

---

## ⚙️ Použité technológie

- Java 21
- Spring Boot
- REST API
- Lombok

---

## 🧠 Architektúra projektu

Projekt je rozdelený na viac vrstiev:

- **controller** – REST rozhranie pre ovládanie hry
- **service** – herná logika a spracovanie ťahov
- **model** – herné objekty (hráč, pole, hra)
- **factory** – vytváranie herného sveta
- **config** – konfigurácia hry
- **dto** – prenos dát medzi backendom a klientom

---

## 🧩 Hlavné časti systému

### 🎲 Herný engine
Zabezpečuje:
- spracovanie ťahov hráčov
- pohyb po hracej doske
- vyhodnotenie políčok
- správu herného stavu

### 🧑‍🤝‍🧑 Hráči
- pohyb po doske
- správa peňazí
- stav (aktívny / vo väzení / vyradený)

### 🏠 Polia
Implementované typy polí:
- firmy (kupovanie, nájom)
- karty (náhodné udalosti)
- start bonus
- väzenie
- polia ktoré pridávajú bonusy (serverovňa, dielna)

### 💳 Ekonomika
- nákup firiem
- platenie poplatkov
- bonusy za prechod štartom
- bankrot hráča

---

## 📡 API endpoints

- `POST /game/create` – vytvorenie novej hry
- `POST /game/{id}/turn` – hod kockou a vykonanie ťahu
- `POST /game/{id}/buy` – kúpa aktuálneho poľa
- `POST /game/{id}/skip` – odmietnutie kúpy
- `POST /game/{id}/leave` – odchod hráča z hry
- `GET /game/{id}/state` – získanie stavu hry

---

## 🚧 Aktuálny stav projektu

Projekt je vo fáze aktívneho vývoja. Backend hernej logiky je funkčný a stabilný, pričom architektúra je navrhnutá tak, aby umožnila ďalšie rozšírenia.

---

## 🛣️ Plánované rozšírenia

- 🔌 multiplayer pomocou WebSocket (real-time hra viacerých hráčov)
- 🗄️ persistencia hier pomocou PostgreSQL
- 🎨 frontend klient (React)

---

## 💡 Cieľ projektu

Cieľom projektu je vytvoriť plne funkčný backend pre multiplayerovú hru s dôrazom na:
- čistú architektúru
- rozšíriteľnosť
- stavový herný engine
- realistické simulovanie herných mechaník

---

## 👨‍💻 Autor

Backend projekt vytvorený ako osobný vývojový projekt na zlepšenie znalostí:
- Java / Spring Boot
- návrh backend architektúry
- herná logika a stavové systémy