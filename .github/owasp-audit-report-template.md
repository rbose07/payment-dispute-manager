<!--
RENDERING MODE

Select exactly one mode before generating the report.

1. EXECUTIVE mode, default
Use when the request is generic, for example:
- Scan my entire repository or branch for security violations.
- Find OWASP findings and generate a report.
- Review this full repository or current branch for OWASP Top 10 risks.

Render only:
- Executive Decision
- At a Glance
- Report Summary
- Top Actions
- OWASP Scorecard
- Findings Requiring Action
- Final Acknowledgement

2. DETAILED mode, explicit opt-in only
Use only when the request explicitly asks for proof, evidence, technical detail,
files scanned, methodology, how the result was created, or a detailed report.

Render every EXECUTIVE section plus:
- Detailed Review Evidence
- Review Scope
- Origin Assessment
- Evidence Quality
- Files Reviewed
- Assumptions and Limitations

Do not render placeholders, unused sections, instructions, or these comments.
Do not repeat the same information in multiple sections. Each section has one purpose.

FINDING INCLUSION RULES
- Repository baseline scan: show every actionable finding once under Findings Requiring Action.
- PR or branch scan: show every finding introduced, worsened, or materially exposed by the branch.
- Do not create finding blocks for Pass or N/A categories.
- Unrelated pre-existing baseline findings do not belong in Findings Requiring Action; summarize only materially relevant baseline debt in the technical appendix when DETAILED mode is requested.
- Keep each finding concise. Do not omit an actionable finding merely to shorten the report.
-->

# {{REPORT_ICON}} OWASP Security Review

> **{{REPORT_TYPE_ICON}} Report Type:** {{REPORT_TYPE}}

## 💳 {{REPORT_TITLE}}

> ### {{DISPOSITION_ICON}} Final Decision: **{{DISPOSITION}}**
>
> **📊 Security Score:** {{SCORE_PERCENT}} · **🚦 Overall Posture:** {{OVERALL_POSTURE}}  
> **🚨 Highest Finding:** {{HIGHEST_FINDING}}  
> **🌿 Branch:** `{{CURRENT_BRANCH}}` · **Compared with:** `{{BASE_BRANCH}}` · **📅 Reviewed:** {{AUDIT_DATE}}

---

## 📌 At a Glance

| 🚨 Critical | 🔴 High | 🟠 Medium | 🔵 Low | ✅ Passed | ➖ N/A |
|---:|---:|---:|---:|---:|---:|
| **{{CRITICAL_COUNT}}** | **{{HIGH_COUNT}}** | **{{MEDIUM_COUNT}}** | **{{LOW_COUNT}}** | **{{PASS_COUNT}}** | **{{NA_COUNT}}** |

### 💼 Report Summary

{{BUSINESS_SUMMARY_MAX_80_WORDS}}

### 🎯 Top Actions

1. **{{ACTION_1_ICON}} {{ACTION_1_TITLE}}** · {{ACTION_1_SEVERITY}}  
   {{ACTION_1_MAX_20_WORDS}}
2. **{{ACTION_2_ICON}} {{ACTION_2_TITLE}}** · {{ACTION_2_SEVERITY}}  
   {{ACTION_2_MAX_20_WORDS}}
3. **{{ACTION_3_ICON}} {{ACTION_3_TITLE}}** · {{ACTION_3_SEVERITY}}  
   {{ACTION_3_MAX_20_WORDS}}

---

## 🧭 OWASP Top 10 Scorecard

| ID | Security Area | Result | Key Message |
|:--:|---|:--:|---|
| 🔐 A01 | Broken Access Control | {{A01_RESULT}} | {{A01_MESSAGE_MAX_14_WORDS}} |
| 🔏 A02 | Cryptographic Failures | {{A02_RESULT}} | {{A02_MESSAGE_MAX_14_WORDS}} |
| 💉 A03 | Injection | {{A03_RESULT}} | {{A03_MESSAGE_MAX_14_WORDS}} |
| 🏗️ A04 | Insecure Design | {{A04_RESULT}} | {{A04_MESSAGE_MAX_14_WORDS}} |
| ⚙️ A05 | Security Misconfiguration | {{A05_RESULT}} | {{A05_MESSAGE_MAX_14_WORDS}} |
| 📦 A06 | Vulnerable and Outdated Components | {{A06_RESULT}} | {{A06_MESSAGE_MAX_14_WORDS}} |
| 🪪 A07 | Identification and Authentication Failures | {{A07_RESULT}} | {{A07_MESSAGE_MAX_14_WORDS}} |
| 🔗 A08 | Software and Data Integrity Failures | {{A08_RESULT}} | {{A08_MESSAGE_MAX_14_WORDS}} |
| 📡 A09 | Security Logging and Monitoring Failures | {{A09_RESULT}} | {{A09_MESSAGE_MAX_14_WORDS}} |
| 🌐 A10 | Server-Side Request Forgery | {{A10_RESULT}} | {{A10_MESSAGE_MAX_14_WORDS}} |

**Legend:** ✅ Pass · ⚠️ Concern · ❌ Fail · ➖ N/A

**How scoring works:** Each applicable OWASP category can earn up to **2 points**:
- ✅ **Pass** = 2 points
- ⚠️ **Concern** = 1 point
- ❌ **Fail** = 0 points
- ➖ **N/A** = Not scored

**🧮 Overall Score:** {{EARNED_POINTS}} points earned out of {{POSSIBLE_POINTS}} available = **{{SCORE_PERCENT}}**

---

## 🚨 Findings Requiring Action

> **Coverage:** {{ACTIONABLE_FINDING_COUNT}} actionable findings are listed below. Pass and N/A categories are excluded.

