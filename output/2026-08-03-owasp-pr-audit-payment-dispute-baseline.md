# OWASP PR Audit — payment-dispute-baseline

Date: 2026-08-03
Base branch: `develop`
Change summary: Establish the initial OWASP Top 10 security baseline for the deliberately vulnerable Payment Dispute Manager application.
Report slug: `payment-dispute-baseline`

=================================

Executive snapshot

- Decision: ❌ FAIL / WAIVER RECOMMENDED — full baseline audit performed.
- Reason: Multiple high-impact, pre-existing vulnerabilities were found in controllers, query construction, authentication, sensitive-data handling, and configuration that materially affect payment-dispute data confidentiality and integrity.
- Final score: 14% (2 / 14)
- Disposition: FAIL / WAIVER RECOMMENDED

Top 3 prioritized findings

1. F-01 — A03 Injection — SQL injection via concatenated query (Critical, High confidence)
2. F-02 — A01 Broken Access Control — Unprotected endpoints exposing disputes (High, High confidence)
3. F-03 — A02 Cryptographic Failures — Full PAN stored in plaintext in DB/seed data (High, High confidence)

Severity summary (compact)

- Critical: 1
- High: 3
- Medium: 0
- Low: 0
- Informational: 0

Ten-row OWASP scorecard (A01..A10)

| Category | Result | Points |
|---|---:|---:|
| A01 Broken Access Control | ❌ Fail | 0 |
| A02 Cryptographic Failures | ❌ Fail | 0 |
| A03 Injection | ❌ Fail | 0 |
| A04 Insecure Design | ⚠️ Concern | 1 |
| A05 Security Misconfiguration | ❌ Fail | 0 |
| A06 Vulnerable and Outdated Components | ➖ N/A (see rationale) | (excluded) |
| A07 Identification and Authentication Failures | ❌ Fail | 0 |
| A08 Software and Data Integrity Failures | ➖ N/A (no evidence) | (excluded) |
| A09 Security Logging and Monitoring Failures | ⚠️ Concern | 1 |
| A10 Server-Side Request Forgery | ➖ N/A (no outbound call surface) | (excluded) |

Applicable categories: 7. Earned points = 2. Possible points = 14. Score = 2/14 = 14%.

Disposition rules (per AGENTS.md): score < 60% and multiple Fails -> FAIL / WAIVER RECOMMENDED.

Detailed findings

F-01 — A03 Injection
- Severity: Critical
- Confidence: High
- Origin: Pre-existing repository baseline
- Status: OPEN
- Affected file(s) and evidence:
  - `src/main/java/com/acme/dispute/service/DisputeService.java` lines 20-22:

```java
20 | 	public List<Map<String,Object>> searchByEmail(String email){
21 | 		String sql="select * from dispute where customer_email=+email+";
22 | 		return jdbc.queryForList(sql);
```

- Why this is a problem / attack scenario:
  - The service constructs SQL by concatenating an untrusted `email` parameter into a query string and passes it to `JdbcTemplate.queryForList`. An attacker can supply crafted input (for example: `x' OR '1'='1`) to control SQL semantics and exfiltrate or modify dispute data.

- Payment-dispute business impact:
  - Complete disclosure of dispute records (including PANs in the seeded DB), ability to modify or delete records, and arbitrary data exposure across customers.

- Recommended action:
  1. Stop building SQL via string concatenation. Use parameterized queries or Spring Data JPA repository methods.
  2. Example fix: `jdbc.queryForList("select * from dispute where customer_email = ?", email);` or implement `List<Dispute> findByCustomerEmail(String email)` in `DisputeRepository`.

- Verification criteria:
  - Automated tests asserting that special characters in `email` do not alter query semantics.
  - Run a simple runtime test: call `/api/disputes/search?email=' OR '1'='1` and verify only expected results (or none) are returned after the fix.

F-02 — A01 Broken Access Control
- Severity: High
- Confidence: High
- Origin: Pre-existing repository baseline
- Status: OPEN
- Affected file(s) and evidence:
  - `src/main/java/com/acme/dispute/controller/DisputeController.java` lines 15-23:

```java
15 | 	@GetMapping("/{id}")
16 | 	public Dispute get(@PathVariable Long id){
17 | 		return service.find(id).orElseThrow();
18 | 	}
19 |
20 | 	@GetMapping("/search")
21 | 	public List<Map<String,Object>> search(@RequestParam String email){
22 | 		return service.searchByEmail(email);
23 | 	}
```

- Why this is a problem / attack scenario:
  - No authentication or authorization checks are applied to endpoints that return dispute data. Any caller (anonymous) can enumerate or fetch disputes by id or email. Combined with injection and stored PANs, this enables wide data leakage.

