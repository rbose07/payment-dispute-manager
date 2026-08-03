# 🛡️ OWASP PR Audit Report

> **Payment Dispute Manager**  
> AI-assisted security review of a ready change against OWASP Top 10

---

## Executive Security Snapshot

| Signal | Result |
|---|---|
| **Final disposition** | `{{PASS / REVIEW REQUIRED / FAIL - WAIVER RECOMMENDED}}` |
| **Security score** | `{{earned}} / {{possible}} = {{percentage}}` |
| **Change risk** | `{{LOW / MEDIUM / HIGH / CRITICAL}}` |
| **Review confidence** | `{{LOW / MEDIUM / HIGH}}` |
| **Applicable categories** | `{{count}} / 10` |
| **Critical / High / Medium / Low** | `{{counts}}` |
| **Full-report trigger** | {{why this change qualified}} |

> **Reviewer takeaway:** {{Two or three sentences summarizing the changed behavior, strongest evidence, and merge/waiver implication.}}

---

## 1. Change Under Review

- **Repository:** `payment-dispute-manager`
- **Current branch:** `{{branch}}`
- **Base branch:** `{{base}}`
- **Effective range:** `{{range}}`
- **Audit date:** `{{YYYY-MM-DD}}`
- **Change summary:** {{summary}}
- **Changed files:**
  - `{{path}}`
- **Security-sensitive surfaces:** {{surfaces}}

### Change classification

| Classification | Evidence |
|---|---|
| Introduced by this change | {{items or none}} |
| Exposed or exercised by this change | {{items or none}} |
| Pre-existing repository baseline | {{items or none}} |
| Evidence gaps | {{items or none}} |

---

## 2. Security Posture Dashboard

Use `✅ Pass`, `⚠️ Concern`, `❌ Fail`, or `➖ N/A`.

| ID | OWASP category | Applicable? | Result | Score | Evidence summary |
|---|---|---:|---|---:|---|
| A01 | Broken Access Control | {{Yes/No}} | {{result}} | {{0/1/2/Excluded}} | {{summary}} |
| A02 | Cryptographic Failures | {{Yes/No}} | {{result}} | {{0/1/2/Excluded}} | {{summary}} |
| A03 | Injection | {{Yes/No}} | {{result}} | {{0/1/2/Excluded}} | {{summary}} |
| A04 | Insecure Design | {{Yes/No}} | {{result}} | {{0/1/2/Excluded}} | {{summary}} |
| A05 | Security Misconfiguration | {{Yes/No}} | {{result}} | {{0/1/2/Excluded}} | {{summary}} |
| A06 | Vulnerable and Outdated Components | {{Yes/No}} | {{result}} | {{0/1/2/Excluded}} | {{summary}} |
| A07 | Identification and Authentication Failures | {{Yes/No}} | {{result}} | {{0/1/2/Excluded}} | {{summary}} |
| A08 | Software and Data Integrity Failures | {{Yes/No}} | {{result}} | {{0/1/2/Excluded}} | {{summary}} |
| A09 | Security Logging and Monitoring Failures | {{Yes/No}} | {{result}} | {{0/1/2/Excluded}} | {{summary}} |
| A10 | Server-Side Request Forgery | {{Yes/No}} | {{result}} | {{0/1/2/Excluded}} | {{summary}} |

### Score calculation

| Metric | Value |
|---|---:|
| Applicable categories | `{{count}}` |
| Earned points | `{{earned}}` |
| Possible points | `{{possible}}` |
| Overall score | `{{percentage}}` |
| Final disposition | `{{disposition}}` |

---

## 3. Priority Findings

### ❌ F-01: {{Short finding title}}

| Attribute | Value |
|---|---|
| **OWASP category** | `{{Axx - name}}` |
| **Severity** | `{{Critical/High/Medium/Low}}` |
| **Confidence** | `{{High/Medium/Low}}` |
| **Origin** | `{{Introduced / Exposed / Baseline}}` |
| **Status** | `Open` |
| **Affected location** | `{{path:line or symbol}}` |

**Evidence**  
{{Exact code behavior and why the control is absent or ineffective. Do not paste excessive source code.}}

**Attack narrative**  
{{Describe attacker-controlled input, vulnerable path or sink, and realistic outcome.}}

**Payment-dispute impact**  
{{Explain effects such as unauthorized case access, PAN/customer exposure, evidence tampering, workflow abuse, or operational disruption.}}

**Recommended action**  
{{Precise remediation direction without editing the repository.}}

**Verification criteria**

- [ ] {{Observable criterion}}
- [ ] {{Negative or authorization test}}
- [ ] {{Logging/configuration/runtime evidence if relevant}}

---

Repeat the finding block for each actionable finding.

## 4. Developer Action Plan

### Fix before merge

1. `{{finding}}` - {{required outcome}}

### Review or explicitly accept before merge

1. `{{finding}}` - {{decision required}}

### Backlog or hardening candidates

1. `{{finding}}` - {{follow-up}}

### Waiver considerations

- **Finding:** `{{ID}}`
- **Reason a waiver may be considered:** {{reason}}
- **Compensating controls required:** {{controls}}
- **Expiry or reassessment trigger:** {{trigger}}

---

## 5. Positive Security Signals

- ✅ {{Control or bounded behavior supported by evidence}}
- ✅ {{Test or configuration evidence}}
- ✅ {{Area not regressed by this change}}

---

## 6. Evidence Appendix

### Files inspected

- `{{path}}`

### Commands executed

```text
{{command}}
{{actual result summary}}
```

### Assumptions and evidence gaps

- {{Gap, why it matters, and what evidence would close it}}

### Scope exclusions

- {{Explicitly excluded item and rationale}}

---

## 7. Residual Risk and Final Acknowledgement

**Residual risk:** {{What remains after the proposed actions or what cannot be established statically.}}

**Recommended next step:** {{Fix, review, waiver, specialist review, or runtime validation.}}

> This AI-assisted OWASP PR audit is informational, report-only, and non-gating. It does not replace penetration testing, SAST, DAST, dependency scanning, secret scanning, PCI DSS assessment, or human security approval. Reviewers retain final judgment.
