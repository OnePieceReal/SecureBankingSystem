# Secure Banking System (Java/JavaFX)

![Java](https://img.shields.io/badge/Java-21%2B-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-24%2B-orange)
![Security](https://img.shields.io/badge/Security-Protocols-green)

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

 
## NetBeans Setup Guide

### Prerequisites
- NetBeans IDE 18+ (with Java support)
- Java SE Development Kit 21
- JavaFX SDK 24

### Installation Steps

1. **Install JDK 21**
   - Download from [Oracle JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
   - Set as default JDK in NetBeans:
     ```
     Tools → Java Platforms → Add JDK...
     ```

2. **Install JavaFX 24 SDK**
   - Download from [Gluon JavaFX](https://gluonhq.com/products/javafx/)
   - Extract to a permanent location (e.g., `C:\javafx-sdk-24`)

3. **Configure NetBeans for JavaFX**
   - Go to:
     ```
     File → Project Properties → Libraries
     ```
   - Add these VM Options:
     ```
     --module-path="path/to/javafx-sdk-24/lib" --add-modules=javafx.controls,javafx.fxml
     ```
   - Example for Windows:
     ```
     --module-path="C:\javafx-sdk-24\lib" --add-modules=javafx.controls,javafx.fxml
     ```

4. **Clone and Import Project**
   ```bash
   git clone [repository-url]
