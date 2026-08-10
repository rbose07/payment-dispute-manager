# 🔬 OWASP Security Review

> **🧪 Report Type:** Detailed Technical Evidence

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

<details>
<summary><strong>🔬 Detailed Review Evidence</strong> · expand to view scope, origin analysis, and proof</summary>

---

# 🔬 Technical Evidence

> Supporting code evidence, affected files, origin analysis, and validation coverage.

## 🧾 Evidence Summary

- **Findings with technical proof:** 7
- **Files reviewed:** 9
- **Review method:** Static source code inspection and configuration analysis
- **Runtime validation:** Not performed (baseline repository scan)
- **Dependency analysis:** Gradle manifest inspected for known CVEs

## 🔍 Review Context

- **📦 Repository:** `payment-dispute-manager-gradle`
- **📝 Request:** Scan entire repository for security violations
- **🌿 Branch Comparison:** Full repository baseline (no PR/branch)
- **🔀 Effective Change Range:** Complete codebase inspection
- **📄 Reviewed Surface:** Controllers, services, entities, configuration, seed data, dependencies
- **🚪 Audit Trigger:** User-initiated full repository security scan

## 🧬 Origin Assessment

| Origin | Findings | Assessment |
|---|---:|---|
| 🆕 Introduced by This Change | **0** | Not a change audit; baseline repository assessment only. |
| 📈 Worsened by This Change | **0** | Not a change audit; baseline repository assessment only. |
| 🔦 Exposed or Exercised by This Change | **0** | Not a change audit; baseline repository assessment only. |
| 🏛️ Pre-existing Repository Baseline | **7** | All findings present in the checked-in codebase on develop branch. |

### Origin Rules Applied

- **Introduced:** absent on the base branch and added by the reviewed change.
- **Worsened:** present on the base branch, with increased reach, likelihood, affected data, or impact.
- **Exposed or exercised:** present on the base branch and newly reached by changed behavior.
- **Baseline:** present on the base branch, unchanged, and unrelated to the changed execution path.

## 🧾 Finding Evidence

### F-001 · Technical Proof

- **File:** `src/main/java/com/acme/dispute/service/DisputeService.java`
- **Symbol or Lines:** `searchByEmail()` method, line 20-23
- **Observed Behavior:** The method constructs a SQL query by concatenating user input directly: `String sql="select * from dispute where customer_email=+email+";`. The `email` parameter comes from an HTTP request parameter in `DisputeController.search()` (line 21) with no sanitization. JdbcTemplate.queryForList() executes this concatenated string, allowing an attacker to inject SQL operators, logical conditions, or subqueries.
- **Trust Boundary:** User-controlled HTTP request parameter `email` flows directly into SQL query construction without parameterization. The boundary is crossed at DisputeController line 21 (`@RequestParam String email`) and sinks at DisputeService line 22 (`jdbc.queryForList(sql)`).
- **Attack Scenario:** An attacker submits `GET /api/disputes/search?email=' OR '1'='1` to bypass the WHERE clause and retrieve all disputes regardless of email. Alternatively, submitting `email=' UNION SELECT * FROM dispute --` could exfiltrate all columns. An attacker could also craft queries to UPDATE or DELETE records if database permissions allow.
- **Suggested Direction:** Replace the string concatenation with parameterized query: `String sql="select * from dispute where customer_email=?"; return jdbc.queryForList(sql, email);` or use a named parameter approach with Spring Data. This ensures the email parameter cannot alter query structure.
- **Verification Approach:** Test with SQL injection payloads (single quote, OR clauses, UNION queries) and confirm they return error or no data; verify query structure by inspecting logs or profiler; write unit tests that attempt injection and confirm protection.

### F-002 · Technical Proof

