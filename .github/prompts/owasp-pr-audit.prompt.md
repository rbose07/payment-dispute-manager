---
agent: owasp-pr-auditor
description: Force a complete, evidence-based OWASP Top 10 audit and generate a presentation-ready Markdown report.
---

Run a full OWASP Top 10 audit now, even if the changed surface would normally receive only a short readiness acknowledgement.

Requested base branch: `${input:baseBranch:Base branch, normally develop}`
Change summary: `${input:changeSummary:One sentence describing the intended change}`
Report slug: `${input:reportSlug:Short lowercase hyphenated report name}`

Follow `AGENTS.md`, `.github/owasp-pr-audit-design.md`, and `.github/owasp-pr-audit-report-template.md`.

Inspect the Git diff, relevant surrounding execution paths, configuration, dependencies visible in the repository, and available test evidence. Generate one report at:

`output/YYYY-MM-DD-owasp-pr-audit-${input:reportSlug}.md`

Do not edit any other file. Do not invent evidence or command results.
