# 🛡️ OWASP Security Review

## 💳 Full repository audit: payment-dispute-manager (detailed)

> ### ❌ Final Decision: **FAIL**
>
> **📊 Security Score:** 21% · **🚦 Overall Posture:** High risk  
> **🚨 Highest Finding:** Injection and cleartext PAN storage  
> **🌿 Branch:** `develop` · **Compared with:** `develop` · **📅 Reviewed:** 2026-08-10

---

## 📌 At a Glance

| 🚨 Critical | 🔴 High | 🟠 Medium | 🔵 Low | ✅ Passed | ➖ N/A |
|---:|---:|---:|---:|---:|---:|
| **2** | **2** | **2** | **0** | **0** | **3** |

### 💼 Report Summary

This detailed baseline audit of the checked-in application found multiple high-severity issues: SQL injection in a service method, full card numbers stored in seed data and entity fields, unauthenticated endpoints with hardcoded credentials, and unsafe file handling. These weaknesses expose payment and customer data and must be remediated before any production use.

### 🎯 Top Actions

1. **❌ Fix SQL injection** · Critical  
   Convert `searchByEmail` to parameterized queries or repository methods.
2. **❌ Protect sensitive data (PANs)** · Critical  
   Remove PANs from seed data; tokenize or encrypt at rest and mask in responses.
3. **🔐 Implement authentication & hardening** · High  
   Add Spring Security, remove hardcoded creds, and disable H2 console in production.

---

## 🧭 OWASP Top 10 Scorecard

| ID | Security Area | Result | Key Message |
|:--:|---|:--:|---|
| 🔐 A01 | Broken Access Control | ❌ Fail | Endpoints expose sensitive data without auth |
| 🔏 A02 | Cryptographic Failures | ❌ Fail | Full PANs stored in plaintext in DB and seeds |
| 💉 A03 | Injection | ❌ Fail | SQL constructed from untrusted input in service layer |
| 🏗️ A04 | Insecure Design | ⚠️ Concern | No data minimization or privacy controls |
| ⚙️ A05 | Security Misconfiguration | ⚠️ Concern | H2 console enabled; unsafe file writes |
| 📦 A06 | Vulnerable and Outdated Components | ➖ N/A | No SCA evidence available in repository
| 🪪 A07 | Identification & Auth Failures | ❌ Fail | Hardcoded credentials and weak login flow |
| 🔗 A08 | Software & Data Integrity Failures | ➖ N/A | No supply-chain evidence in repo scan
| 📡 A09 | Security Logging & Monitoring Failures | ⚠️ Concern | No audit or structured logging present |
| 🌐 A10 | Server-Side Request Forgery | ➖ N/A | No outbound HTTP sinks observed in codebase |

**Legend:** ✅ Pass · ⚠️ Concern · ❌ Fail · ➖ N/A

**How scoring works:** Each applicable OWASP category can earn up to **2 points**:
- ✅ **Pass** = 2 points
- ⚠️ **Concern** = 1 point
- ❌ **Fail** = 0 points
- ➖ **N/A** = Not scored

**🧮 Overall Score:** 3 points earned out of 14 available = **21%**

---

## 🚨 Findings Requiring Action

> **Coverage:** 6 actionable findings are listed below. Pass and N/A categories are excluded.

### ❌ F001 · SQL Injection in DisputeService.searchByEmail

**❌ Critical Severity** · `💉 Injection`  
**🛈 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** An attacker controlling the `email` query parameter can inject SQL, allowing exfiltration or modification of dispute and payment data (including PANs).

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/service/DisputeService.java` · lines 20-23
- **👁️ Issue:** `searchByEmail` builds SQL using string concatenation: `String sql="select * from dispute where customer_email=+email+"` and passes it to `jdbc.queryForList(sql)`.
- **🛠️ Required Outcome:** Use parameterized queries (e.g., `jdbc.queryForList(String sql, Object... args)`) or Spring Data repository methods with method derivation: `List<Dispute> findByCustomerEmail(String email)`. Validate and normalize input.
- **✅ Complete When:** Service uses parameter binding or repository call and unit/integration tests verify malicious payloads are not executed; SAST scan flags resolved.

</details>

---

### ❌ F002 · Cleartext PANs persisted in seed data and entity

**❌ Critical Severity** · `🔏 Cryptographic Failures`  
**🛈 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Full Primary Account Numbers (PANs) are present in `data.sql` and the `Dispute` entity stores `cardNumber` as a plain String. This creates a clear PCI-compliance and data breach risk.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/resources/data.sql` · lines 1-2; `src/main/java/com/acme/dispute/entity/Dispute.java` · line 15
- **👁️ Issue:** `data.sql` contains seeded PANs (`4111111111111111`, `5555555555554444`). The `Dispute` entity holds `cardNumber` unencrypted.
- **🛠️ Required Outcome:** Remove PANs from repository seed data; adopt tokenization or format-preserving encryption; store only truncated/masked PANs in application logs and responses.
- **✅ Complete When:** No PANs exist in repo seeds; PANs at rest are encrypted/tokenized and verification shows stored values are not full PANs.

