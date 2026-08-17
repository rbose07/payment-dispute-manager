# 🛡️ OWASP Security Review

> **📊 Report Type:** Security Assessment Summary

## 💳 Repository Baseline Security Assessment

> ### ❌ Final Decision: **FAIL**
>
> **📊 Security Score:** 17% · **🚦 Overall Posture:** Critical Risk  
> **🚨 Highest Finding:** Critical  
> **🌿 Branch:** `develop` · **📅 Reviewed:** 2026-08-10

---

## 📌 At a Glance

| 🚨 Critical | 🔴 High | 🟠 Medium | 🔵 Low | ✅ Passed | ➖ N/A |
|---:|---:|---:|---:|---:|---:|
| **4** | **2** | **1** | **0** | **1** | **1** |

### 💼 Report Summary

The payment-dispute-manager repository contains multiple critical vulnerabilities that severely compromise the security posture. SQL injection, broken access control, hardcoded credentials, and unsafe file handling create immediate risk to payment data and system integrity. The application lacks foundational security controls including authentication, authorization, and data protection mechanisms required for payment processing.

### 🎯 Top Actions

1. **💉 Remediate SQL Injection** · Critical  
   Replace string concatenation in searchByEmail with parameterized queries immediately.
2. **🔐 Implement Authentication and Authorization** · Critical  
   Remove hardcoded credentials and deploy role-based access control on all endpoints.
3. **🔏 Encrypt Payment Card Data** · High  
   Encrypt card numbers at rest and eliminate full-PAN storage in seed data.

---

## 🧭 OWASP Top 10 Scorecard

| ID | Security Area | Result | Key Message |
|:--:|---|:--:|---|
| 🔐 A01 | Broken Access Control | ❌ Fail | No authentication or authorization on any endpoint. |
| 🔏 A02 | Cryptographic Failures | ❌ Fail | Unencrypted PANs stored; hardcoded credentials visible. |
| 💉 A03 | Injection | ❌ Fail | SQL injection via email parameter in search function. |
| 🏗️ A04 | Insecure Design | ❌ Fail | Hardcoded authentication; no ownership-validation model. |
| ⚙️ A05 | Security Misconfiguration | ⚠️ Concern | H2 console enabled; security defaults not hardened. |
| 📦 A06 | Vulnerable and Outdated Components | ✅ Pass | Dependencies reasonably current; no known critical CVEs. |
| 🪪 A07 | Identification and Authentication Failures | ❌ Fail | Plain-text weak credentials embedded in code. |
| 🔗 A08 | Software and Data Integrity Failures | ❌ Fail | Path traversal risk in file upload; no input validation. |
| 📡 A09 | Security Logging and Monitoring Failures | ❌ Fail | No security event logging or monitoring in place. |
| 🌐 A10 | Server-Side Request Forgery | ➖ N/A | No outbound HTTP calls; SSRF not applicable. |

**Legend:** ✅ Pass · ⚠️ Concern · ❌ Fail · ➖ N/A

**How scoring works:** Each applicable OWASP category can earn up to **2 points**:
- ✅ **Pass** = 2 points
- ⚠️ **Concern** = 1 point
- ❌ **Fail** = 0 points
- ➖ **N/A** = Not scored

**🧮 Overall Score:** 3 points earned out of 18 available = **17%**

---

## 🚨 Findings Requiring Action

> **Coverage:** 7 actionable findings are listed below. Pass and N/A categories are excluded.

### 💉 F-001 · SQL Injection in Email Search

**🚨 Critical Severity** · `A03`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** An attacker can bypass query logic to retrieve, modify, or delete sensitive payment disputes and customer data, exposing all PAN and dispute information.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/service/DisputeService.java` · Line 21
- **👁️ Issue:** Email parameter concatenated directly into SQL: `"select * from dispute where customer_email="+email+""`. No parameterized query or prepared statement used.
- **🛠️ Required Outcome:** Replace string concatenation with parameterized query using JdbcTemplate.queryForList() with ? placeholders.
- **✅ Complete When:** searchByEmail uses PreparedStatement or JdbcTemplate parameter binding and email parameter cannot alter SQL structure.

</details>

### 🔐 F-002 · Broken Access Control - No Authentication Required

**🚨 Critical Severity** · `A01`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Any unauthenticated user can access, search, and potentially modify any dispute record or upload malicious evidence files, enabling unauthorized dispute tampering and data theft.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/DisputeController.java` · Lines 15-22, `src/main/java/com/acme/dispute/controller/FileUploadController.java` · Line 9
- **👁️ Issue:** All endpoints lack any @PreAuthorize, @RolesAllowed, or Spring Security filter annotation. No authentication enforced; hardcoded credentials in AuthController are never validated against requests.
- **🛠️ Required Outcome:** Implement Spring Security with JWT or session authentication; require valid principal on all dispute endpoints; validate ownership of dispute records.
- **✅ Complete When:** Requests without valid authentication are rejected with 401; requests for unowned disputes are rejected with 403.

