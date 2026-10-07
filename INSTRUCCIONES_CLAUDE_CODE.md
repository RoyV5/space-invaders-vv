# Task: JUnit 5 black-box tests for Space Invaders (V&V lab, UPM, Bloque 1 – Nivel 1)

You are given three files in the project root:

- `INSTRUCCIONES_CLAUDE_CODE.md` – this file.
- `casos_de_prueba_nivel1.csv` – the 143 designed test cases (UTF-8, columns: `metodo, tecnica, id, entradas_explicitas, entrada_implicita_estado, resultado_esperado`).
- `Space-Invaders.jar` – the game, precompiled (Java 21, `Main-Class: main.Main`, Maven coords `org.example:Space-Invaders-Original:1.0-SNAPSHOT`).

Your job: (1) build a Maven workspace that runs JUnit 5 against the jar from VS Code or IntelliJ, (2) implement one JUnit 5 test per case in the CSV, (3) run them and report which pass and fail. Write all code, comments and reports in English except test display names, which may reuse the Spanish text of the CSV.

The test plan itself was written from the game's javadocs only. The expected results in the CSV are the oracle.

## 1. Rules (read first)

1. **Black box.** Do not decompile the jar or read bytecode logic (no `javap -c`, no decompilers). `javap -p -cp lib/Space-Invaders.jar <class>` to read **signatures only** is allowed and required (see §5).
2. **Never edit the jar and never "fix" the game.** The jar contains deliberately injected defects.
3. **Expected values are fixed.** If a test fails because the game contradicts the CSV, that is a correct result: leave the test as is and report it. The only exception is a case that depends on an assumption in §8 – for those, report the failure and mark it `ASSUMPTION-DEPENDENT` rather than changing the expectation.
4. **One test per case ID**, and the ID must appear in the test's method name or `@DisplayName` (e.g. `AL_01_...` / `"AL-01 ..."`), so results map back to the plan. `@ParameterizedTest` is fine if every row keeps its own ID in its display name.
5. Cases marked `(*)` in the CSV depend on an interpretation; implement them but flag them in the report if they fail.
6. Java **21**, JUnit **5** (Jupiter). No Mockito.
7. Do not implement Nivel 2 (white-box). Only create its empty module.

## 2. Workspace layout

```
SpaceInvaders_Tests/
├── pom.xml                      (parent, packaging pom)
├── lib/Space-Invaders.jar
├── casos_de_prueba_nivel1.csv
├── INSTRUCCIONES_CLAUDE_CODE.md
├── setup.ps1 / setup.sh         (one-time jar install)
├── .vscode/settings.json
├── docs/ai-trace/README.md      (AI-use log template, see §9)
├── SpaceInvaders_Nivel1/        (black-box tests: pom.xml + src/test/java/nivel1/...)
└── SpaceInvaders_Nivel2/        (empty module for later: pom.xml only)
```

The two module folder names must be exactly `SpaceInvaders_Nivel1` and `SpaceInvaders_Nivel2` (lab requirement).

## 3. Build setup

**Jar install (one time).** `setup.sh` / `setup.ps1` run:

```
mvn install:install-file -Dfile=lib/Space-Invaders.jar -DgroupId=org.example -DartifactId=Space-Invaders-Original -Dversion=1.0-SNAPSHOT -Dpackaging=jar
```

**Parent `pom.xml`:** groupId `org.example.tests`, artifactId `spaceinvaders-tests`, modules Nivel1 and Nivel2. Properties: `maven.compiler.release` 21, UTF-8, `junit.version` 5.10.2. Dependencies inherited by children: the game jar and `org.junit.jupiter:junit-jupiter` (test scope). Plugins:

- `maven-compiler-plugin` 3.13.0, release 21.
- `maven-surefire-plugin` 3.2.5 with `<argLine>@{argLine} -Djava.awt.headless=true</argLine>` and **`<testFailureIgnore>true</testFailureIgnore>`** (failing tests are expected findings; the build must still finish and write reports).
- `jacoco-maven-plugin` 0.8.12: `prepare-agent` + `report` bound to `test`, restricted to `main/**` and `space_invaders/**`, excluding `main/Main*` and `main/Commons*`. Report at `target/site/jacoco/index.html`.

