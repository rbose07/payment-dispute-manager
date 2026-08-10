
# 🛡️ OWASP Security Review

## 💳 Repository Baseline Scan

> ### ❌ Final Decision: **FAIL / WAIVER RECOMMENDED**
>
> **📊 Security Score:** 21% · **🚦 Overall Posture:** High risk
> **🚨 Highest Finding:** Multiple High findings (A01, A02, A03, A07)
> **🌿 Branch:** `develop` · **Compared with:** `develop` · **📅 Reviewed:** 2026-08-10

---

## 📌 At a Glance

| 🚨 Critical | 🔴 High | 🟠 Medium | 🔵 Low | ✅ Passed | ➖ N/A |
|---:|---:|---:|---:|---:|---:|
| **0** | **4** | **3** | **0** | **0** | **3** |

### 💼 Report Summary

This baseline audit found multiple high-severity, pre-existing issues including hard‑coded credentials, unparameterized SQL, plaintext PANs, and missing access controls. Remediation is required before production use.

### 🎯 Top Actions

1. **❌ Enforce access control** · High  
   Require authentication and per-resource authorization on `/api/disputes/**`.
2. **❌ Remove full PANs from repo** · High  
   Remove or redact seeded PANs and store only masked or tokenized card identifiers.
3. **❌ Parameterize DB queries** · High  
   Replace string-concatenated SQL with prepared statements or JPA repository methods.

---

## 🧭 OWASP Top 10 Scorecard

| ID | Security Area | Result | Key Message |
|:--:|---|:--:|---|
| 🔐 A01 | Broken Access Control | ❌ Fail | Endpoints expose disputes without ownership checks |
| 🔏 A02 | Cryptographic Failures | ❌ Fail | Full PAN stored in entity and seed data |
| 💉 A03 | Injection | ❌ Fail | SQL built from untrusted input in service layer |
| 🏗️ A04 | Insecure Design | ⚠️ Concern | Unsafe file upload path and filename handling |
| ⚙️ A05 | Security Misconfiguration | ⚠️ Concern | H2 console enabled; auto DDL create active |
| 📦 A06 | Vulnerable and Outdated Components | ➖ N/A | No SCA or advisory evidence in repo |
| 🪪 A07 | Identification and Authentication Failures | ❌ Fail | Hard-coded admin credentials in controller |
| 🔗 A08 | Software and Data Integrity Failures | ➖ N/A | No deserialization or integrity evidence found |
| 📡 A09 | Security Logging and Monitoring Failures | ⚠️ Concern | Missing authentication and audit logging |
| 🌐 A10 | Server-Side Request Forgery | ➖ N/A | No outbound HTTP calls observed |

**Legend:** ✅ Pass · ⚠️ Concern · ❌ Fail · ➖ N/A

**How scoring works:** Each applicable OWASP category can earn up to **2 points**:
- ✅ **Pass** = 2 points
- ⚠️ **Concern** = 1 point
- ❌ **Fail** = 0 points
- ➖ **N/A** = Not scored

**🧮 Overall Score:** 3 points earned out of 14 available = **21%**

---

## 🚨 Findings Requiring Action

> **Coverage:** 7 actionable findings are listed below. Pass and N/A categories are excluded.

### ❌ FIND-001 · A01 Broken Access Control on dispute endpoints

**🔴 High Severity** · `A01: Broken Access Control`  
**📍 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Any user can read or search dispute records, exposing customer data and enabling enumeration.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/DisputeController.java` · lines 15-23
- **🔎 Confidence:** High — public controller methods have no auth checks
- **👁️ Issue:** `get(id)` and `search(email)` return dispute data without authentication or ownership checks.
- **🛠️ Required Outcome:** Enforce authentication and per-resource access control; validate ownership or roles.
- **✅ Complete When:** Endpoints require a valid principal and only return disputes the principal is authorized to view.

</details>

---

### ❌ FIND-002 · A02 Plaintext PANs stored in code and seed data

**🔴 High Severity** · `A02: Cryptographic Failures`  
**📍 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Full card PANs present in `Dispute.cardNumber` and `data.sql` increase breach and PCI risks.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/entity/Dispute.java` · lines 14-16; `src/main/resources/data.sql` · lines 1-2
- **🔎 Confidence:** High — PANs visible in seed SQL and model field exists
- **👁️ Issue:** `cardNumber` stored as plaintext and sample full PANs committed to `data.sql`.
- **🛠️ Required Outcome:** Remove full PANs from source, store only masked or tokenized values, and apply encryption if full PAN retention is necessary.
- **✅ Complete When:** No full PANs in repository; CI checks reject PAN-like strings; storage uses tokenization/encryption.