- **File:** `src/main/java/com/acme/dispute/controller/DisputeController.java` and `src/main/java/com/acme/dispute/controller/FileUploadController.java`
- **Symbol or Lines:** `DisputeController` lines 15-22 (all endpoints), `FileUploadController` lines 9-13 (upload endpoint)
- **Observed Behavior:** The DisputeController defines two endpoints: GET `/api/disputes/{id}` and GET `/api/disputes/search`. Neither endpoint has any Spring Security annotation (@PreAuthorize, @PostAuthorize, @Secured, @RolesAllowed) or custom filter. FileUploadController POST `/api/evidence/upload` similarly lacks any access control. All three endpoints are publicly accessible; any HTTP client can invoke them without credentials.
- **Trust Boundary:** HTTP request boundary at controller method entry. No authentication filter intercepts requests; no principal is extracted from SecurityContext. The service and repository layers receive user requests without identity or role information.
- **Attack Scenario:** An attacker discovers the endpoints (e.g., via API documentation or probing). Without authentication, the attacker directly accesses `GET /api/disputes/1` to retrieve dispute records for customer Alice, then queries `GET /api/disputes/search?email=bob@test.com` to retrieve disputes for customer Bob, and finally uploads a malicious evidence file via POST `/api/evidence/upload?file=shell.jsp`. All actions succeed because no authentication is enforced. The attacker then modifies dispute outcomes or impersonates analysts.
- **Suggested Direction:** Integrate Spring Security (e.g., JWT-based authentication). Annotate endpoints with @PreAuthorize("isAuthenticated()") and add role checks like @PreAuthorize("hasRole('ANALYST')"). Implement an ownership validation layer so users can only access their own or assigned disputes. Add a principal-aware repository query that filters results by user tenant or role.
- **Verification Approach:** Deploy Spring Security and attempt unauthenticated requests; confirm 401 responses. Inject valid JWT and verify access. Attempt to access a dispute owned by another user and confirm 403 response. Test with multiple roles to ensure role-based endpoint access works.

### F-003 · Technical Proof

- **File:** `src/main/java/com/acme/dispute/controller/AuthController.java`
- **Symbol or Lines:** `login()` method, line 10
- **Observed Behavior:** The AuthController implements a login method that compares the request parameters directly against hardcoded strings: `if("admin".equals(username) && "admin123".equals(password))`. The credentials "admin" and "admin123" are visible in plain text in source code. The method is reachable at POST `/api/auth/login`. Credentials are also stored in Git history and are visible to anyone with source code access (developers, security reviewers, code repositories, archives).
- **Trust Boundary:** HTTP request parameters (username, password) are compared against hardcoded values. The boundary is not properly protected; the hardcoded secrets cross into version control and developer access logs.
- **Attack Scenario:** An attacker inspects the public GitHub repository or obtains a source-code tarball and discovers the hardcoded credentials "admin" / "admin123". The attacker calls POST `/api/auth/login?username=admin&password=admin123` and receives a "SUCCESS" response. Although this AuthController method is not enforced by Spring Security (because no security filter exists—see F-002), the attacker now knows the admin password and can impersonate an administrator in any system that does implement authentication. Additionally, any developer with source code access is exposed to the credentials, violating least-privilege principles.
- **Suggested Direction:** Remove hardcoded credentials. Externalze secrets to environment variables or a secure vault (e.g., Spring Cloud Config, AWS Secrets Manager, HashiCorp Vault). Load credentials at runtime from externalized configuration. Hash password comparisons using bcrypt or Argon2. Implement a proper authentication service that validates credentials against a secure store. Use Spring Security configuration to mandate JWT or OAuth 2.0 token-based authentication.
- **Verification Approach:** Inspect source code and confirm no plain-text credentials appear. Run `git log -p src/main/java/com/acme/dispute/controller/AuthController.java` and confirm credentials are not in Git history (or redact history). Verify Spring Security is configured to use externalized, hashed credentials. Test authentication with correct and incorrect credentials; confirm both scenarios are logged securely.

### F-004 · Technical Proof

