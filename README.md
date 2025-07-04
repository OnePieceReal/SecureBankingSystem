# Banking System Security Protocols
This project involves the design and implementation of secure communication protocols for a banking system consisting of a bank server and multiple ATM clients. The system ensures secure authentication, key distribution, and encrypted transactions while maintaining an encrypted audit log of all activities.
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
