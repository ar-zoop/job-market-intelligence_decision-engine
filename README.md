# Job Market Intelligence – Decision Engine

## What Is This?

**Problem:** Most job-matching tools are prompt-only and opaque; scores and decisions aren’t reproducible or explainable.

**Solution:** A deterministic Java engine that scores resume–job fit, labels decisions (APPLY_NOW / PREPARE_THEN_APPLY / SKIP), and explains skill gaps and role alignment. Raw text is first normalized by a separate Python (LLM) service; this repo does the **hybrid AI + deterministic backend**—interpretation upstream, decisions here.

**Who it’s for:** Engineers and product people who want reproducible, explainable job-fit logic; teams building job-matching or career tools with a clear split between AI interpretation and rule-based scoring.

---

## Architecture

```
  resume.txt + jds/jd_*.txt
           │
           ▼
  ┌─────────────────────────┐
  │  Python service         │  (localhost:8000)
  │  /analyze-resume        │
  │  /analyze-job           │  → normalized JSON
  └───────────┬─────────────┘
              │
              ▼
  ┌─────────────────────────┐
  │  Java (this repo)       │
  │  JobScorer              │  → fit score + decision
  │  GapExplanationService  │  → matched/missing skills, role
  │  ActionPlanService      │  → prioritised actions
  └───────────┬─────────────┘
              │
              ▼
  JobMatchResult → console (and future API/UI)
```

---

## Tech Stack

- **Java 21**, Maven
- **Jackson** (JSON)
- **Java 11+ HTTP client** (calls Python service)
- **Python companion** (separate repo): LLM-based resume/job normalizer

---

## How to Run

**1. Start the Python service** (https://github.com/ar-zoop/job-market-intelligence_ai-implementation; must be running on `http://localhost:8000`):

```bash
# In job-market-intelligence_ai-implementation (or your Python normalizer repo)
# pip install -r requirements.txt
# python app.py
```

**2. Run the Java main:**

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.jobintelligence.Main"
```

**3. Folder structure (inputs):**

```
src/main/resources/
  resume.txt          ← your resume text (plain)
  jds/
    jd_1.txt
    jd_2.txt
    jd_3.txt
    jd_4.txt          ← job description texts (plain)
```

Put your resume content in `resume.txt` and one JD per file in `jds/`. The engine reads these, sends them to the Python service for normalization, then scores and explains.

---

## Sample Input / Output

**Resume text sample** (`resume.txt`):

```
Arzoo Bapna. CS graduate. Skilled in Java, Spring Boot, Vert.x, REST APIs,
MySQL, PostgreSQL, Redis, Kafka, Docker, Git. Software Engineer at American Express.
Backend microservices, Java, Vert.x, Redis, Kafka, Docker, SQL/NoSQL, REST APIs.
```

**Job text sample** (`jds/jd_1.txt`):

```
Backend Engineer. Required: Java, REST, SQL. Nice to have: Docker, Kafka.
```

**Example output (trimmed):**

```
Job: backend_engineer_1
Fit Score: 72
Decision: APPLY_NOW

Matched Required Skills: [java, rest, sql]
Missing Required Skills: []
Matched Optional Skills: [docker, kafka]
Missing Optional Skills: []

Role Alignment: Exact match. Job role: backend, Resume role: backend
```
