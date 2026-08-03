# 🛡️ OWASP Security Review

## 💳 {{CHANGE_TITLE}}

> ### {{DISPOSITION_ICON}} Executive Decision: **{{DISPOSITION}}**
>
> **📊 Security Score:** {{SCORE_PERCENT}} · **🚦 Overall Posture:** {{OVERALL_POSTURE}}  
> **🚨 Highest Finding:** {{HIGHEST_FINDING}} · **🔎 Audit Confidence:** {{AUDIT_CONFIDENCE}}  
> **🌿 Scope:** `{{CURRENT_BRANCH}}` → `{{BASE_BRANCH}}` · **📅 Reviewed:** {{AUDIT_DATE}}

---

## 📌 At a Glance

| 🚨 Critical | 🔴 High | 🟠 Medium | 🔵 Low | ✅ Passed | ➖ N/A |
|---:|---:|---:|---:|---:|---:|
| **{{CRITICAL_COUNT}}** | **{{HIGH_COUNT}}** | **{{MEDIUM_COUNT}}** | **{{LOW_COUNT}}** | **{{PASS_COUNT}}** | **{{NA_COUNT}}** |

### 💼 What the Business Should Know

{{BUSINESS_SUMMARY_MAX_90_WORDS}}

### 🚦 Decision

> {{DECISION_STATEMENT_MAX_45_WORDS}}

### 🎯 Top Three Actions

1. **{{ACTION_1_ICON}} {{ACTION_1_TITLE}}** · `{{ACTION_1_SEVERITY}}`  
   {{ACTION_1_MAX_22_WORDS}}
2. **{{ACTION_2_ICON}} {{ACTION_2_TITLE}}** · `{{ACTION_2_SEVERITY}}`  
   {{ACTION_2_MAX_22_WORDS}}
3. **{{ACTION_3_ICON}} {{ACTION_3_TITLE}}** · `{{ACTION_3_SEVERITY}}`  
   {{ACTION_3_MAX_22_WORDS}}

---

## 🧬 Change Security Impact

| Origin                                     | Count | Meaning |
|--------------------------------------------|---:|---|
| 🆕 **Introduced by This Change**           | **{{INTRODUCED_COUNT}}** | New weakness that does not exist on the base branch |
| 📈 **Worsened by This Change**             | **{{WORSENED_COUNT}}** | Existing weakness whose reach, likelihood, or impact increased |
| 🔦 **Exposed or Exercised by This Change** | **{{EXPOSED_COUNT}}** | Existing vulnerable path newly reached by the changed behavior |
| 🏛️ **Pre-existing Repository Baseline**    | **{{BASELINE_COUNT}}** | Existing issue not created or materially changed by this branch |

> **Branch Assessment:** {{BRANCH_IMPACT_SUMMARY_MAX_60_WORDS}}

---

## 🧭 OWASP Top 10 Scorecard

| ID | Security area | Result | Key message |
|:--:|---|:--:|---|
| 🔐 A01 | Broken Access Control | {{A01_RESULT}} | {{A01_MESSAGE_MAX_18_WORDS}} |
| 🔏 A02 | Cryptographic Failures | {{A02_RESULT}} | {{A02_MESSAGE_MAX_18_WORDS}} |
| 💉 A03 | Injection | {{A03_RESULT}} | {{A03_MESSAGE_MAX_18_WORDS}} |
| 🏗️ A04 | Insecure Design | {{A04_RESULT}} | {{A04_MESSAGE_MAX_18_WORDS}} |
| ⚙️ A05 | Security Misconfiguration | {{A05_RESULT}} | {{A05_MESSAGE_MAX_18_WORDS}} |
| 📦 A06 | Vulnerable and Outdated Components | {{A06_RESULT}} | {{A06_MESSAGE_MAX_18_WORDS}} |
| 🪪 A07 | Identification and Authentication Failures | {{A07_RESULT}} | {{A07_MESSAGE_MAX_18_WORDS}} |
| 🔗 A08 | Software and Data Integrity Failures | {{A08_RESULT}} | {{A08_MESSAGE_MAX_18_WORDS}} |
| 📡 A09 | Security Logging and Monitoring Failures | {{A09_RESULT}} | {{A09_MESSAGE_MAX_18_WORDS}} |
| 🌐 A10 | Server-Side Request Forgery | {{A10_RESULT}} | {{A10_MESSAGE_MAX_18_WORDS}} |

> **Legend:** ✅ Pass · ⚠️ Concern · ❌ Fail · ➖ N/A  
> **🧮 Score:** {{EARNED_POINTS}} / {{POSSIBLE_POINTS}} = **{{SCORE_PERCENT}}**

---

## 🚨 Findings Requiring Action

### {{FINDING_1_ICON}} {{FINDING_1_ID}} · {{FINDING_1_TITLE}}

**{{FINDING_1_SEVERITY_ICON}} {{FINDING_1_SEVERITY}} severity** · **🔎 {{FINDING_1_CONFIDENCE}} audit confidence** · `{{FINDING_1_CATEGORY}}` · **🏷️ {{FINDING_1_ORIGIN}}**

> **🌿 Branch Impact:** {{FINDING_1_BRANCH_DELTA_MAX_45_WORDS}}
>
> **💼 Business Impact:** {{FINDING_1_BUSINESS_IMPACT_MAX_45_WORDS}}

