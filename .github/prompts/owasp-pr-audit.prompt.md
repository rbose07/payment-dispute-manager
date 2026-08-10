---
agent: owasp-pr-auditor
description: Generate a polished, stakeholder-ready OWASP Top 10 report with a concise executive view and developer appendix.
---

Run a full OWASP Top 10 audit.

Base branch: `${input:baseBranch:Base branch, normally develop}`
Change summary: `${input:changeSummary:One sentence describing the change or baseline}`
Report slug: `${input:reportSlug:Lowercase hyphenated report name}`
Audit mode: `${input:auditMode:Use baseline or pr}`
Report mode: `${input:reportMode:Use auto, summary, or detailed}`

Requirements:
- Resolve report mode before rendering:
  - `summary` always produces `📊 Security Assessment Summary`.
  - `detailed` always produces `🧪 Detailed Technical Evidence` and includes the Technical Evidence section.
  - `auto` uses summary for generic requests and detailed only when the user explicitly asks for proof, technical evidence, files reviewed, methodology, trust boundaries, or how findings were established.

1. In `baseline` mode, inspect the complete checked-in application even when no Git diff exists.
2. In `pr` mode, inspect the effective diff plus relevant callers, callees, configuration, and controls.
3. Create `output/YYYY-MM-DD-owasp-pr-audit-${input:reportSlug}.md`.
4. Use `.github/owasp-pr-audit-report-template.md` exactly for section order and visual structure.
5. Keep the stakeholder summary lively and concise; move technical depth to the collapsed appendix.
6. Do not modify any other file.
7. Do not echo the full report in chat after saving it.
