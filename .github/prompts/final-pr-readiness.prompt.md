---
agent: owasp-auditor
description: Decide whether the ready change requires a full OWASP Top 10 audit and generate the report when required.
---

Perform final PR security readiness for the current repository state.

Requested base branch: `${input:baseBranch:Base branch, normally develop}`
Change summary: `${input:changeSummary:One sentence describing the intended change}`

Follow `AGENTS.md`, `.github/owasp-audit-design.md`, and the OWASP Auditor instructions.

First establish the effective change range. Inspect committed, staged, and unstaged changes as necessary. Then:

1. Explain which files changed and which security-sensitive surfaces are touched.
2. Decide `FULL_REPORT_REQUIRED` or `FULL_REPORT_NOT_REQUIRED`.
3. Give a short, evidence-based reason and confidence level.
4. If required, immediately perform the complete OWASP A01-A10 audit and create the report under `output/` using the repository template.
5. If not required, do not create a report.
6. Finish with the decision, evidence range, and report path when applicable.

Do not change application or test files.
