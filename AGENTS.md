# Repository Agent Guidance

## Purpose

`payment-dispute-manager` is a deliberately vulnerable Spring Boot demonstration application for payment-dispute processing. Its main purpose is to demonstrate an AI-assisted OWASP Top 10 PR audit that decides whether a change warrants a full security report and, when required, creates a polished evidence-based Markdown report.

The vulnerabilities are intentional training material. Never silently remediate them unless the user explicitly requests a remediation exercise.

## Technology and structure

- Java 17
- Spring Boot 3
- Gradle with the checked-in wrapper
- H2 in-memory database
- Lombok may be used for boilerplate reduction
- Production packages live under `src/main/java/com/acme/dispute`
- Configuration and seed data live under `src/main/resources`
- Generated OWASP reports belong in `output/`

Expected layering:

- `controller`: HTTP boundary and request handling
- `service`: business logic and deliberately vulnerable query behavior
- `repository`: Spring Data persistence
- `entity`: persistence model

## Local commands

Windows:

```powershell
.\gradlew.bat clean test
.\gradlew.bat bootRun
```

Git Bash, macOS, or Linux:

```bash
./gradlew clean test
./gradlew bootRun
```

Useful evidence commands:

```bash
git status --short
git branch --show-current
git diff --name-status develop...HEAD
git diff --stat develop...HEAD
git diff --unified=80 develop...HEAD
git log --oneline --decorate -10
```

If `develop...HEAD` is unavailable because the current work is uncommitted or already on `develop`, inspect both `git diff` and `git diff --cached` and state the effective range used.

## Agent roles

- `change-planner`: creates a bounded implementation plan once. It does not edit files.
- `coder`: implements an approved production-code handoff. It does not own tests or review its own work.
- `tester`: owns test changes and execution evidence. It does not alter production behavior.
- `reviewer`: performs a read-only final quality review and returns `APPROVED` or `CHANGES_REQUESTED`.
- `owasp-pr-auditor`: performs a report-only OWASP Top 10 audit. It may create or replace one report under `output/`, but must not alter application code, tests, configuration, dependencies, or seed data.

The normal development loop is:

```text
change-planner -> coder -> tester -> reviewer
                         ^             |
                         |-- changes --|
```

The OWASP route is separate and runs manually after a change is ready:

```text
/final-pr-readiness -> eligibility decision -> full OWASP report when required
/owasp-pr-audit     -> force a full OWASP report
```

## OWASP audit principles

The audit must distinguish among:

1. `Introduced by this change`
2. `Exposed or exercised by this change`
3. `Pre-existing repository baseline`
4. `Not applicable or insufficient evidence`

A small PR must not be blamed for every baseline weakness. The report must explain why each finding is relevant and where it originated.

A full report is normally warranted when a change touches any of these surfaces:

- Controllers, endpoints, request bodies, query parameters, path variables, headers, or cookies
- Authentication, authorization, ownership, tenant, or role checks
- Database queries, repositories, search, filters, or persistence
- File upload, download, path construction, or filesystem access
- Card, payment, dispute, customer, credential, token, or other sensitive data
- Logging, exception messages, tracing, or monitoring
- Application configuration, CORS, actuator, debug functions, or error disclosure
- Serialization, deserialization, schema validation, or data binding
- Outbound HTTP calls or caller-controlled destinations
- Gradle dependencies, plugins, repositories, or supply-chain controls

Documentation-only, comment-only, formatting-only, and clearly non-executable metadata changes usually receive a short no-report decision.

## Audit scoring

For each applicable OWASP category:

- `Pass` = 2 points
- `Concern` = 1 point
- `Fail` = 0 points
- `N/A` is excluded from the denominator and requires a rationale

Disposition:

- `PASS`: score at least 80%, with no Fail and no unresolved High or Critical finding
- `REVIEW REQUIRED`: score 60% to 79%, or at least one Concern
- `FAIL / WAIVER RECOMMENDED`: score below 60%, any applicable Fail, or any unresolved Critical/High finding that materially affects the change path

## Evidence rules

- Ground every factual finding in checked-in code, a Git diff, command output, or clearly labelled missing evidence.
- Include repository-relative file paths and line numbers or symbols whenever possible.
- Never invent command results, CVEs, runtime behavior, branches, commits, tests, or coverage.
- Do not claim exploitability without explaining the input, sink, missing control, and realistic payment-dispute impact.
- Treat demonstration card values as synthetic data, while still assessing unsafe full-PAN handling patterns.
- Keep the audit informational and non-gating. Human reviewers retain final judgment.
