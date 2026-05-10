# ByteAndBuy

ByteAndBuy je multiplayerová ekonomická desková hra inšpirovaná Monopoly.  
Projekt obsahuje vlastný herný engine, real-time multiplayer komunikáciu cez WebSocket a persistenciu hier pomocou PostgreSQL.

Hlavným cieľom projektu je návrh modulárnej backend architektúry, spracovanie herných pravidiel, synchronizácia herného stavu medzi hráčmi a vytvorenie základu pre kompletnú online hru s frontend klientom.

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
- Spring Web
- Spring Data JPA
- Spring WebSocket
- REST API
- WebSocket (real-time komunikácia)
- PostgreSQL
- Hibernate / JPA
- Lombok
- Maven
- Jackson (JSON serializácia)

---

## 🧠 Architektúra projektu

Projekt je rozdelený do viacerých vrstiev a balíkov podľa zodpovednosti:

- **controller** – REST a WebSocket endpointy pre komunikáciu s klientom
- **service** – herná logika, spracovanie ťahov a správa herného stavu
- **model** – hlavné herné objekty a herné mechaniky
- **factory** – vytváranie a inicializácia herného sveta
- **persistence** – snapshot systém a mapovanie herného stavu
- **entity** – databázové entity ukladané do PostgreSQL
- **repository** – prístup k databáze pomocou Spring Data JPA
- **dto** – prenos dát medzi backendom a klientom
- **exception** – centralizované spracovanie chýb a výnimiek
- **config** – konfigurácia aplikácie a herných nastavení

Architektúra je navrhnutá modulárne s cieľom oddeliť hernú logiku, persistenciu a komunikačné vrstvy.

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
- `POST /game/{gameId}/join` – pripojenie sa do hry
- `POST /game/{gameId}/start` – spustenie začiatku hry (po pripojení hráčov)
- `POST /game/{gameId}/roll` – hod kockou a pohyb figurkou
- `POST /game/{gameId}/buy` – kúpa aktuálneho polička
- `POST /game/{gameId}/skip` – odmietnutie kúpy
- `POST /game/{gameId}/draw` – potiahnutie karty z balíčka
- `POST /game/{gameId}/leave` – odchod hráča z hry
- `GET /game/{gameId}/state` – získanie stavu hry

---

## 🚧 Aktuálny stav projektu

Projekt sa momentálne nachádza vo fáze aktívneho vývoja.  
Backend hernej logiky je funkčný a stabilný vrátane:

- 🎲 kompletnej hernej logiky
- 🗄️ persistencie hier pomocou PostgreSQL
- 🔄 snapshot systému pre ukladanie a načítanie hier
- 🌐 multiplayer podpory cez WebSocket
- ⚡ real-time synchronizácie medzi hráčmi

Architektúra projektu je navrhnutá modulárne s dôrazom na ďalšiu rozšíriteľnosť a jednoduchšiu údržbu kódu.

---

## 🛣️ Plánované rozšírenia

- 🎨 frontend klient (React)
- 🧪 automatizované testy
- 🤖 AI hráči / bot hráči
- 🏆 rozšírenie herných mechaník a typov políčok

---

## 💡 Cieľ projektu

Cieľom projektu je vytvoriť kompletnú multiplayerovú hru s dôrazom na:

- čistú a modulárnu architektúru
- rozšíriteľnosť projektu
- stavový herný engine
- real-time multiplayer komunikáciu
- persistenciu hier a obnovu herného stavu
- realistické simulovanie herných mechaník

Projekt aktuálne obsahuje backend server s hernou logikou, databázovou persistenciou a WebSocket komunikáciou.  
V budúcnosti bude rozšírený o frontend klienta a ďalšie herné funkcionality.

---

## 👨‍💻 Autor

Projekt vytvorený ako osobný vývojový projekt zameraný na precvičenie a rozvoj znalostí v oblastiach:

- Java / Spring Boot
- návrh backend architektúry
- návrh multiplayerových systémov
- WebSocket komunikácia a real-time synchronizácia
- databázová persistencia (PostgreSQL)
- herná logika a stavové systémy
- návrh rozšíriteľného a modulárneho kódu

Projekt slúži zároveň ako praktická ukážka práce s komplexnejšou backend architektúrou a multiplayer herným serverom.