- **File:** `src/main/java/com/acme/dispute/entity/Dispute.java` and `src/main/resources/data.sql`
- **Symbol or Lines:** `Dispute.java` line 15 (cardNumber field), `data.sql` lines 1-2 (seed data)
- **Observed Behavior:** The Dispute entity declares a `cardNumber` field as a plain String: `private String cardNumber;`. No encryption annotation or custom type is applied. Seed data in `data.sql` inserts full primary account numbers: `'4111111111111111'` and `'5555555555554444'`. H2 in-memory database stores these values in plaintext in memory. When data is queried (via F-001 SQL injection or standard queries), full card numbers are returned in the JSON response over HTTP. No encryption at rest (database) or in transit (HTTPS/TLS) is evident in configuration.
- **Trust Boundary:** Sensitive payment card data (PAN) crosses the HTTP boundary without encryption. Database stores PAN plaintext. Insider access to the database or network traffic sniffing exposes full card numbers. The data flows: HTTP client → Spring Boot → JdbcTemplate → H2 database, with no encryption at any layer.
- **Attack Scenario:** An attacker performs the SQL injection attack (F-001) to query `SELECT * FROM dispute` and receives a JSON response containing `"cardNumber": "4111111111111111"` for multiple disputes. The attacker now possesses full PANs for customers Alice and Bob. Alternatively, an insider with database access exports the entire dispute table and obtains all PANs in plaintext. An attacker eavesdropping on HTTP traffic (via man-in-the-middle attack or network tap) intercepts the full JSON response and captures PANs. All scenarios violate PCI DSS requirement 3.4 (encryption of stored PAN) and 4.1 (encryption of PANs in transit).
- **Suggested Direction:** Encrypt sensitive fields using JPA entity listeners (e.g., Jasypt or Spring Security Crypto) or database-level encryption (e.g., Transparent Data Encryption in PostgreSQL). Tokenize PANs via a third-party payment processor (e.g., Stripe, Square) and store only tokens in the database. Mask or redact PANs in API responses, showing only the last 4 digits. Enforce HTTPS/TLS on all endpoints. Remove full PANs from seed data; use masked test values like `****11111111`. Implement field-level encryption so the cardNumber field is decrypted only when explicitly needed.
- **Verification Approach:** Verify encryption keys are stored separately from code (e.g., in environment or a vault). Test that card numbers in the database are encrypted/tokenized, not plaintext. Verify API responses mask or omit full PANs. Configure and enforce HTTPS/TLS. Test decryption is available only to authorized code paths. Review seed data and confirm no full PANs are present.

### F-005 · Technical Proof

- **File:** `src/main/java/com/acme/dispute/controller/FileUploadController.java`
- **Symbol or Lines:** `upload()` method, lines 10-11
- **Observed Behavior:** The upload method accepts a MultipartFile and saves it using: `file.transferTo(new File("uploads/" + file.getOriginalFilename()));`. The `getOriginalFilename()` is the filename provided by the HTTP client in the multipart form data, with no validation or sanitization. An attacker can submit a filename containing path-traversal sequences (e.g., `../../conf/application.yml` or `..\..\..\windows\system32\driver\etc\hosts`). The File constructor will resolve the traversal and write to an arbitrary location on the filesystem. Additionally, there is no file-type restriction; an attacker can upload a `.jsp` or `.sh` file, and if the `uploads/` directory is web-accessible, execute arbitrary code.
- **Trust Boundary:** User-supplied filename crosses the HTTP multipart boundary directly into filesystem path construction. The boundary is at `FileUploadController.upload()` line 10 (MultipartFile parameter) and sinks at line 11 (`new File("uploads/" + file.getOriginalFilename())`). No filesystem access control or input validation exists between the two.
- **Attack Scenario:** An attacker crafts a multipart POST request with filename `../../gradlew.bat`. The upload method resolves the path to the project root and overwrites the Gradle wrapper. Or the attacker submits filename `shell.jsp` and file content containing JSP code. When the attacker accesses `/uploads/shell.jsp` via HTTP, the web server executes the JSP and the attacker gains remote code execution. Alternatively, the attacker traverses to a configuration directory and overwrites `application.yml` to inject malicious configuration (e.g., enable an actuator endpoint or inject a default admin user).
- **Suggested Direction:** Validate the filename before use: reject any filename containing `.`, `..`, `/`, `\`, null bytes, or other special characters. Generate a new UUID-based filename and preserve only the extension if needed (after validating it against a whitelist of allowed extensions). Store the file in a directory outside the web root. Implement file-type validation by inspecting the file's magic bytes (MIME type detection), not just the filename extension. Log all file uploads with the user, timestamp, and sanitized filename for audit purposes.
- **Verification Approach:** Attempt to upload files with path-traversal sequences (`../../`, `..\\`) and confirm they are rejected or stored with sanitized names. Verify that files are stored in a non-web-accessible directory or behind authentication. Test that uploading a `.jsp`, `.sh`, or `.bat` file is rejected. Inspect server logs to confirm all uploads are audited with user identity and filename.

### F-006 · Technical Proof

- **File:** `src/main/resources/application.yml`
- **Symbol or Lines:** Lines 2-4
- **Observed Behavior:** The Spring Boot application configuration includes H2 console configuration with `enabled: true`. H2 is an in-memory relational database often used for development and testing. The H2 console is a web UI that provides SQL query execution and database browsing capabilities. With `enabled: true`, Spring Boot auto-configures and exposes the H2 console at the default path `/h2-console`. The application listens on `http://localhost:8080` (default), making the console reachable at `http://localhost:8080/h2-console`. No authentication is enforced; any HTTP client can access it without credentials.
- **Trust Boundary:** HTTP request boundary at `/h2-console` endpoint. No Spring Security filter protects this endpoint; no authentication is checked. The H2 console connects directly to the datasource (H2 in-memory database) with full administrative privileges.
- **Attack Scenario:** An attacker discovers the application is running and accesses `http://target:8080/h2-console` in a browser. The H2 console interface loads without requiring login. The attacker uses the SQL query editor to execute `SELECT * FROM dispute` and retrieves all dispute records, including card numbers and customer emails. The attacker then executes `UPDATE dispute SET assigned_analyst='attacker' WHERE id=1` to tamper with dispute assignments. Finally, the attacker executes `DROP TABLE dispute` to destroy the database. All attacks succeed because the H2 console provides direct, unauthenticated access to the database with CRUD and administrative privileges.
- **Suggested Direction:** Disable the H2 console in all profiles except development. Use Spring profiles to conditionally enable/disable the console: set `enabled: true` only in `application-dev.yml` and `enabled: false` (or omit) in `application-prod.yml` and `application-staging.yml`. Restrict H2 console access via Spring Security, requiring authentication and authorization (e.g., `@PreAuthorize("hasRole('ADMIN')")`). In production, use a managed database (e.g., PostgreSQL, MySQL) instead of H2 and do not expose console access.
- **Verification Approach:** Start the application and confirm H2 console is not accessible at `/h2-console` (or returns 401/403). Verify application-prod.yml explicitly disables H2 console. Test that a development environment with `application-dev.yml` can access the console only after authentication. Inspect build and deployment pipelines to confirm production deployments use the production profile.

