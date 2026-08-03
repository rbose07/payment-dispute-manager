# Live Demo Runbook

## One-time setup

Copy this bundle into the repository root, commit it on `develop`, and restart IntelliJ if the agents or prompts do not appear immediately.

```bash
git add AGENTS.md DEMO-RUNBOOK.md .github output/.gitkeep
git commit -m "Add Copilot OWASP PR audit workflow"
git push
```

## Recommended small demo change

Create a feature branch:

```bash
git checkout -b feature/dispute-status-filter
```

Make a small change that adds an optional caller-controlled `status` filter to the existing dispute search path. Keep the demo change small enough to review onscreen. Commit it:

```bash
git add src
git commit -m "Add optional dispute status filter"
```

## Readiness route

Open Copilot Chat in agent mode and invoke:

```text
/final-pr-readiness
```

Suggested inputs:

```text
baseBranch: develop
changeSummary: Add an optional status filter to dispute search.
```

Expected decision:

```text
FULL_REPORT_REQUIRED
```

The change touches caller-controlled input, database query behavior, and sensitive dispute retrieval.

## Direct fallback

If live routing is slow or the prompt is not discovered, select the `OWASP PR Auditor` agent and invoke:

```text
/owasp-pr-audit
```

Suggested inputs:

```text
baseBranch: develop
changeSummary: Add an optional status filter to dispute search.
reportSlug: dispute-status-filter
```

## Expected artifact

```text
output/YYYY-MM-DD-owasp-pr-audit-dispute-status-filter.md
```

Open the report and present these sections in order:

1. Executive Security Snapshot
2. Security Posture Dashboard
3. The highest-severity two or three findings
4. Change classification: introduced, exposed, baseline
5. Developer Action Plan
6. Residual Risk and Final Acknowledgement

## Important presentation line

> The small change did not create every weakness. It crossed a security-sensitive boundary, causing the agent to inspect the relevant path and surface both change-specific and pre-existing repository risks.