**VS Code** (`.vscode/settings.json`): `java.configuration.runtimes` with the detected JDK 21 path (default), `java.test.config` with `vmArgs ["-Djava.awt.headless=true"]`. Required extension: Extension Pack for Java. IntelliJ opens the root `pom.xml` directly. If no JDK 21 is installed, tell the user how to install one (e.g. Temurin 21); do not silently use another version.

## 4. Shared test helpers (`SpaceInvaders_Nivel1/src/test/java/nivel1/util/`)

- `Reflect.invoke(Object target, String name, Class<?>[] types, Object... args)` – `getDeclaredMethod` + `setAccessible(true)` + `invoke`, unwrapping `InvocationTargetException`.
- `Keys.pressed(int vk)` / `Keys.released(int vk)` – `new KeyEvent(new JPanel(), KeyEvent.KEY_PRESSED|KEY_RELEASED, 0, 0, vk, KeyEvent.CHAR_UNDEFINED)`.
- `BoardFactory.stopped()` – `new Board()` and immediately `getTimer().stop()` so the Swing timer cannot change state during a test.

Private `Board` methods to invoke by reflection: `gameInit`, `update`, `update_shots`, `update_aliens`, `update_bomb` (all no-arg).

## 5. Verify signatures before writing tests

Run `javap -p -cp lib/Space-Invaders.jar main.Board main.Commons space_invaders.sprites.Alien 'space_invaders.sprites.Alien$Bomb' space_invaders.sprites.Player space_invaders.sprites.Shot space_invaders.sprites.Sprite` and confirm: exact method names above, whether `Alien.Bomb` is a **static** nested class (how to construct it), which setters are public, and how `Commons` constants are exposed. Adapt the helpers to what you find. If something in this file contradicts the signatures, tell the user instead of guessing.

## 6. Setup recipes per case family

- **Coordinates / sprites:** read results with `getX()` / `getY()` (`Sprite.x`/`y` are package-private; tests live in another package). Position sprites with `setX` / `setY`.
- **`Shot(x, y)` adds offsets** (x+6, y−1). To place a shot at exact coordinates (US-* cases), create it then call `setX` / `setY`. To make a shot invisible use `die()`.
- **Player (`PA`, `KP`, `KR`, `UP-06`):** there is no `dx` getter. Drive state with `Keys.pressed/released` and observe the effect through `act()` + `getX()`. Initial player is at x=179, y=280. Position it with `setX`.
- **Board state:** build scenarios with `setAliens(list)`, `setPlayer`, `setShot`, `setDirection`, `setDeaths`, `setInGame`; then call the private method via `Reflect`; then read back with the getters (`getDeaths`, `getDirection`, `isInGame`, `getMessage`, `getAliens`, `getShot`, `getPlayer`).
- **`update_shots` / `update_aliens` / `update_bomb` cases:** isolate with a list holding only the alien(s) named in the case. Check `isDying()` for "destroyed" aliens/players and `isVisible()` for removed shots.
- **`update_bomb` determinism:** bombs are created randomly (0–14 vs `CHANCE`), but only when the alien's bomb is destroyed. Deterministic cases first make the bomb active: `alien.getBomb().setDestroyed(false)` then `setX/setY` on the bomb. Case `UB-15` is statistical: call `update_bomb` up to 1000 times with a visible alien (bomb destroyed) and assert that at some call `isDestroyed()` becomes false, with bomb `getX()` = alien x and `getY()` within [alien y, alien y + 1].
- **Repeated calls:** "×N" in the CSV means call the method N times in a row; `PA-12` / `PA-13` assert the bound holds after every single call.
- **Shot preconditions:** where a case needs a visible sprite, assert `isVisible()` as a precondition (default visibility is not documented, see S11).

## 7. Game constants (from the javadocs; use `main.Commons.*` in code, hard-code only where the CSV does)

BOARD_WIDTH 358, BOARD_HEIGHT 350, BORDER_LEFT 5, BORDER_RIGHT 30, GROUND 290, ALIEN_WIDTH 12, ALIEN_HEIGHT 12, ALIEN_ROWS 4, ALIEN_COLUMNS 6, ALIEN_SEPARATOR 18, ALIEN_INIT_X 150, ALIEN_INIT_Y 5, GO_DOWN 15, NUMBER_OF_ALIENS_TO_DESTROY 24, CHANCE 5, PLAYER_WIDTH 15, PLAYER_HEIGHT 10, SHOT_SPEED 4, BOMB_SPEED 1, BOMB_HEIGHT 5; `Shot.H_SPACE` 6, `Shot.V_SPACE` 1 (package-private; hard-code 6 and 1).