### F-007 · Technical Proof

- **File:** All controller and service files: `src/main/java/com/acme/dispute/controller/AuthController.java`, `src/main/java/com/acme/dispute/controller/DisputeController.java`, `src/main/java/com/acme/dispute/controller/FileUploadController.java`, `src/main/java/com/acme/dispute/service/DisputeService.java`
- **Symbol or Lines:** All public methods lack logging statements; no log framework imports; no SLF4J or Log4j configuration in build.gradle or application.yml
- **Observed Behavior:** The application performs security-sensitive operations (login, data access, file upload, database queries) without generating any log events. The AuthController login method returns "SUCCESS" or "FAILED" but does not log authentication attempts with timestamp, source IP, or username. DisputeController endpoints do not log who accessed which disputes or when. FileUploadController does not log file upload details. DisputeService.searchByEmail() does not log query parameters or results. No logging configuration is present in `build.gradle` (no logback dependency visible explicitly; Spring Boot includes logback by default, but the application does not use it). No `logback-spring.xml` or `log4j.properties` configuration file exists in `src/main/resources`.
- **Trust Boundary:** Security event boundary at all controller entry points and service methods that touch sensitive data. Events that should be logged (authentication, authorization, data access, modification, errors) are not captured or retained. An attacker's actions leave no audit trail.
- **Attack Scenario:** An attacker exploits F-002 (no authentication) and F-001 (SQL injection) to query all disputes. The attacker retrieves sensitive data, modifies records, and uploads malicious files. Because no logging is performed, the security team has no audit trail to detect the breach, determine the scope of data accessed, identify the attacker's IP address, or reconstruct the timeline of compromise. Incident response is delayed or impossible. Regulatory compliance audits (PCI DSS, SOX) cannot be satisfied because no evidence of access controls or monitoring exists.
- **Suggested Direction:** Add structured logging to all security-sensitive operations. Log authentication attempts (success/failure) with timestamp, username, source IP, and user-agent. Log all data access with user identity, query/endpoint, and result count. Log file uploads with user, filename (sanitized), file size, and result. Log authorization failures (403 responses) with user, resource, and attempted action. Use a structured logging format (e.g., JSON or key=value) for easy parsing. Configure log retention and centralized log collection (e.g., ELK stack, Splunk, CloudWatch). Implement log rotation to prevent disk space exhaustion.
- **Verification Approach:** Start the application and perform authentication, data access, and file upload operations. Inspect application logs (e.g., in `tail -f` if logging to stdout) and confirm security events are recorded with sufficient detail. Test that both successful and failed operations are logged. Verify logs cannot be modified or deleted by the application user. Verify log collection system (if used) receives all events. Audit log retention to confirm compliance with policy (e.g., 90-day retention for payment disputes).