</details>

---

### ❌ FIND-003 · A03 SQL injection in searchByEmail

**🔴 High Severity** · `A03: Injection`  
**📍 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** An attacker-controlled `email` parameter can inject SQL, leading to data exfiltration or modification.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/service/DisputeService.java` · lines 20-22
- **🔎 Confidence:** High — SQL string concatenation observed and executed via JdbcTemplate
- **👁️ Issue:** SQL assembled as `"select * from dispute where customer_email=+email+"` and executed via `jdbc.queryForList` without parameter binding.
- **🛠️ Required Outcome:** Use parameterized queries (e.g., `jdbc.queryForList("select * from dispute where customer_email = ?", email)`), or JPA repository query methods.
- **✅ Complete When:** Dynamic SQL concatenation removed; tests validate inputs with SQL metacharacters are safe.

</details>

---

### ❌ FIND-004 · A07 Hard-coded credentials in AuthController

**🔴 High Severity** · `A07: Identification and Authentication Failures`  
**📍 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Hard-coded credentials (`admin`/`admin123`) enable trivial compromise and bypass of authentication in any deployed instance.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/AuthController.java` · lines 8-12
- **🔎 Confidence:** High — credential literals visible in source
- **👁️ Issue:** Username/password checked against hard-coded literals; no secure auth mechanism present.
- **🛠️ Required Outcome:** Integrate a proper authentication provider (e.g., Spring Security with hashed credentials or external IdP); remove hard-coded secrets from code.
- **✅ Complete When:** No credentials hard-coded; authentication uses secure provider and hashed passwords.

</details>

---

### ⚠️ FIND-005 · A04 Unsafe file upload handling

**🟠 Medium Severity** · `A04: Insecure Design`  
**📍 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Uploads saved using `file.getOriginalFilename()` may allow path traversal or overwrite, enabling data tampering or remote code issues.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/FileUploadController.java` · lines 9-13
- **🔎 Confidence:** Medium — simple file write observed, but attacker-controlled filename impact depends on runtime paths
- **👁️ Issue:** `file.transferTo(new File("uploads/" + file.getOriginalFilename()))` uses attacker-controlled filename without validation.
- **🛠️ Required Outcome:** Validate/sanitize filenames, restrict to a safe upload directory, randomize stored names, and enforce size/type checks.
- **✅ Complete When:** Upload endpoint rejects filenames containing path separators; files stored with generated safe names; unit tests validate traversal attempts fail.

</details>

---

### ⚠️ FIND-006 · A05 H2 console and auto DDL enabled

**🟠 Medium Severity** · `A05: Security Misconfiguration`  
**📍 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** H2 console enabled (`application.yml`) and `ddl-auto: create` increase attack surface and risk of data exposure in non-development environments.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/resources/application.yml` · lines 1-10
- **🔎 Confidence:** High — console enabled and ddl-auto set to create in config
- **👁️ Issue:** `spring.h2.console.enabled: true` and `hibernate.ddl-auto: create` present in config.
- **🛠️ Required Outcome:** Disable H2 console in production profiles and use migrations (Flyway/Liquibase) instead of destructive DDL settings.
- **✅ Complete When:** Sensitive consoles disabled in non-dev profiles; migrations manage schema changes.

</details>

---

### ⚠️ FIND-007 · A09 Missing authentication and audit logging

**🟠 Medium Severity** · `A09: Security Logging and Monitoring Failures`  
**📍 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Missing authentication logs and audit trails reduce detection and response capability after incidents.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** project-wide (no auth/logging code present); see controllers and service classes.
- **🔎 Confidence:** Medium — absence of logging hooks is clear; integration with monitoring unknown
- **👁️ Issue:** No evidence of authentication auditing, login attempt logging, or access logs for sensitive operations.
- **🛠️ Required Outcome:** Integrate structured audit logging for authentication events and sensitive actions; ensure logs are centrally collected and monitored.
- **✅ Complete When:** Authentication and critical operations produce audit logs; tests or runtime checks show logs contain user id, action, timestamp.

</details>

---

## 📝 Final Acknowledgement

> 🤖 This AI-assisted review is **informational and non-gating**. It does not replace penetration testing, SAST, DAST, software-composition analysis, secret scanning, PCI DSS assessment, or human security approval.

### 🚀 Recommended Next Step

Prioritize FIND-001 through FIND-004 remediation, then run SAST and SCA tools and re-audit.