- **📍 Evidence:** `{{FINDING_1_PATH}}` · `{{FINDING_1_SYMBOL_OR_LINES}}`
- **👁️ Observed Behavior:** {{FINDING_1_EVIDENCE_MAX_55_WORDS}}
- **⚔️ Attack Path:** {{FINDING_1_ATTACK_MAX_55_WORDS}}
- **🛠️ Developer Action:** {{FINDING_1_ACTION_MAX_55_WORDS}}
- **✅ Done When:** {{FINDING_1_VERIFY_MAX_40_WORDS}}

<!--
Repeat this concise block for every actionable finding.
Allowed origin values only:
- 🆕 Introduced by This Change
- 📈 Worsened by This Change
- 🔦 Exposed or Exercised by This Change
- 🏛️ Pre-existing Repository Baseline
Do not create finding blocks for N/A categories.
-->

---

## 🏛️ Known Baseline Risks Outside This Change

<!--
For PR audits, list only material pre-existing findings that are unrelated to the changed execution path.
Keep this section brief and do not count these items in the PR-scoped score.
For baseline audits, replace the content with: "Not applicable. This report is the repository baseline."
-->

- **{{BASELINE_FINDING_ID}} · {{BASELINE_FINDING_TITLE}}:** {{BASELINE_FINDING_SUMMARY_MAX_30_WORDS}}  
  **Relationship to this change:** Not introduced, worsened, or exercised by this branch. Refer to the latest baseline audit.

---

## 🗺️ Remediation Roadmap

### 🛑 Fix before Merge or Demonstration Sign-Off

- [ ] **{{FINDING_ID}}:** {{REQUIRED_OUTCOME_MAX_25_WORDS}}

### 🧰 Planned Hardening

- [ ] **{{FINDING_ID}}:** {{FOLLOW_UP_MAX_25_WORDS}}

### 🔬 Evidence Still Needed

- [ ] {{MISSING_EVIDENCE_MAX_30_WORDS}}

### 🤝 Human Decision Required

> The report identifies risk and recommended actions. The developer and reviewer decide whether to remediate, defer, or formally accept the remaining risk.

---

## 🌟 Positive Security Signals

- ✅ {{POSITIVE_SIGNAL_1_MAX_25_WORDS}}
- ✅ {{POSITIVE_SIGNAL_2_MAX_25_WORDS}}
- ✅ {{POSITIVE_SIGNAL_3_MAX_25_WORDS}}

---

<details>
<summary><strong>🧪 Technical appendix</strong> · scope, scoring, evidence, and limitations</summary>

## 🔍 Review Scope

- **📦 Repository:** `payment-dispute-manager`
- **📝 Change Summary:** {{CHANGE_SUMMARY}}
- **🌿 Effective Range:** `{{EFFECTIVE_RANGE}}`
- **📄 Changed Files:** {{CHANGED_FILES_OR_BASELINE}}
- **🚪 Full-Report Trigger:** {{TRIGGER_REASON}}
- **🔎 Audit Confidence:** {{AUDIT_CONFIDENCE}} · {{AUDIT_CONFIDENCE_RATIONALE_MAX_35_WORDS}}

## 🧬 Origin assessment

| Classification                         | Assessment |
|----------------------------------------|---|
| 🆕 Introduced by This Change           | {{INTRODUCED_ITEMS_OR_NONE}} |
| 📈 Worsened by This Change             | {{WORSENED_ITEMS_OR_NONE}} |
| 🔦 Exposed or Exercised by This Change | {{EXPOSED_ITEMS_OR_NONE}} |
| 🏛️ Pre-existing Repository Baseline    | {{BASELINE_ITEMS}} |
| ❓ Evidence Gaps                       | {{EVIDENCE_GAPS}} |

### Origin Decision Rules

- **Introduced:** absent on the base branch and added by this branch.
- **Worsened:** present on the base branch, but this branch increases reach, likelihood, affected data, or impact.
- **Exposed or exercised:** present on the base branch and newly reached by changed behavior without modifying the weakness itself.
- **Baseline:** present on the base branch, unchanged, and unrelated to the changed execution path.

## 🧮 Scoring Boundary

- In **baseline mode**, score all applicable repository findings.
- In **PR mode**, score findings introduced, worsened, or materially exposed by the branch.
- Show unrelated baseline findings separately and do not include them in the PR-scoped score.
- ✅ **Pass** = 2
- ⚠️ **Concern** = 1
- ❌ **Fail** = 0
- ➖ **N/A** = excluded from the denominator and requires a rationale

{{CATEGORY_RATIONALES_INCLUDING_NA}}

## 📚 Files Inspected

{{FILES_INSPECTED_LIST}}

## 🧾 Evidence Quality

- **Source Review:** {{SOURCE_EVIDENCE_STATUS}}
- **Configuration Review:** {{CONFIGURATION_EVIDENCE_STATUS}}
- **Test Evidence:** {{TEST_EVIDENCE_STATUS}}
- **Runtime Validation:** {{RUNTIME_EVIDENCE_STATUS}}

## ⚠️ Assumptions and Limitations

{{ASSUMPTIONS_AND_LIMITATIONS}}

</details>

---

## 📝 Final Acknowledgement

> 🤖 This AI-assisted review is **informational and non-gating**. It does not replace penetration testing, SAST, DAST, software-composition analysis, secret scanning, PCI DSS assessment, or human security approval. Reviewers retain final judgment.

### 🚀 Recommended Next Step

{{NEXT_STEP_MAX_35_WORDS}}
