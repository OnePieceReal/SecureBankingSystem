# Secure Banking System (Java/JavaFX)

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-19%2B-orange)
![Security](https://img.shields.io/badge/Security-Protocols-green)

## Features
- **Secure Authentication Protocol**:
  - Two-layer authentication using shared keys and derived master secrets
  - Mutual authentication between ATM clients and bank server

- **Key Management**:
  - Authenticated key distribution protocol to establish Master Secret
  - Key derivation function to create encryption and MAC keys

- **Secure Transactions**:
  - Encrypted and integrity-protected transactions for:
    - Balance inquiries
    - Deposits
    - Withdrawals
  - Message Authentication Codes (MAC) for all communications

- **Audit Logging**:
  - Encrypted record of all customer actions
  - Includes customer ID, action type, and timestamp
