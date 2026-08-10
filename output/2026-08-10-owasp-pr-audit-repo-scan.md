# 🛡️ OWASP Security Review

## 💳 Repository scan: payment-dispute-manager (baseline)

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

This baseline scan found multiple high-risk issues: SQL injection, storage of full card numbers in cleartext, missing authentication and hardcoded credentials, and unsafe file handling. Immediate remediation is required before any production use.

### 🎯 Top Actions

1. **❌ Fix SQL injection** · Critical  
   Use prepared statements or param binding with JdbcTemplate.
2. **❌ Protect sensitive data** · Critical  
   Remove full PANs from storage or encrypt/mask at rest.
3. **🔐 Enforce authentication & hardening** · High  
   Disable H2 console in prod, remove hardcoded creds, add auth.

---

## 🧭 OWASP Top 10 Scorecard

| ID | Security Area | Result | Key Message |
|:--:|---|:--:|---|
| 🔐 A01 | Broken Access Control | ❌ Fail | Endpoints expose sensitive data without auth |
| 🔏 A02 | Cryptographic Failures | ❌ Fail | Full PANs stored in plaintext in DB |
| 💉 A03 | Injection | ❌ Fail | SQL built from untrusted input (JdbcTemplate) |
| 🏗️ A04 | Insecure Design | ⚠️ Concern | No data minimization or privacy controls |
| ⚙️ A05 | Security Misconfiguration | ⚠️ Concern | H2 console enabled; unsafe file writes |
| 📦 A06 | Vulnerable Dependencies | ➖ N/A | No SCA evidence available in repo scan |
| 🪪 A07 | Identification & Auth Failures | ❌ Fail | Hardcoded credentials; weak auth model |
| 🔗 A08 | Software & Data Integrity | ➖ N/A | No evidence of supply-chain issues in repo scan |
| 📡 A09 | Logging & Monitoring Failures | ⚠️ Concern | No application audit/logging present |
| 🌐 A10 | Server-Side Request Forgery | ➖ N/A | No outbound HTTP sinks observed |

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

### ❌ F001 · SQL Injection in searchByEmail

**❌ Critical Severity** · `💉 Injection`  
**🛈 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** An attacker can inject SQL via the search parameter, exfiltrating or altering dispute data, including card numbers.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/service/DisputeService.java` · lines 20-23
- **👁️ Issue:** Method `searchByEmail` constructs SQL using untrusted `email` input: `String sql="select * from dispute where customer_email=+email+"` and calls `jdbc.queryForList(sql)`.
- **🛠️ Required Outcome:** Use parameterized queries (JdbcTemplate with `queryForList(String sql, Object... args)` or PreparedStatement) and validate input.
- **✅ Complete When:** Inputs cannot alter SQL structure and tests show attempted payloads are rejected or parameterized queries return expected results.

</details>

---

### ❌ F002 · Cleartext PANs persisted and seeded

**❌ Critical Severity** · `🔏 Cryptographic Failures`  
**🛈 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Full card numbers (PANs) are stored in the database (seed data and entity field), risking PCI exposure and data breach.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/resources/data.sql` · lines 1-2; `src/main/java/com/acme/dispute/entity/Dispute.java` · line 15
- **👁️ Issue:** `data.sql` inserts `card_number` values such as `4111111111111111`. Entity `Dispute` stores `cardNumber` as a plain String.
- **🛠️ Required Outcome:** Remove seeded PANs, tokenize or encrypt PANs at rest, and apply data retention/minimization. Ensure PCI compliance controls.
- **✅ Complete When:** No PANs present in repo or database seeds; PANs encrypted/tokenized; verification via code review and DB inspection.

</details>

---

### ❌ F003 · Missing access controls on dispute endpoints

**🔴 High Severity** · `🔐 Broken Access Control`  
**🛈 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Unauthenticated users can retrieve dispute records and sensitive fields, exposing customer data and card details.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/DisputeController.java` · lines 15-23
- **👁️ Issue:** Controller methods (GET `/api/disputes/{id}`, `/api/disputes/search`) have no authentication or authorization checks.
- **🛠️ Required Outcome:** Implement authentication and authorization (Spring Security), enforce least privilege, and redact sensitive fields from API responses.
- **✅ Complete When:** Endpoints require authentication and only authorized roles can access sensitive fields; automated tests assert access controls.

</details>

---

### ❌ F004 · Hardcoded credentials and weak auth

**🔴 High Severity** · `🪪 Identification and Authentication Failures`  
**🛈 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Hardcoded username/password in `AuthController` enables trivial compromise of admin functions.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/AuthController.java` · lines 8-12
- **👁️ Issue:** `login` compares credentials to literal strings `admin` / `admin123` and returns plaintext success/failure.
- **🛠️ Required Outcome:** Remove hardcoded credentials, integrate standard authentication (Spring Security), store hashed passwords, and implement account lockout/monitoring.
- **✅ Complete When:** No hardcoded credentials remain; authentication uses secure storage and hashing; tests validate login flow.

</details>

---

### ❗ F005 · Unsafe file upload and filesystem write

**🟠 Medium Severity** · `⚙️ Security Misconfiguration`  
**🛈 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Uploaded files are written using the original filename to `uploads/` without validation, enabling path traversal or arbitrary file overwrite.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/FileUploadController.java` · lines 9-12
- **👁️ Issue:** `file.transferTo(new File("uploads/" + file.getOriginalFilename()));` uses client-controlled filename directly.
- **🛠️ Required Outcome:** Validate and sanitize filenames, enforce storage directory boundaries, apply virus scanning and size limits, and restrict file types.
- **✅ Complete When:** Uploads use safe storage APIs, invalid filenames rejected, and tests demonstrate sanitized storage.

</details>

---

### ❗ F006 · Lack of security logging and monitoring

**🟠 Medium Severity** · `📡 Security Logging and Monitoring Failures`  
**🛈 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** No application-level audit logs or monitoring; incidents could go undetected and forensic analysis is hampered.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** multiple files (e.g., `src/main/java/com/acme/dispute/controller/DisputeController.java`, `FileUploadController.java`)
- **👁️ Issue:** Controllers and services contain no logging statements or audit hooks for sensitive operations.
- **🛠️ Required Outcome:** Add structured audit logging for authentication, file uploads, and data access. Integrate with monitoring/alerting.
- **✅ Complete When:** Logs capture who, what, when, and where for sensitive actions and alerting is configured.

</details>

---

## 📝 Final Acknowledgement

> 🤖 This AI-assisted review is **informational and non-gating**. It does not replace penetration testing, SAST, DAST, SCA, or human security approval.

> **Risk acceptance:** Proceeding without remediation requires a separate, documented, time-bound decision by the appropriate human risk owner. This report does not recommend or approve a waiver.

### 🚀 Recommended Next Step

Run prioritized fixes for F001–F004 and re-run SAST and SCA scans; then perform a focused penetration test.