**Out of scope (do not test):** `Sprite`, `Commons`, `Main`, `Board.GameCycle`, `Board.TAdapter`, all getters/setters, `initBoard`, `doGameCycle`, all `draw*`/`paintComponent`/`doDrawing`/`gameOver`, `Alien.getBomb`, `Alien.Bomb.setDestroyed/isDestroyed`, and the explosion-image effects. They may be *used* to set up state and read results.

## 8. Assumptions behind the expected values (S1–S11)

The javadocs do not fix some values. The CSV uses these interpretations. If a case relying on one fails, report it as `ASSUMPTION-DEPENDENT`, do not change it.

| Id | Ambiguity | Interpretation used |
|---|---|---|
| S1 | Max Y for `Alien`/`Bomb` is not stated (only X = screen width). | Max Y = `BOARD_HEIGHT` = 350. |
| S2 | "Center of the screen" / "10 px above the ground" has no exact formula. | Player at x = `BOARD_WIDTH`/2 = 179, y = `GROUND` − 10 = 280. |
| S3 | Player "valid limits" have no numbers. | Property oracle: 0 ≤ x ≤ `BOARD_WIDTH` − `PLAYER_WIDTH` = 343. |
| S4 | `keyReleased` with the opposite key is undefined. | Not tested. |
| S5 | `update_shots`: border inclusivity, move-then-check order and y=0 are undefined. | 1 px inside / 1 px outside horizontally; clear distances vertically; y=0 after moving not tested. |
| S6 | `update_aliens`: `BORDER_RIGHT` as coordinate vs margin; order "reverse then move"; initial `direction` undocumented. | Right border at `BOARD_WIDTH` − `BORDER_RIGHT` = 328. At borders only direction and the `GO_DOWN` descent are asserted, not final x. `direction` is set with `setDirection`. |
| S7 | `update_bomb`: ground check before/after moving; exact creation y. | y=284 not tested; creation y accepted in [alien y, alien y + 1]. |
| S8 | `update` with deaths > 24 undefined. | Not tested. |
| S9 | Behavior of `Alien.act` / constructors beyond the screen edge after moving. | Not tested. |
| S10 | `Shot(int,int)` with negative / off-screen coordinates. | Not tested. |
| S11 | Default sprite visibility undocumented. | Precondition `isVisible()` asserted where needed. |

## 9. AI-use traceability (lab rule)

The course only allows generative AI (NotebookLM) for specific tasks and requires evidence. Create `docs/ai-trace/README.md` as a template: tool, date, exact prompt, raw output, and a log of human edits. Remind the user that anything generated here should be reviewed and modified by the group and disclosed according to the lab rules.

## 10. Deliverables and reporting

1. The Maven workspace per §2–§4; `mvn -q test` at the root runs both modules.
2. `SpaceInvaders_Nivel1/src/test/java/nivel1/**`: 143 tests, one per CSV row, IDs in the names.
3. A smoke test (`SmokeTest`: `new Player()` not null; `BoardFactory.stopped().getAliens().size()==24`; reflective `update` call does not throw) that passes before the rest.
4. `docs/RESULTADOS_NIVEL1.md`, generated from `target/surefire-reports`, with: totals (passed / failed / skipped), a table of **every case ID with PASS or FAIL**, and for each failure the method, the actual versus expected value and whether it is `ASSUMPTION-DEPENDENT`. The failed IDs are what goes to the debugging phase.
5. JaCoCo report generated for Nivel1 (informational for now).
6. Root `README.md`: JDK needed, one-time setup command, how to run tests, where the reports are written.

## 11. Definition of done

- `mvn -q test` runs without build errors; the smoke test is green; the VS Code Test Explorer lists and runs the tests.
- 143 tests exist and every CSV ID appears exactly once in the results report.
- No expected value was changed to make a test pass. Any deviation from the CSV is listed in the report with the reason.