- Payment-dispute business impact:
  - Unauthorized disclosure of dispute details and cardholder data; privacy/regulatory exposure (PCI/DPA); business and reputational damage.

- Recommended action:
  1. Introduce authentication and per-resource authorization checks. Use Spring Security to protect `/api/**` and require roles (e.g., `ANALYST`, `ADMIN`).
  2. Enforce ownership/tenant checks: only assigned analyst or authorized roles can access a dispute record.

- Verification criteria:
  - Attempt unauthorized requests to endpoints (without auth) and confirm they return 401/403.
  - Confirm authorized users with a role can access allowed resources and not others.

F-03 — A02 Cryptographic Failures (Sensitive data handling)
- Severity: High
- Confidence: High
- Origin: Pre-existing repository baseline
- Status: OPEN
- Affected file(s) and evidence:
  - `src/main/java/com/acme/dispute/entity/Dispute.java` lines 14-16 define `cardNumber` stored as a String:

```java
14 | 	private String cardNumber;
15 | 	private String customerEmail;
16 | 	private String assignedAnalyst;
```

  - `src/main/resources/data.sql` lines 1-2 seed the DB with full PANs in cleartext:

```sql
1 | insert into dispute(id,transaction_id,card_number,customer_email,assigned_analyst) values (1,'TX1001','4111111111111111','alice@test.com','analystA');
2 | insert into dispute(id,transaction_id,card_number,customer_email,assigned_analyst) values (2,'TX1002','5555555555554444','bob@test.com','analystB');
```

- Why this is a problem / attack scenario:
  - Full PANs are stored in application-controlled storage in plaintext without encryption, tokenization, or access controls. If the database is compromised (or via injection/exposed endpoints) full PANs are immediately available.

- Payment-dispute business impact:
  - Direct PCI-PII exposure; regulatory fines; mandatory breach notifications.

- Recommended action:
  1. Avoid storing full PAN in application DB. Replace with tokenization, truncated PAN (e.g., last 4 digits), or encrypt-at-rest with proper key management.
  2. Remove full PANs from seed data; use synthetic masked values for testing.

- Verification criteria:
  - Seed data contains only masked PANs (e.g., `************1111`).
  - Stored values in runtime DB are not full PANs; keys and secrets for encryption are not in source.

F-04 — A07 Identification and Authentication Failures
- Severity: High
- Confidence: High
- Origin: Pre-existing repository baseline
- Status: OPEN
- Affected file(s) and evidence:
  - `src/main/java/com/acme/dispute/controller/AuthController.java` lines 8-12:

```java
8 | 	@PostMapping("/login")
9 | 	public String login(@RequestParam String username, @RequestParam String password){
10 | 		if("admin".equals(username) && "admin123".equals(password))
11 | 			return "SUCCESS";
12 | 		return "FAILED";
```

- Why this is a problem / attack scenario:
  - Hard-coded credentials and plaintext password comparison provide no real authentication. No account management, password hashing, MFA, or session management is present. This trivializes attacker access.

- Payment-dispute business impact:
  - Anyone who learns or guesses the credentials gains privileged access, enabling data theft or modification.

- Recommended action:
  1. Replace the ad-hoc login with a proper authentication framework (e.g., Spring Security + persistent users, hashed passwords, session/JWT management).
  2. Remove hard-coded secrets from source.

- Verification criteria:
  - Credentials are stored hashed and salted (e.g., bcrypt) and not present in source.
  - Authentication flows return appropriate status codes (401 / 403) and tokens/sessions are validated.

F-05 — A05 Security Misconfiguration
- Severity: High
- Confidence: High
- Origin: Pre-existing repository baseline
- Status: OPEN
- Affected file(s) and evidence:
  - `src/main/resources/application.yml` lines 1-9:

```yaml
1 | spring:
2 |   h2:
3 |     console:
4 |       enabled: true
5 |   datasource:
6 |     url: jdbc:h2:mem:testdb
7 |   jpa:
8 |     hibernate:
9 |       ddl-auto: create
```

- Why this is a problem / attack scenario:
  - H2 console is enabled (database UI) and `ddl-auto: create` will auto-create schemas. If left enabled in non-dev environments or accessible, an attacker can modify DB directly. Default in-memory DB with seeded PANs increases exposure risk during development.

- Payment-dispute business impact:
  - Direct DB access and modification via console; accidental data loss or exposure.

- Recommended action:
  1. Disable H2 console except for secured development environments; remove it from production profiles.
  2. Disable `ddl-auto: create` in non-dev environments; prefer explicit migrations (Flyway/Liquibase).

