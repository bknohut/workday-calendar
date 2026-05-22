## 🔐 Security Checklist

### 🧠 STRIDE Analysis

- [ ] **Spoofing considered**  
  _Could someone pretend to be another user or system?_  

- [ ] **Tampering considered**  
  _Can someone modify data they shouldn’t?_  

- [ ] **Repudiation considered**  
  _Can a user deny they performed an action?_  

- [ ] **Information Disclosure considered**  
  _Could sensitive data leak?_  

- [ ] **Denial of Service considered**  
  _Can someone overload or break the system?_  

- [ ] **Elevation of Privilege considered**  
  _Can a user gain more access than they should?_  

### 📊 Risk Assessment

- [ ] This change is **small / low risk**
- [ ] This change is **significant and requires risk assessment**

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
