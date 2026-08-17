# 🛡️ OWASP Security Review

> **📊 Report Type:** Security Assessment Summary

## 💳 Repository Security Baseline Assessment

> ### ❌ Final Decision: **FAIL**
>
> **📊 Security Score:** 16.67% · **🚦 Overall Posture:** Critical Risk  
> **🚨 Highest Finding:** SQL Injection, Plaintext PANs, Broken Access Control  
> **🌿 Branch:** `develop` · **Compared with:** `Repository baseline` · **📅 Reviewed:** August 11, 2026

---

## 📌 At a Glance

| 🚨 Critical | 🔴 High | 🟠 Medium | 🔵 Low | ✅ Passed | ➖ N/A |
|---:|---:|---:|---:|---:|---:|
| **3** | **5** | **0** | **0** | **1** | **1** |

### 💼 Report Summary

This payment-dispute manager application contains intentional security vulnerabilities for training purposes. The codebase lacks fundamental access controls, authentication mechanisms, and secure data handling practices. All user input flows directly to database queries without sanitization. Sensitive payment card data is stored in plaintext. The application is not production-ready without comprehensive security remediation.

### 🎯 Top Actions

1. **💉 SQL Injection Risk** · 🚨 Critical  
   Parameterize all database queries immediately. Current email search uses string concatenation.

2. **🔐 Broken Access Control** · 🚨 Critical  
   Implement authentication and authorization. All endpoints are accessible without identity verification.

3. **🔏 Plaintext Payment Data** · 🚨 Critical  
   Stop storing full PAN in plaintext. Implement field-level encryption or tokenization for payment data.

---

## 🧭 OWASP Top 10 Scorecard

| ID | Security Area | Result | Key Message |
|:--:|---|:--:|---|
| 🔐 A01 | Broken Access Control | ❌ | No authorization checks on any endpoint. Direct resource access. |
| 🔏 A02 | Cryptographic Failures | ❌ | Plaintext PANs, hardcoded credentials in code. |
| 💉 A03 | Injection | ❌ | SQL injection in email search endpoint via string concatenation. |
| 🏗️ A04 | Insecure Design | ❌ | No authentication layer, no ownership verification logic. |
| ⚙️ A05 | Security Misconfiguration | ❌ | H2 console enabled, no security headers or CSRF protection. |
| 📦 A06 | Vulnerable and Outdated Components | ✅ | Spring Boot 3.3.5 and dependencies are current with no known CVEs. |
| 🪪 A07 | Identification and Authentication Failures | ❌ | Hardcoded credentials in login endpoint, no real authentication. |
| 🔗 A08 | Software and Data Integrity Failures | ⚠️ | File upload uses original filename without sanitization. Path traversal possible. |
| 📡 A09 | Security Logging and Monitoring Failures | ❌ | No audit logging, no authentication attempt logging. |
| 🌐 A10 | Server-Side Request Forgery | ➖ | No outbound HTTP requests in codebase. Not applicable. |

**Legend:** ✅ Pass · ⚠️ Concern · ❌ Fail · ➖ N/A

**How scoring works:** Each applicable OWASP category can earn up to **2 points**:
- ✅ **Pass** = 2 points
- ⚠️ **Concern** = 1 point
- ❌ **Fail** = 0 points
- ➖ **N/A** = Not scored

**🧮 Overall Score:** 3 points earned out of 18 available = **16.67%**

---

## 🚨 Findings Requiring Action

> **Coverage:** 9 actionable findings are listed below. Pass and N/A categories are excluded.

### 🚨 FND-001 · SQL Injection in Email Search

**🚨 Critical Severity** · `A03 Injection`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Attackers can extract, modify, or delete all dispute records and customer payment data from the database without authentication.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/service/DisputeService.java` · Lines 20-22
- **👁️ Issue:** Query constructed via string concatenation: `"select * from dispute where customer_email=+email+"`. Email parameter flows directly into SQL.
- **🛠️ Required Outcome:** Use parameterized queries with `?` placeholders or named parameters. Example: `jdbc.queryForList("SELECT * FROM dispute WHERE customer_email = ?", email)`
- **✅ Complete When:** Verify all database queries use parameterized statements and automated SAST tool confirms no SQL injection patterns.

</details>

### 🚨 FND-002 · Broken Access Control - Unauthenticated Dispute Access

**🚨 Critical Severity** · `A01 Broken Access Control`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Any unauthenticated user can retrieve any dispute by ID via GET `/api/disputes/{id}`, exposing all customer names, emails, transaction IDs, and payment data.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/DisputeController.java` · Lines 15-18
- **👁️ Issue:** No `@PreAuthorize`, no authentication check, no role verification. Endpoint accessible to anyone.
- **🛠️ Required Outcome:** Add Spring Security authentication via `@PreAuthorize("isAuthenticated()")` and verify caller owns the dispute or has analyst role.
- **✅ Complete When:** Unauthorized requests return 401/403. Authorized requests validate dispute ownership against authenticated user.

</details>

### 🚨 FND-003 · Hardcoded Credentials in Authentication Endpoint

