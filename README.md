# Space Invaders – V&V lab tests (UPM, Bloque 1)

JUnit 5 tests run against the precompiled game `lib/Space-Invaders.jar`. The jar contains deliberately injected defects, so failing tests are expected findings. Do not edit the jar.

| Module | Content |
|---|---|
| `SpaceInvaders_Nivel1` | Black-box tests: one test per case in `casos_de_prueba_nivel1.csv` (143) plus `SmokeTest` |
| `SpaceInvaders_Nivel2` | White-box tests (empty for now) |

## Requirements

- **JDK 21** (e.g. [Eclipse Temurin 21](https://adoptium.net/temurin/releases/?version=21): `winget install EclipseAdoptium.Temurin.21.JDK`). The code targets Java 21 (`maven.compiler.release` 21). Any newer JDK also builds it. The current results were produced on Temurin JDK 23 because JDK 21 was not installed. To change the JDK VS Code uses, edit `.vscode/settings.json`.
- Maven 3.9+.
- VS Code with the *Extension Pack for Java*, or IntelliJ IDEA (open the root `pom.xml`).

## One-time setup

Install the game jar into the local Maven repository:

```
./setup.ps1        # Windows PowerShell
./setup.sh         # Linux / macOS / Git Bash
```

## Running the tests

```
mvn -q test
```

The build always finishes. Failing tests do not stop it (`testFailureIgnore`), because they are findings. The tests run headless (`-Djava.awt.headless=true`). In VS Code, the Test Explorer lists and runs them.

To regenerate the results report after a test run:

```
powershell -ExecutionPolicy Bypass -File tools/generate-report.ps1
```

## Where the reports are written

| Report | Location |
|---|---|
| Surefire (raw results) | `SpaceInvaders_Nivel1/target/surefire-reports/` |
| JaCoCo coverage | `SpaceInvaders_Nivel1/target/site/jacoco/index.html` |
| PASS/FAIL per case ID, with failure details | `docs/RESULTADOS_NIVEL1.md` |
| AI-use log | `docs/ai-trace/README.md` |

## Layout

```
pom.xml                         parent (JUnit 5.10.2, surefire, JaCoCo)
lib/Space-Invaders.jar          game under test
casos_de_prueba_nivel1.csv      test plan (oracle)
INSTRUCCIONES_CLAUDE_CODE.md    task description
SpaceInvaders_Nivel1/src/test/java/nivel1/       tests, one class per method under test
SpaceInvaders_Nivel1/src/test/java/nivel1/util/  Reflect, Keys, BoardFactory, Sprites helpers
SpaceInvaders_Nivel2/           empty module
tools/generate-report.ps1       builds docs/RESULTADOS_NIVEL1.md
```