</details>

---

### ❌ F003 · Missing access controls on dispute endpoints

**🔴 High Severity** · `🔐 Broken Access Control`  
**🛈 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Public endpoints allow retrieval of dispute records and sensitive fields without authentication, enabling data leaks.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/DisputeController.java` · lines 15-23
- **👁️ Issue:** Controller exposes `GET /api/disputes/{id}` and `GET /api/disputes/search` without authentication or authorization checks.
- **🛠️ Required Outcome:** Integrate Spring Security with role-based access control; redact card number and sensitive fields from public DTOs.
- **✅ Complete When:** Endpoints require authenticated accounts with appropriate roles; automated tests verify unauthorized requests return 401/403.

</details>

---

### ❌ F004 · Hardcoded credentials and weak auth

**🔴 High Severity** · `🪪 Identification and Authentication Failures`  
**🛈 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Literal credentials in `AuthController` (`admin` / `admin123`) allow trivial compromise of admin actions or test accounts.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/AuthController.java` · lines 8-12
- **👁️ Issue:** `login` compares input to hardcoded strings and returns plaintext status. No password hashing, no account management.
- **🛠️ Required Outcome:** Remove hardcoded credentials, adopt secure auth storage (hashed passwords), use Spring Security and session/token management, and add rate limiting/account lockout.
- **✅ Complete When:** No hardcoded secrets; authentication uses secure store and hashing; tests validate correct behavior.

</details>

---

### ❗ F005 · Unsafe file upload and filesystem writes

**🟠 Medium Severity** · `⚙️ Security Misconfiguration`  
**🛈 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Unvalidated `originalFilename` written to `uploads/` can enable path traversal, overwriting files, or storing harmful content.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/FileUploadController.java` · lines 9-12
- **👁️ Issue:** `file.transferTo(new File("uploads/" + file.getOriginalFilename()))` writes user-provided filename directly.
- **🛠️ Required Outcome:** Sanitize filenames, enforce storage in a controlled directory, validate file types/sizes, and scan for malware.
- **✅ Complete When:** Uploads use safe storage utilities; tests include path-traversal attempts that are rejected.

</details>

---

### ❗ F006 · Lack of security logging and monitoring

**🟠 Medium Severity** · `📡 Security Logging and Monitoring Failures`  
**🛈 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** No structured audit logs for authentication, data access, or file uploads; incidents may go undetected and forensics are limited.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** multiple controllers/services (e.g., `DisputeController.java`, `FileUploadController.java`)
- **👁️ Issue:** No logging or audit hooks observed for sensitive operations.
- **🛠️ Required Outcome:** Add structured audit logging (user, action, resource, timestamp) for sensitive operations and integrate with monitoring/alerting.
- **✅ Complete When:** Logs capture key events and alerts are produced for suspicious activity.

</details>

---

<details>
<summary><strong>🔬 Detailed Review Evidence</strong> · expand to view scope, origin analysis, and proof</summary>

## 🔍 Review Context

- **📦 Repository:** `payment-dispute-manager-gradle`
- **📝 Request:** Full repository OWASP Top 10 audit (detailed mode)
- **🌿 Branch Comparison:** `develop` against `develop` (baseline)
- **🔀 Effective Change Range:** No code changes in the effective range; this is a baseline repository scan of the checked-in code. (Used `develop...HEAD` which shows develop at HEAD.)
- **📄 Reviewed Surface:** All production Java packages: `controller`, `service`, `repository`, `entity`, configuration and seed data under `src/main`.
- **🚪 Audit Trigger:** Manual request for full repository audit.

## 🧬 Origin Assessment

| Origin | Findings | Assessment |
|---|---:|---|
| 🆕 Introduced by This Change | **0** | No change-specific introductions in the reviewed range |
| 📈 Worsened by This Change | **0** | No changes worsen existing issues |
| 🔦 Exposed or Exercised by This Change | **0** | No exposure by a change; this is baseline evidence |
| 🏛️ Pre-existing Repository Baseline | **6** | Multiple high-severity baseline findings (F001–F006)

### Origin Rules Applied

- **Introduced:** absent on the base branch and added by the reviewed change. (Not applicable)
- **Worsened:** present on the base, with increased reach or impact. (Not applicable)
- **Exposed or exercised:** present on the base and newly reached by changed behavior. (Not applicable)
- **Baseline:** present on the base branch, unchanged, and observed in the checked-in code.

## 🧾 Finding Evidence

### F001 · Technical Proof (SQL Injection)

- **File:** `src/main/java/com/acme/dispute/service/DisputeService.java`
- **Symbol or Lines:** `searchByEmail` method (lines 20-23)
- **Observed Behavior:** Builds SQL using string concatenation and passes to `JdbcTemplate#queryForList`.
- **Trust Boundary:** `email` is controller-supplied query parameter from `DisputeController.search` and flows directly into SQL.
- **Attack Scenario:** HTTP GET /api/disputes/search?email=anything' OR '1'='1 would alter logic and return all rows, exposing PANs.
- **Suggested Direction:** Replace with parameterized query or Spring Data repository method.
- **Verification Approach:** Unit/integration tests that assert payloads containing SQL characters do not change query behavior; SAST no longer flags this pattern.

