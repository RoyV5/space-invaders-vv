# AI-use trace log

The lab only allows generative AI for specific tasks and requires evidence of its use. Record every use below. Anything generated must be reviewed and modified by the group, and disclosed according to the lab rules.

## How to log an entry

Copy the template once per interaction. Paste the prompt and the output exactly as they were, with no edits. Record your own changes in the "Human edits" table.

---

## Entry template

### Entry N – <short title>

| Field | Value |
|---|---|
| Tool | <e.g. NotebookLM / Claude Code (model)> |
| Date | <YYYY-MM-DD> |
| Group member(s) | <names> |
| Task / purpose | <what the AI was used for> |
| Artifacts affected | <files, test IDs> |

**Exact prompt**

```
<paste prompt here>
```

**Raw output**

```
<paste raw output here, or link to the file / commit that holds it>
```

**Human edits**

| Date | Who | File / section | Change made | Reason |
|---|---|---|---|---|
| | | | | |

---

## Entries

### Entry 1 – Nivel 1 test workspace and JUnit implementation

| Field | Value |
|---|---|
| Tool | Claude Code (Claude Opus 5.5) |
| Date | 2026-10-07 |
| Group member(s) | <fill in> |
| Task / purpose | Build the Maven workspace, implement the 143 black-box JUnit tests from `casos_de_prueba_nivel1.csv`, run them and generate `docs/RESULTADOS_NIVEL1.md` |
| Artifacts affected | `pom.xml`, `SpaceInvaders_Nivel1/**`, `SpaceInvaders_Nivel2/pom.xml`, `setup.*`, `.vscode/settings.json`, `tools/generate-report.ps1`, `README.md`, `docs/**` |

**Exact prompt**

```
Read and implement with the files in the folder 'instructions'. Ask questions as needed
```

(The instructions were in `INSTRUCCIONES_CLAUDE_CODE.md`. Decisions made during the session: build on JDK 23 with release 21, and use the repo root as the workspace.)

**Raw output**

```
<link to the commit containing the generated files>
```

**Human edits**

| Date | Who | File / section | Change made | Reason |
|---|---|---|---|---|
| | | | | |