<!--
BASELINE MODE: repeat this block for every actionable repository finding.
PR MODE: repeat this block for every finding introduced, worsened, or materially exposed by the branch.
Do not include unrelated baseline findings in this section.
Do not create findings for Pass or N/A categories.
-->

### {{FINDING_ICON}} {{FINDING_ID}} · {{FINDING_TITLE}}

**{{SEVERITY_ICON}} {{SEVERITY}} Severity** · `{{OWASP_CATEGORY}}`  
**{{ORIGIN_ICON}} Origin:** {{ORIGIN}}

> **💼 Business Impact:** {{BUSINESS_IMPACT_MAX_35_WORDS}}

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `{{FILE_PATH}}` · `{{SYMBOL_OR_LINES}}`
- **👁️ Issue:** {{ISSUE_MAX_35_WORDS}}
- **🛠️ Required Outcome:** {{REQUIRED_OUTCOME_MAX_35_WORDS}}
- **✅ Complete When:** {{VERIFICATION_MAX_30_WORDS}}

</details>

<!-- End repeated finding block. -->

---

<!-- DETAILED MODE ONLY: omit everything from here to the closing marker in EXECUTIVE mode. -->

<details>
<summary><strong>🔬 Detailed Review Evidence</strong> · expand to view scope, origin analysis, and proof</summary>

<!-- DETAILED MODE ONLY: omit this entire block in summary mode. -->

---

# 🔬 Technical Evidence

> Supporting code evidence, affected files, origin analysis, and validation coverage.

## 🧾 Evidence Summary

- **Findings with technical proof:** {{PROVEN_FINDING_COUNT}}
- **Files reviewed:** {{FILES_REVIEWED_COUNT}}
- **Review method:** {{REVIEW_METHOD}}
- **Runtime validation:** {{RUNTIME_VALIDATION_STATUS}}
- **Dependency analysis:** {{DEPENDENCY_ANALYSIS_STATUS}}

## 🔍 Review Context

- **📦 Repository:** `{{REPOSITORY_NAME}}`
- **📝 Request:** {{REQUEST_SUMMARY}}
- **🌿 Branch Comparison:** `{{CURRENT_BRANCH}}` against `{{BASE_BRANCH}}`
- **🔀 Effective Change Range:** `{{EFFECTIVE_RANGE}}`
- **📄 Reviewed Surface:** {{REVIEWED_SURFACE}}
- **🚪 Audit Trigger:** {{TRIGGER_REASON}}

## 🧬 Origin Assessment

| Origin | Findings | Assessment |
|---|---:|---|
| 🆕 Introduced by This Change | **{{INTRODUCED_COUNT}}** | {{INTRODUCED_SUMMARY_MAX_25_WORDS}} |
| 📈 Worsened by This Change | **{{WORSENED_COUNT}}** | {{WORSENED_SUMMARY_MAX_25_WORDS}} |
| 🔦 Exposed or Exercised by This Change | **{{EXPOSED_COUNT}}** | {{EXPOSED_SUMMARY_MAX_25_WORDS}} |
| 🏛️ Pre-existing Repository Baseline | **{{BASELINE_COUNT}}** | {{BASELINE_SUMMARY_MAX_25_WORDS}} |

### Origin Rules Applied

- **Introduced:** absent on the base branch and added by the reviewed change.
- **Worsened:** present on the base branch, with increased reach, likelihood, affected data, or impact.
- **Exposed or exercised:** present on the base branch and newly reached by changed behavior.
- **Baseline:** present on the base branch, unchanged, and unrelated to the changed execution path.

## 🧾 Finding Evidence

<!-- Repeat only for findings where detailed evidence was requested. -->

### {{FINDING_ID}} · Technical Proof

- **File:** `{{FILE_PATH}}`
- **Symbol or Lines:** `{{SYMBOL_OR_LINES}}`
- **Observed Behavior:** {{TECHNICAL_OBSERVATION_MAX_75_WORDS}}
- **Trust Boundary:** {{INPUT_TO_SINK_OR_MISSING_CONTROL_MAX_60_WORDS}}
- **Attack Scenario:** {{ATTACK_SCENARIO_MAX_60_WORDS}}
- **Suggested Direction:** {{REMEDIATION_DIRECTION_MAX_60_WORDS}}
- **Verification Approach:** {{TECHNICAL_VERIFICATION_MAX_50_WORDS}}

## 📚 Files Reviewed

{{FILES_REVIEWED_LIST}}

## 🧪 Evidence Quality

- **Source Review:** {{SOURCE_EVIDENCE_STATUS}}
- **Configuration Review:** {{CONFIGURATION_EVIDENCE_STATUS}}
- **Test Evidence:** {{TEST_EVIDENCE_STATUS}}
- **Runtime Validation:** {{RUNTIME_EVIDENCE_STATUS}}
- **Dependency Evidence:** {{DEPENDENCY_EVIDENCE_STATUS}}

## ⚠️ Assumptions and Limitations

{{ASSUMPTIONS_AND_LIMITATIONS_MAX_120_WORDS}}

</details>

<!-- END DETAILED MODE ONLY -->

---

## 📝 Final Acknowledgement

> 🤖 This AI-assisted review is **informational and non-gating**. It does not replace penetration testing, SAST, DAST, software-composition analysis, secret scanning, PCI DSS assessment, or human security approval.

> **Risk Acceptance:** Proceeding without remediation requires a separate, documented, time-bound decision by the appropriate human risk owner. This report does not recommend or approve a waiver.

### 🚀 Recommended Next Step

{{NEXT_STEP_MAX_30_WORDS}}