### F002 · Technical Proof (Cleartext PANs)

- **File:** `src/main/resources/data.sql` (lines 1-2); `src/main/java/com/acme/dispute/entity/Dispute.java` (line 15)
- **Observed Behavior:** Seeded PANs present, entity stores `cardNumber` as String.
- **Trust Boundary:** Repository and database store sensitive payment data in plaintext.
- **Attack Scenario:** Data-breach or insider access exposes full PANs, causing PCI breach and regulatory fines.
- **Suggested Direction:** Remove PANs from repo; use tokenization/encryption and mask in DTOs.
- **Verification Approach:** Repository no longer contains clear PANs; DB stores tokens or encrypted values.

## 📚 Files Reviewed

- src/main/java/com/acme/dispute/controller/DisputeController.java
- src/main/java/com/acme/dispute/controller/AuthController.java
- src/main/java/com/acme/dispute/controller/FileUploadController.java
- src/main/java/com/acme/dispute/service/DisputeService.java
- src/main/java/com/acme/dispute/entity/Dispute.java
- src/main/java/com/acme/dispute/repository/DisputeRepository.java
- src/main/resources/application.yml
- src/main/resources/data.sql
- build.gradle

## 🧪 Evidence Quality

- **Source Review:** Complete for checked-in Java sources and resources.
- **Configuration Review:** Application config and seed data inspected (`application.yml`, `data.sql`).
- **Test Evidence:** No tests present asserting security controls; test evidence absent.
- **Runtime Validation:** Not performed (no running instance examined). Evidence is static source/config review.
- **Dependency Evidence:** No SCA performed; dependency CVEs not evaluated (A06 marked N/A).

## ⚠️ Assumptions and Limitations

- This is a static repository scan of the checked-in code on branch `develop`; no runtime or CI pipeline artifacts were examined.
- No SCA or automated dependency scan was run; A06 is marked N/A for lack of evidence.
- No runtime logs or environment-specific secrets were available; detection of runtime protections is out of scope.

### Commands and outputs used as evidence

```
git status --short
```

Output:

```
 M .github/owasp-pr-audit-report-template.md
D  output/2026-08-10-owasp-pr-audit-full.md
?? output/2026-08-07-owasp-pr-audit-repo-scan.md
?? output/2026-08-10-owasp-pr-audit-repo-scan.md
```

```
git branch --show-current
```

Output:

```
develop
```

```
git diff --unified=80 develop...HEAD
```

Output: (no application code changes between base and HEAD; only local output files differ)

```
git log --oneline --decorate -10
```

Output (truncated):

```
414861b (HEAD -> develop, origin/develop, origin/HEAD) Update agent guardrail failure
2aa679a Update report template
7f54b64 (origin/fixes) Upgrade to J21
eb173f6 Update audit template and prompts
b5bfdea Update audit template and prompts
95b77dd Add init report
025467c Add Owasp agents
73796a4 Add Lombok
4c5d301 Add Lombok
bd6b43a (copilot/worktree-2026-08-03T14-00-39) Add Readme
```

</details>

---

## 📝 Final Acknowledgement

> 🤖 This AI-assisted review is **informational and non-gating**. It does not replace penetration testing, SAST, DAST, software-composition analysis, secret scanning, PCI DSS assessment, or human security approval.

> **Risk acceptance:** Proceeding without remediation requires a separate, documented, time-bound decision by the appropriate human risk owner. This report does not recommend or approve a waiver.

### 🚀 Recommended Next Step

Run the prioritized fixes for F001–F004, then perform SAST and SCA scans and a focused penetration test.

