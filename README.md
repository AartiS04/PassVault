# 🔐 PassVault – Java-Based AES Encrypted Password Manager

**PassVault** is a lightweight command-line password manager built in Java that securely stores user credentials using AES encryption. The application uses a master password to dynamically generate encryption keys, ensuring that sensitive information is both encrypted and accessible only by the rightful user.

---

## 🧩 Features

- 🔑 **Master password protection**  
  Set and verify a master password on first use to control vault access.

- 🔒 **AES encryption for credentials**  
  All data is encrypted using AES (Advanced Encryption Standard) with keys derived from the master password.

- 📁 **Local encrypted storage**  
  Credentials are saved securely in a local file (`vault.txt`).

- 🔁 **Dynamic key generation**  
  No encryption keys are stored; they are generated from your input each session.

- 📦 **No external libraries required**  
  Uses only standard Java libraries.

---

## ⚙️ Technologies Used

- Java SE  
- AES encryption (`javax.crypto.Cipher`, `SecretKeySpec`)  
- File I/O (`java.io`, `java.util.Scanner`)  
- Base64 Encoding/Decoding (`java.util.Base64`)
