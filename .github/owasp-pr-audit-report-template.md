# 🛡️ OWASP Security Review

## 💳 {{CHANGE_TITLE}}

> ### {{DISPOSITION_ICON}} Executive decision: **{{DISPOSITION}}**
>
> **📊 Security score:** {{SCORE_PERCENT}} · **🎯 Risk:** {{RISK}} · **🔎 Confidence:** {{CONFIDENCE}}  
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

**{{FINDING_1_SEVERITY_ICON}} {{FINDING_1_SEVERITY}} severity** · **🔎 {{FINDING_1_CONFIDENCE}} confidence** · `{{FINDING_1_CATEGORY}}` · **🏷️ {{FINDING_1_ORIGIN}}**

> **💼 Business impact:** {{FINDING_1_BUSINESS_IMPACT_MAX_45_WORDS}}

- **📍 Evidence:** `{{FINDING_1_PATH}}` · `{{FINDING_1_SYMBOL_OR_LINES}}`
- **👁️ Observed behavior:** {{FINDING_1_EVIDENCE_MAX_55_WORDS}}
- **⚔️ Attack path:** {{FINDING_1_ATTACK_MAX_55_WORDS}}
- **🛠️ Developer action:** {{FINDING_1_ACTION_MAX_55_WORDS}}
- **✅ Done when:** {{FINDING_1_VERIFY_MAX_40_WORDS}}

<!-- Repeat this concise block for every actionable finding. Do not create finding blocks for N/A categories. -->

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
- **📝 Change summary:** {{CHANGE_SUMMARY}}
- **🌿 Effective range:** `{{EFFECTIVE_RANGE}}`
- **📄 Changed files:** {{CHANGED_FILES_OR_BASELINE}}
- **🚪 Full-report trigger:** {{TRIGGER_REASON}}

## 🧬 Change versus Baseline

| Classification | Assessment |
|---|---|
| 🆕 Introduced by this change | {{INTRODUCED_ITEMS_OR_NONE}} |
| 🔦 Exposed or exercised by this change | {{EXPOSED_ITEMS_OR_NONE}} |
| 🏛️ Pre-existing repository baseline | {{BASELINE_ITEMS}} |
| ❓ Evidence gaps | {{EVIDENCE_GAPS}} |

## 🧮 Scoring Detail

- ✅ **Pass** = 2
- ⚠️ **Concern** = 1
- ❌ **Fail** = 0
- ➖ **N/A** = excluded from the denominator and requires a rationale

{{CATEGORY_RATIONALES_INCLUDING_NA}}

## 📚 Files Inspected

{{FILES_INSPECTED_LIST}}

## ⚠️ Assumptions and Limitations

{{ASSUMPTIONS_AND_LIMITATIONS}}

</details>

---

## 📝 Final Acknowledgement

> 🤖 This AI-assisted review is **informational and non-gating**. It does not replace penetration testing, SAST, DAST, software-composition analysis, secret scanning, PCI DSS assessment, or human security approval. Reviewers retain final judgment.

### 🚀 Recommended Next Step

{{NEXT_STEP_MAX_35_WORDS}}
