# 📚 Perpustakaan Mini (Java Swing)

## Deskripsi Proyek

**Perpustakaan Mini** adalah aplikasi desktop berbasis **Java Swing** yang digunakan untuk mengelola peminjaman dan pengembalian buku secara sederhana. Aplikasi ini dilengkapi dengan fitur login, dashboard statistik, daftar buku, peminjaman buku, pengembalian buku, serta penyimpanan data menggunakan file teks.


---

## 🎯 Fitur Utama

* 🔐 **Login Admin**
* 📊 **Dashboard Statistik Buku**
* 📚 **Daftar Buku + Pencarian**
* 📖 **Peminjaman Buku**
* ↩️ **Pengembalian Buku**
* 🗂️ **Manajemen Transaksi Aktif**
* 💾 **Penyimpanan Data Otomatis (File TXT)**
* 🎨 **UI Modern (Custom Font & Rounded Button)**

---

## 👤 Akun Login Default

```
Username : admin
Password : admin123
```

---

## 🧱 Struktur Project

```
PerpustakaanMini/
│
├── Main.java                # Entry point aplikasi
├── LibraryApp.java          # UI utama (Swing + CardLayout)
├── LibraryManager.java      # Logika bisnis perpustakaan
├── Book.java                # Model data buku
├── BookTransaction.java     # Model transaksi peminjaman
├── FileHandler.java         # Simpan & load data ke file
├── FontManager.java         # Manajemen font Poppins
│
├── books.txt                # Data buku (auto-generate)
├── transactions.txt         # Data transaksi (auto-generate)
│
└── resources/
    └── Poppins-Regular.ttf  # Font UI (opsional)
```

---

## 🖥️ Tampilan Aplikasi

### 1️⃣ Login

* Autentikasi admin
* Validasi username & password

### 2️⃣ Dashboard

* Statistik total buku
* Buku dipinjam
* Buku tersedia

### 3️⃣ Daftar Buku

* Tabel buku lengkap
* Fitur pencarian (ID, judul, penulis, kategori)

### 4️⃣ Pinjam Buku

* Input ID buku
* Nama peminjam
* Tanggal pengembalian

### 5️⃣ Kembalikan Buku

* Input ID transaksi atau ID buku
* Pilih kondisi buku
* Lihat transaksi aktif

---

## 💾 Penyimpanan Data

Aplikasi menggunakan **file teks (.txt)** sebagai media penyimpanan:

* `books.txt` → Data buku
* `transactions.txt` → Data transaksi

Format data menggunakan delimiter `|`.

Contoh data buku:

```
B001|Naruto Vol. 1|Masashi Kishimoto|1999|Tersedia|Manga|||
```
---

## ⚙️ Cara Menjalankan Aplikasi

### 1️⃣ Persyaratan

* Java JDK 8 atau lebih baru
* IDE (IntelliJ IDEA / NetBeans / Eclipse)

### 2️⃣ Langkah Menjalankan

1. Clone atau download project
2. Pastikan semua file `.java` berada dalam satu package atau default package
3. Jalankan file:

```
Main.java
```

---

## 🛠️ Teknologi yang Digunakan

* **Java SE**
* **Java Swing (GUI)**
* **CardLayout & GridBagLayout**
* **File I/O (BufferedReader / FileWriter)**
* **OOP (Object-Oriented Programming)**

---

## 📌 Catatan Penting

* Jika font **Poppins** tidak ditemukan, aplikasi otomatis menggunakan font default.
* Data akan dibuat otomatis saat pertama kali aplikasi dijalankan.
* Aplikasi ini bersifat **offline** dan **single-user**.

---

## 👨‍💻 Pengembang

Dikembangkan sebagai projek pembelajaran Java GUI & OOP.

---

## 📜 Lisensi

Proyek ini bebas digunakan untuk keperluan **pembelajaran dan akademik**.

---

✨ *Perpustakaan Mini — Belajar Java Swing jadi lebih menyenangkan!*
