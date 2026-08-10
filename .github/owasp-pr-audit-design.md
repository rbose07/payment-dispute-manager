# OWASP PR Audit Design

## Objective

Provide a manual, AI-assisted security review for a ready change. The workflow first determines whether the change warrants a full OWASP Top 10 audit. A qualifying change receives a presentation-ready Markdown report containing evidence, scoring, findings, developer actions, and residual risk.

## Non-goals

The audit is not:

- an automated security gate
- a penetration test
- a substitute for SAST, DAST, dependency scanning, secret scanning, or human security approval
- permission to deploy the deliberately vulnerable demo application
- an implementation or remediation agent

## Trigger

Primary command:

```text
/final-pr-readiness
```

Direct demonstration fallback:

```text
/owasp-pr-audit
```

Both are manual. The fallback forces a full report and is useful during a live presentation.

## Change-range resolution

Preferred range:

```bash
git diff <base-branch>...HEAD
```

The auditor must also inspect staged and unstaged changes. If the preferred range is not valid, the report must state the alternative range used. Never silently audit an empty or unrelated range.

## Eligibility

Trigger a full report when changed behavior reaches a trust boundary or materially exercises one. Relevant surfaces include user input, object ownership, authentication, query construction, persistence, sensitive payment data, file paths, logging, configuration, deserialization, external requests, and dependencies.

Do not trigger a full report for a clearly documentation-only, comment-only, formatting-only, or non-executable metadata change unless the user forces the audit.

## Audit scope

The audit includes:

1. The exact change
2. Direct callers and callees
3. Relevant security controls
4. Configuration governing the path
5. Repository-baseline weaknesses materially connected to the changed path

Unrelated repository weaknesses may be recorded as observations but must not distort the PR-specific conclusion.

## Origin labels

- **Introduced by this change**: the diff creates or materially worsens the weakness
- **Exposed or exercised by this change**: the diff reaches an existing vulnerable path
- **Pre-existing repository baseline**: the weakness already exists and is not materially changed
- **Not applicable**: no credible relationship exists for the reviewed scope

## Scoring

- Pass = 2
- Concern = 1
- Fail = 0
- N/A = excluded with rationale

Overall percentage:

```text
earned points / possible applicable points * 100
```

Disposition:

- PASS: at least 80%, no Fail, and no unresolved High/Critical finding
- REVIEW REQUIRED: 60-79% or any Concern
- FAIL: below 60%, any applicable Fail, or a material unresolved High/Critical finding

### Risk acceptance

Allowed audit dispositions are exactly:
- PASS
- REVIEW REQUIRED
- FAIL

The audit must never return a waiver disposition. A waiver or risk acceptance is a separate human governance decision. The report may state that proceeding without remediation requires separate, documented, time-bound approval from the appropriate human risk owner, but it must not recommend or approve that decision.

## Evidence standard

Each claim must be backed by:

- a repository-relative file and line/symbol
- Git diff evidence
- a reproducible command and actual output
- or an explicit evidence gap

Static evidence must not be presented as runtime proof. Dependency CVEs must not be asserted without authoritative scan or advisory evidence.

## Output

Full reports are stored at:

```text
output/YYYY-MM-DD-owasp-pr-audit-<change-slug>.md
```

The report is informational, report-only, and non-gating.