</details>

### 🪪 F-003 · Hardcoded Credentials in Source Code

**🚨 Critical Severity** · `A07`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Credentials hardcoded in the application allow any developer with code access to bypass authentication and impersonate administrators, compromising the entire dispute system.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/AuthController.java` · Line 10
- **👁️ Issue:** Username and password hardcoded as plain strings: `if("admin".equals(username) && "admin123".equals(password))`. Visible to source code review and Git history.
- **🛠️ Required Outcome:** Move credentials to environment variables or secure configuration; hash passwords; never commit secrets to version control.
- **✅ Complete When:** No plain-text credentials exist in source; authentication uses externalized, encrypted configuration.

</details>

### 🔏 F-004 · Unencrypted Payment Card Data Storage

**🔴 High Severity** · `A02`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Full primary account numbers (PANs) stored in plaintext in the database violate PCI DSS requirements and expose card data to insider threats and database compromise.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/resources/data.sql` · Lines 1-2, `src/main/java/com/acme/dispute/entity/Dispute.java` · Line 15
- **👁️ Issue:** Card numbers stored as plain `String` cardNumber field; seed data contains full test PANs (4111111111111111, 5555555555554444). No encryption at rest or in transit.
- **🛠️ Required Outcome:** Encrypt sensitive card fields with AES-256; use tokenization or masking; never store full PANs in development seed data.
- **✅ Complete When:** Card numbers encrypted at database layer; test data uses masked PAN format; decryption keys managed separately from code.

</details>

### 📂 F-005 · Path Traversal in File Upload

**🚨 Critical Severity** · `A08`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Attacker can upload files with path-traversal sequences (e.g., `../../`) to overwrite application files, inject malicious code, or access sensitive files, leading to remote code execution or data theft.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/FileUploadController.java` · Line 11
- **👁️ Issue:** File.transferTo() uses user-supplied filename directly: `new File("uploads/" + file.getOriginalFilename())`. No validation of filename; path-traversal characters not stripped.
- **🛠️ Required Outcome:** Validate and sanitize filename; reject ../, null bytes, or special characters; store with random UUID; enforce file-type restrictions.
- **✅ Complete When:** Filenames contain only alphanumeric, dash, underscore; uploads cannot traverse directories; file types restricted to approved list.

</details>

### ⚙️ F-006 · H2 Database Console Enabled in Production Configuration

**🟠 Medium Severity** · `A05`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** H2 console accessible on the network allows unauthenticated users to directly query, modify, or dump the entire database, exposing all payment and customer data.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/resources/application.yml` · Lines 2-4
- **👁️ Issue:** Spring H2 console enabled: `enabled: true`. No authentication guard; typically accessible at `/h2-console`.
- **🛠️ Required Outcome:** Disable H2 console in production profiles; restrict to development only via Spring profiles.
- **✅ Complete When:** application.yml disables console or uses conditional profile; application-prod.yml explicitly sets `enabled: false`.

</details>

### 📡 F-007 · Missing Security Logging and Monitoring

**🔴 High Severity** · `A09`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** No audit trail for authentication attempts, data access, or suspicious activity prevents detection of unauthorized access, data theft, or insider threats in the dispute system.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/` (all controllers), `src/main/java/com/acme/dispute/service/DisputeService.java`
- **👁️ Issue:** No security event logging for login attempts, data access, uploads, or search queries. No structured logging framework configured.
- **🛠️ Required Outcome:** Add security event logging: log authentication attempts (success/failure), data access, file uploads, and search queries with timestamp and user identity.
- **✅ Complete When:** Security events logged to structured format (JSON or similar); audit trails retained for investigation and compliance.

</details>

---

## 📝 Final Acknowledgement

> 🤖 This AI-assisted review is **informational and non-gating**. It does not replace penetration testing, SAST, DAST, software-composition analysis, secret scanning, PCI DSS assessment, or human security approval.

> **Risk Acceptance:** Proceeding without remediation requires a separate, documented, time-bound decision by the appropriate human risk owner. This report does not recommend or approve a waiver.

### 🚀 Recommended Next Step

Engage the development team immediately to patch SQL injection and access-control flaws before any integration testing or deployment activity.

