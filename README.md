# Job Market Intelligence - Decision Engine (Java)

Deterministic backend engine for analyzing job descriptions against a candidate’s resume and generating **fit scores, gap explanations, and action insights**.

This project focuses on **decision logic and explainability**, not prompt engineering.
LLMs are used upstream for normalization, while this engine performs **structured, rule-based evaluation**.

---

## What This Project Does

Given:

* A normalized **Resume Profile** (skills + primary role)
* One or more normalized **Job Profiles**

The engine will:

1. **Compute a Fit Score (0–100)** using weighted logic
2. **Label Job Decisions**

    * `APPLY_NOW`
    * `PREPARE_THEN_APPLY`
    * `SKIP`
3. **Explain Skill Gaps**

    * Matched required skills
    * Missing required skills
    * Matched optional skills
    * Missing optional skills
4. **Explain Role Alignment**

    * Exact match vs mismatch

All outputs are **deterministic and reproducible**.

---

## Why This Exists

Most AI job tools rely purely on prompts.
This engine demonstrates:

* Backend system design
* Deterministic decision making
* Explainable AI principles
* Clean separation between **interpretation** and **decision logic**

---

## Architecture Overview

```
Resume JSON + Job JSON
        ↓
   JobScorer
        ↓
 GapExplanationService
        ↓
   JobMatchResult
        ↓
   Console / API / UI (future)
```

### Layers

| Layer                     | Responsibility                                             |
| ------------------------- | ---------------------------------------------------------- |
| **JobScorer**             | Numerical fit score + decision label                       |
| **GapExplanationService** | Matched/missing skills + role explanation                  |
| **Main**                  | Orchestration and console output                           |
| **Models**                | Data contracts (ResumeProfile, JobProfile, JobMatchResult) |

---

## Scoring Logic

### Weights

* Required Skills Match → **60%**
* Optional Skills Match → **20%**
* Role Alignment → **20%**

### Decision Thresholds

| Score | Decision           |
| ----- | ------------------ |
| ≥ 70  | APPLY_NOW          |
| 50–69 | PREPARE_THEN_APPLY |
| < 50  | SKIP               |

---

## Example Output

```
Job: backend_engineer_1
Fit Score: 50
Decision: PREPARE_THEN_APPLY

Matched Required Skills: [java]
Missing Required Skills: [rest]
Matched Optional Skills: []
Missing Optional Skills: [docker]

Role Alignment: Exact match. Job role: backend, Resume role: backend
```

---

## Project Structure

```
src/main/java/com/jobintelligence/
 ├── Main.java
 ├── model/
 │   ├── ResumeProfile.java
 │   ├── JobProfile.java
 │   └── JobMatchResult.java
 └── service/
     ├── JobScorer.java
     └── GapExplanationService.java

src/main/resources/
 ├── resume.json
 └── job.json
```

---

## Running the Project

### Requirements

* Java 21+
* Maven 3.9+

### Commands

```
mvn clean compile
mvn exec:java
```

---

## Input Format

### resume.json

```json
{
  "skills": ["java", "spring", "sql"],
  "primaryRole": "backend"
}
```

### jds/job.json

```json
{
  "jobId": "job_1",
  "roleType": "backend",
  "requiredSkills": ["java", "rest"],
  "niceToHaveSkills": ["docker"]
}
```

---

## Design Principles

* **Deterministic over probabilistic**
* **Explainable over opaque**
* **Separation of concerns**
* **Stateless services**
* **Human-readable outputs**

---

## Future Enhancements

* Multi-job aggregation and skill prioritization
* Adjacent role alignment tiers
* REST API layer (Spring Boot)
* UI dashboard
* Optional LLM-generated narrative summaries

---

## Companion Project

This engine is designed to work alongside a Python-based **Job Description Normalizer** that converts raw JDs into structured JSON using LLMs.

https://github.com/ar-zoop/job-market-intelligence_ai-implementation/tree/main

* Python → Interpretation
* Java → Decisions

---