## 📚 Files Reviewed

1. `src/main/java/com/acme/dispute/PaymentDisputeManagerApplication.java` · 12 lines
2. `src/main/java/com/acme/dispute/controller/AuthController.java` · 15 lines
3. `src/main/java/com/acme/dispute/controller/DisputeController.java` · 25 lines
4. `src/main/java/com/acme/dispute/controller/FileUploadController.java` · 15 lines
5. `src/main/java/com/acme/dispute/service/DisputeService.java` · 25 lines
6. `src/main/java/com/acme/dispute/entity/Dispute.java` · 19 lines
7. `src/main/java/com/acme/dispute/repository/DisputeRepository.java` · 6 lines
8. `src/main/resources/application.yml` · 10 lines
9. `src/main/resources/data.sql` · 2 lines
10. `build.gradle` · 23 lines

**Total Lines Reviewed:** 152 lines of production code, configuration, and seed data

## 🧪 Evidence Quality

- **Source Review:** ✅ Complete static analysis of all Java controllers, services, entities, repositories, and configuration files. No obfuscation or generated code.
- **Configuration Review:** ✅ Application.yml, build.gradle, and data.sql inspected for security settings, dependencies, and initialization data.
- **Test Evidence:** ❌ No test code present in workspace; automated test coverage not evaluated.
- **Runtime Validation:** ❌ Application not executed; findings based on static code inspection and design-time analysis.
- **Dependency Evidence:** ⚠️ Gradle dependencies inspected; Spring Boot 3.3.5 and related libraries appear current; no obvious outdated or known-vulnerable versions detected via manifest review. Full CVE scan not performed.

## ⚠️ Assumptions and Limitations

- **No Runtime Execution:** This assessment is based on static code review. Runtime validation (e.g., attempting SQL injection, testing authentication bypass, accessing H2 console) was not performed. Dynamic findings (e.g., race conditions, timing attacks, resource exhaustion) cannot be assessed without runtime instrumentation.
- **No Penetration Testing:** The audit does not simulate real attacker behavior comprehensively or test defense-in-depth mechanisms. Manual security testing and penetration testing by qualified professionals are still necessary.
- **CVSS Scoring Not Applied:** Severity ratings (Critical, High, Medium) are qualitative assessments based on business impact in the payment-dispute domain. Official CVSS scores require a formal methodology and are not included here.
- **Incomplete Dependency Analysis:** The build.gradle manifest was inspected, but a full software-composition analysis (SCA) with CVE lookups was not performed. Spring Boot 3.3.5 is recent and likely free of critical issues, but no automated scan result is included. An official SCA tool (e.g., OWASP Dependency-Check, Snyk) should be used for production deployments.
- **Test Coverage Unknown:** No unit or integration tests were found in the workspace directory. Code coverage metrics are unavailable. Security assumptions (e.g., boundary validation) are inferred from code inspection, not validated by test evidence.
- **Database Permissions Assumed:** Findings assume standard Spring Data and JdbcTemplate permissions. If the database user has restricted privileges (e.g., no DELETE), the impact of F-001 SQL injection is reduced but not eliminated.
- **File System Access Assumed:** F-005 assumes the `uploads/` directory is readable by the application and potentially web-accessible. If the directory is behind authentication or outside the web root, the risk is reduced but the path-traversal vulnerability remains.
- **Spring Security Not Integrated:** The audit assumes Spring Security is not configured beyond its default no-op state. If custom Spring Security beans are defined elsewhere or in a separate configuration repository, F-002 assessment may be incomplete. No Spring Security configuration class was found in the reviewed files.

</details>

---

## 📝 Final Acknowledgement

> 🤖 This AI-assisted review is **informational and non-gating**. It does not replace penetration testing, SAST, DAST, software-composition analysis, secret scanning, PCI DSS assessment, or human security approval.

> **Risk Acceptance:** Proceeding without remediation requires a separate, documented, time-bound decision by the appropriate human risk owner. This report does not recommend or approve a waiver.

### 🚀 Recommended Next Step

Engage the development team immediately to patch SQL injection and access-control flaws before any integration testing or deployment activity.