- Verification criteria:
  - H2 console is not reachable in production; `ddl-auto` set to `none` or controlled per-profile.

F-06 — A04 Insecure Design (application-level)
- Severity: Medium
- Confidence: Medium
- Origin: Pre-existing repository baseline
- Status: OPEN
- Evidence & rationale:
  - File upload handling in `src/main/java/com/acme/dispute/controller/FileUploadController.java` lines 9-11 writes files to `uploads/` using the original filename without sanitization:

```java
9 | 	@PostMapping("/api/evidence/upload")
10 | 	public String upload(@RequestParam MultipartFile file) throws Exception {
11 | 		file.transferTo(new File("uploads/" + file.getOriginalFilename()));
```

  - This design allows path traversal (if originalFilename contains `../`) and stores files under application-controlled directories without validation.

- Business impact:
  - Uploads could overwrite files or store malicious content; combined with lack of access controls this increases risk.

- Recommended action:
  - Validate and sanitize filenames; generate server-side unique filenames; restrict upload directories and enforce content-type / size checks.

- Verification criteria:
  - Filenames are sanitized; no `..` segments; content-type checks enforced; uploaded files cannot overwrite application files.

F-07 — A09 Security Logging and Monitoring Failures
- Severity: Medium
- Confidence: Medium
- Origin: Pre-existing repository baseline
- Status: OPEN
- Evidence & rationale:
  - Search across controllers and service shows no audit logging of authentication attempts, admin actions, or data access. Example: `DisputeController.java` and `AuthController.java` contain no logger statements.

- Business impact:
  - Lack of audit trails delays detection and response to data exfiltration or abuse.

- Recommended action:
  - Add structured security/audit logging for authentication, authorization failures, administrative actions, and file uploads. Integrate with an external log aggregator and alerting.

- Verification criteria:
  - Logs exist with timestamps, user identifiers, action types, and resource IDs; test alerts for repeated failures.

F-08 — A06 Vulnerable and Outdated Components
- Status: N/A
- Rationale:
  - The codebase uses Spring Boot `3.3.5` and common dependencies (`spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `h2`), visible in `build.gradle`. Per AGENTS.md, do not assert CVEs or outdatedness without a dependency scan. A CVE-scanning step (SCA) is required to evaluate A06; therefore this category is excluded from the score for this baseline.

F-09 — A08 Software and Data Integrity Failures
- Status: N/A
- Rationale:
  - The repository contains no evidence of deserialization sinks, third-party plugin update checks, or signed update checks relevant to the application code paths reviewed. No evidence was found to evaluate this category; excluded with rationale.

F-10 — A10 Server-Side Request Forgery
- Status: N/A
- Rationale:
  - No outbound HTTP call construction or caller-controlled destination was found in the application code. The file upload, DB, and controllers do not perform HTTP requests based on user input. Marked N/A.

Developer action plan (prioritized)

1. Fix the SQL injection (F-01) by replacing concatenated SQL with parameterized queries or JPA repository methods.
2. Add authentication/authorization (F-02, F-04) using Spring Security; implement role-based access and ownership checks for dispute resources.
3. Remove full PANs from storage and seed data; implement tokenization or encryption (F-03).
4. Disable H2 console in production and remove `ddl-auto: create` from non-dev profiles (F-05).
5. Harden file upload handling (F-06): sanitize filenames, restrict directories, validate content.
6. Add security/audit logging and monitoring (F-07).
7. Run an SCA (software composition analysis) scan and dependency CVE check to evaluate A06.

Evidence appendix

- Git state & effective range used for this baseline:
  - Base branch: `develop` (current branch checked out). Working tree: clean. This is an application baseline audit of the checked-in code.

- Files referenced (repository-relative):
  - `src/main/java/com/acme/dispute/service/DisputeService.java` (sql concatenation)
  - `src/main/java/com/acme/dispute/controller/DisputeController.java` (unprotected endpoints)
  - `src/main/java/com/acme/dispute/controller/AuthController.java` (hard-coded credentials)
  - `src/main/java/com/acme/dispute/entity/Dispute.java` (cardNumber field)
  - `src/main/resources/data.sql` (seeded full PANs)
  - `src/main/resources/application.yml` (H2 console enabled, ddl-auto:create)
  - `src/main/java/com/acme/dispute/controller/FileUploadController.java` (unsafe upload handling)

Acknowledgements & next steps

- This is a non-gating, report-only audit of the repository baseline (initial seed). I did not modify source code or tests.
- To continue: implement the recommended fixes in a feature branch and re-run `/final-pr-readiness` to produce a PR-scoped audit. Additionally run automated SCA and secret scans as part of CI to evaluate A06.

Report generated by repository OWASP PR Auditor instructions.

