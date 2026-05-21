## 🔐 Security Checklist

### 🧠 STRIDE Analysis

- [ ] **Spoofing considered**  
  _Could someone pretend to be another user or system?_  
  Examples:
  - Missing authentication on API endpoints
  - Weak JWT/session handling
  - Trusting user IDs from frontend instead of validating on backend

- [ ] **Tampering considered**  
  _Can someone modify data they shouldn’t?_  
  Examples:
  - Manipulating request payloads in API calls
  - Changing React form data before submission
  - Not validating input on backend

- [ ] **Repudiation considered**  
  _Can a user deny they performed an action?_  
  Examples:
  - No logging of important actions (e.g., deletes, payments)
  - Missing user ID / correlation ID in logs
  - No audit trail in database

- [ ] **Information Disclosure considered**  
  _Could sensitive data leak?_  
  Examples:
  - Returning too much data in APIs (e.g., passwords, tokens)
  - Exposing stack traces or internal errors
  - Sending secrets to frontend (React env variables mistake)

- [ ] **Denial of Service considered**  
  _Can someone overload or break the system?_  
  Examples:
  - No rate limiting on APIs
  - Expensive queries without limits (e.g., large pagination)
  - Infinite loops or heavy computation on user input

- [ ] **Elevation of Privilege considered**  
  _Can a user gain more access than they should?_  
  Examples:
  - Frontend hiding buttons but backend not enforcing roles
  - Missing authorization checks in C# endpoints
  - Users modifying roles/permissions in requests

### 📊 Risk Assessment

- [ ] This change is **small / low risk**
- [ ] This change is **significant and requires risk assessment**

If significant please add brief explanation of impact, attack surface, mitigation etc.

### 🔒 General Security Checks

- [ ] **No secrets added**
  - No API keys, passwords, tokens in code or config

- [ ] **Dependencies reviewed**
  - New npm / NuGet packages checked for known vulnerabilities  

- [ ] **Input validation done**
  - All API inputs validated server-side (never trust React frontend)

- [ ] **Error handling safe**
  - No sensitive info in error messages (stack traces, SQL, internal paths)

### ✅ (Optional but recommended)

- [ ] Logging added for critical actions
- [ ] Authorization checked on backend
- [ ] Endpoints tested with invalid / malicious input


### 📚 Further Reading
For more security best practices and common vulnerabilities:  
👉 https://github.com/OWASP/Top10
