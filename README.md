# 📌 Project Title
**LSB-Vernam Secure Embedder**  
*A Hybrid System for Secure Message Embedding Using LSB Steganography and Vernam Cipher*

## 📖 Description
SilentCipher is a hybrid security system that combines the power of **symmetric encryption** (Vernam Cipher) and **LSB steganography** to provide dual-layered data protection. It encrypts messages with a one-time pad and hides them in images using zig-zag LSB embedding, offering both **confidentiality** and **covert transmission**.

## 🚀 Features
- 🔐 One-Time Pad Encryption (Vernam Cipher)
- 🖼️ LSB Image Steganography with zig-zag pixel traversal
- 📁 Supports lossless image formats (PNG, BMP)
- 📤 Embeds metadata (size, seed, flags) for reliable extraction
- 🔍 Secure against brute-force, visual, and histogram analysis

## 🛠️ How It Works

### 🔒 Encoding Process:
1. User inputs a plaintext message.
2. System encrypts it using a one-time pad (Vernam Cipher).
3. Encrypted message is embedded into an image using zig-zag LSB steganography.
4. Output: A **stego-image** with the secret message hidden inside.

### 🔓 Decoding Process:
1. User selects the stego-image.
2. System extracts metadata and the hidden encrypted message.
3. Decrypts it using the same key (seed-based).
4. Output: Original plaintext message.

## 💾 Requirements
- Java 8 or higher  
- OpenCV library  
- JavaFX for the graphical user interface  
- Compatible image format: `.png` or `.bmp` (lossless)

## 📸 Usage

### 🔧 Setup
1. Clone the repo  
   ```bash
   git clone https://github.com/yourusername/SilentCipher.git
   ```
2. Import into your Java IDE (e.g., IntelliJ, Eclipse)  
3. Ensure OpenCV and JavaFX are properly configured

### ▶️ Run
- **To Encode**:  
   Input message → Choose cover image → Output stego image

- **To Decode**:  
   Choose stego image → Extract and decrypt message

## 🧪 Security Notes
- Encryption is only as strong as the key; ensure **non-reuse** of the one-time pad key.
- Use **lossless images** only (PNG or BMP) to avoid data loss.
- Embedding large data may increase detectability—balance is key.

## 👨‍💻 Authors

- [**Ahmad Budairi** ](https://github.com/ahmadbudz)
- [**Mitri Khoury** ](https://github.com/mitrikhoury)
- [**Belal Hamdeh** ](https://github.com/Hamdeh-Belal)

## 📄 License
This project is for academic purposes. Contact authors for permission before reusing or modifying.