**🚨 Critical Severity** · `A02 Cryptographic Failures`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Credentials are visible in source code, Git history, build artifacts, and compiled JARs. Single hardcoded account compromises all authentication.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/AuthController.java` · Lines 10-11
- **👁️ Issue:** Credentials `admin` / `admin123` hardcoded in login method: `if("admin".equals(username) && "admin123".equals(password))`.
- **🛠️ Required Outcome:** Remove hardcoded checks. Use Spring Security with bcrypt-hashed password storage in database or environment variables.
- **✅ Complete When:** Credentials loaded from external configuration. Password comparison uses bcrypt. Source code contains no plaintext secrets.

</details>

### 🚨 FND-004 · Plaintext Payment Card Numbers in Database

**🚨 Critical Severity** · `A02 Cryptographic Failures`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Full 16-digit PAN stored unencrypted in H2 database. Breach exposes payment card data for fraud and chargebacks, violating PCI DSS requirements.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/resources/data.sql` · Lines 1-2 and `src/main/java/com/acme/dispute/entity/Dispute.java` · Line 15
- **👁️ Issue:** Dispute entity stores `cardNumber` as plain string. Seed data contains full PANs: `4111111111111111`, `5555555555554444`.
- **🛠️ Required Outcome:** Implement field-level encryption via JPA `@Convert` annotation or tokenize PANs with payment processor. Store only masked PAN (last 4 digits).
- **✅ Complete When:** Card number field is encrypted at rest. Unit tests verify encryption/decryption. Full PAN never logged or exposed in responses.

</details>

### 🔴 FND-005 · H2 Console Enabled in Production Configuration

**🔴 High Severity** · `A05 Security Misconfiguration`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** H2 console may expose database access if deployed in public environment. Attackers gain direct SQL access to all dispute and customer data.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/resources/application.yml` · Lines 2-4
- **👁️ Issue:** `spring.h2.console.enabled: true` enables web-based database UI at `/h2-console` with no authentication check.
- **🛠️ Required Outcome:** Disable H2 console in production. Use separate profiles: `application-dev.yml` with console enabled, `application-prod.yml` with it disabled.
- **✅ Complete When:** Console disabled by default. Access to console requires Spring Security authentication and analyst role. Verified in test environment.

</details>

### 🔴 FND-006 · No Authentication Mechanism

**🔴 High Severity** · `A07 Identification and Authentication Failures`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Application has no real authentication or session management. Login endpoint does not issue tokens or sessions. All traffic is anonymous.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/AuthController.java` · Lines 8-13
- **👁️ Issue:** Login returns "SUCCESS" or "FAILED" string; does not issue JWT, session cookie, or bearer token. No SecurityContext established.
- **🛠️ Required Outcome:** Implement Spring Security with JWT or session-based authentication. Issue stateless tokens or secure session cookies on login.
- **✅ Complete When:** Successful login returns JWT token. Subsequent requests include token in Authorization header. Invalid tokens rejected with 401.

</details>

### 🔴 FND-007 · No Authorization Checks Between Analysts

**🔴 High Severity** · `A01 Broken Access Control`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Analysts can access disputes assigned to other analysts. No ownership verification. One compromised account exposes all disputes.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/DisputeController.java` · Lines 15-18 and `src/main/java/com/acme/dispute/entity/Dispute.java` · Line 17
- **👁️ Issue:** Entity has `assignedAnalyst` field but controller never checks if caller matches. Access is fully open.
- **🛠️ Required Outcome:** Validate that authenticated analyst matches `dispute.assignedAnalyst` before returning dispute details or allowing updates.
- **✅ Complete When:** Unauthorized analyst gets 403 Forbidden. Authorized analyst sees only own disputes. Unit tests verify access control.

</details>

### 🔴 FND-008 · File Upload Path Traversal

**🔴 High Severity** · `A08 Software and Data Integrity Failures`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Attacker can upload file with path like `../../config/application.yml` to overwrite application configuration or escape to arbitrary filesystem locations.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/java/com/acme/dispute/controller/FileUploadController.java` · Lines 10-11
- **👁️ Issue:** `file.transferTo(new File("uploads/" + file.getOriginalFilename()))` uses caller-controlled filename directly. No sanitization.
- **🛠️ Required Outcome:** Generate UUID-based filename. Reject filenames containing `../`, null bytes, or path separators. Validate file type and size.
- **✅ Complete When:** Filenames sanitized and UUID-renamed. Path traversal attempts rejected. File type whitelist enforced.

</details>

### 🔴 FND-009 · No Security Headers or CORS Protection

**🔴 High Severity** · `A05 Security Misconfiguration`  
**🆕 Origin:** Pre-existing repository baseline

> **💼 Business Impact:** Missing CSRF tokens allow malicious websites to force authenticated users to perform unwanted actions. No XSS protection headers.

<details>
<summary><strong>🧑‍💻 Developer Details</strong></summary>

- **📍 Location:** `src/main/resources/application.yml` · entire file and controllers
- **👁️ Issue:** No Spring Security configuration, no `@EnableWebSecurity`, no header configuration. CORS allows all origins by default.
- **🛠️ Required Outcome:** Create SecurityConfig class with CSRF protection, CORS restriction to trusted origins, and security headers (X-Content-Type-Options, X-Frame-Options, etc.).
- **✅ Complete When:** CSRF tokens required for POST/PUT/DELETE. CORS restricted to localhost/trusted domains. Security headers present in responses.

</details>

---

## 📝 Final Acknowledgement

> 🤖 This AI-assisted review is **informational and non-gating**. It does not replace penetration testing, SAST, DAST, software-composition analysis, secret scanning, PCI DSS assessment, or human security approval.

> **Risk Acceptance:** Proceeding without remediation requires a separate, documented, time-bound decision by the appropriate human risk owner. This report does not recommend or approve a waiver.

### 🚀 Recommended Next Step

Prioritize SQL injection and authentication implementation. This codebase is a training application and should not be deployed to production without full remediation of all Fail findings.

