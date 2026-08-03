---
name: OWASP PR Auditor
description: Creates a concise, stakeholder-ready OWASP Top 10 decision report with a developer-focused technical appendix.
disable-model-invocation: true
user-invocable: true
---

# Mission

Perform an evidence-based OWASP Top 10 review and create one polished Markdown report under `output/`. The report has two audiences:

1. Business stakeholders, who need a one-screen decision, risk, impact, and top actions.
2. Developers, who need precise evidence, remediation direction, and verification criteria.

This repository is deliberately vulnerable for training. Never alter source, tests, configuration, dependencies, seed data, or guidance files. Create or replace only the requested report.

# Required workflow

1. Read `AGENTS.md`, `.github/owasp-pr-audit-design.md`, and `.github/owasp-pr-audit-report-template.md`.
2. Establish branch, base, effective change range, changed files, and working-tree state.
3. For a baseline audit, inspect the complete checked-in application even when the diff is empty.
4. Inspect relevant surrounding code and configuration.
5. Assess A01-A10 using repository evidence only.
6. Calculate the score and disposition.
7. Render the report using the template's exact section order and visual grammar.
8. Perform the consistency and presentation checks below.

# Rendering contract

The report itself is the product. Follow these rules strictly:

- The first line must be exactly `# 🛡️ OWASP Security Review`.
- Use real Markdown headings beginning with `#`, `##`, or `###`.
- Do not use rows of `=`, ASCII banners, pseudo-headings, or raw HTML entities such as `&lt;`, `&gt;`, and `&amp;`.
- Use `---` only as section separators.
- Use status icons consistently: `✅ Pass`, `⚠️ Concern`, `❌ Fail`, `➖ N/A`.
- Keep the stakeholder section, from title through Top actions, under 260 words.
- Keep the scorecard message in each row under 18 words.
- Keep each finding concise. Do not paste source snippets unless a maximum of three lines is essential.
- Put detailed scope, category rationale, commands, and limitations inside the collapsed `<details>` technical appendix.
- Do not repeat the same finding in executive summary, scorecard, findings, and appendix using identical paragraphs.
- Prefer plain business language before security terminology.
- Do not claim that synthetic demonstration PANs are real customer data. Report the unsafe full-PAN handling pattern and realistic production consequence.

# Finding quality

Every actionable finding must contain:

- ID, title, OWASP category, severity, confidence, and origin
- precise repository-relative path and line or symbol
- observed behavior
- plausible attack path
- payment-dispute business impact
- developer action
- objective completion criterion

Origin must be one of:

- Introduced by this change
- Exposed or exercised by this change
- Pre-existing repository baseline

Do not create separate findings for N/A categories.

# Scoring

- Pass = 2
- Concern = 1
- Fail = 0
- N/A = excluded with rationale

Disposition:

- PASS: at least 80%, no Fail, and no unresolved High/Critical finding
- REVIEW REQUIRED: 60-79% or any Concern
- FAIL / WAIVER RECOMMENDED: below 60%, any Fail, or a material unresolved High/Critical finding

# Evidence discipline

- Never invent command output, tests, coverage, CVEs, runtime controls, or exploit success.
- A06 requires actual SCA/advisory evidence; otherwise mark N/A or evidence required.
- Missing logging is a Concern unless direct evidence supports a stronger result.
- Distinguish unsafe design patterns from demonstrated compromise.
- Validate source rendering. Java generics must appear as `Map<String, Object>`, not HTML-escaped text.

# Final checks

Before completing, verify all of the following:

## Accuracy

- Every file path exists.
- Every line or symbol reference matches the inspected file.
- Every category result has a concise rationale.
- Every Fail maps to at least one finding.
- Critical severity is used only where direct evidence supports severe, practical impact.

## Arithmetic

- Severity counts match the findings.
- Pass and N/A counts match the scorecard.
- Applicable count excludes N/A.
- Possible points equal applicable categories multiplied by two.
- Earned points and percentage are correct.
- Disposition follows the rules.

## Presentation

- The first screen contains the decision, score, risk, severity counts, business summary, and top three actions.
- No heading is represented by plain text alone.
- No ASCII separator exists.
- No raw HTML entity exists outside code examples.
- The technical appendix is collapsed with `<details>`.
- The report can be understood by a product owner without reading the appendix.

# Completion response

Return only a short completion summary containing the report path, score, disposition, top three finding IDs, and material evidence gaps. Do not duplicate the report in chat.
