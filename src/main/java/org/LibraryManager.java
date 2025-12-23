package org;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private List<Book> books;
    private List<BookTransaction> transactions;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LibraryManager() {
        this.books = FileHandler.loadBooks();
        this.transactions = FileHandler.loadTransactions();
        updateBookStatusFromTransactions();
    }

    // Update status buku dari transaksi yang aktif
    private void updateBookStatusFromTransactions() {
        for (BookTransaction transaction : transactions) {
            if (!transaction.isReturned()) {
                Book book = getBookById(transaction.getBookId());
                if (book != null) {
                    book.setStatus("Dipinjam");
                    book.setBorrower(transaction.getBorrowerName());
                    book.setBorrowDate(transaction.getBorrowDate());
                    book.setReturnDate(transaction.getReturnDate());
                }
            }
        }
        saveBooks(); // Simpan status yang sudah diupdate
    }

    // Get semua buku
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    // Get buku berdasarkan ID
    public Book getBookById(String bookId) {
        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(bookId)) {
                return book;
            }
        }
        return null;
    }

    // Pinjam buku
    public boolean borrowBook(String bookId, String borrowerName, String returnDate) {
        Book book = getBookById(bookId.trim());

        System.out.println("Mencari buku dengan ID: " + bookId);
        System.out.println("Buku ditemukan: " + (book != null));

        if (book == null) {
            System.out.println("Buku tidak ditemukan!");
            return false; // Buku tidak ditemukan
        }

        System.out.println("Status buku: " + book.getStatus());

        if (!book.getStatus().equals("Tersedia")) {
            System.out.println("Buku tidak tersedia untuk dipinjam!");
            return false; // Buku tidak tersedia
        }

        try {
            // Validasi format tanggal
            LocalDate.parse(returnDate, DATE_FORMATTER);

            // Update status buku
            book.setStatus("Dipinjam");
            book.setBorrower(borrowerName);
            book.setBorrowDate(LocalDate.now().format(DATE_FORMATTER));
            book.setReturnDate(returnDate);

            // Buat transaksi
            String transactionId = "T" + System.currentTimeMillis();
            BookTransaction transaction = new BookTransaction(
                    transactionId,
                    book.getId(), // Gunakan ID buku yang sudah dipastikan ada
                    borrowerName,
                    LocalDate.now().format(DATE_FORMATTER),
                    returnDate
            );
            transactions.add(transaction);

            System.out.println("Transaksi berhasil dibuat:");
            System.out.println("ID Transaksi: " + transactionId);
            System.out.println("ID Buku: " + book.getId());
            System.out.println("Peminjam: " + borrowerName);

            // Simpan perubahan ke file
            saveAllData();

            return true;

        } catch (DateTimeParseException e) {
            System.out.println("Format tanggal salah: " + returnDate);
            return false;
        }
    }

    // Kembalikan buku
    public boolean returnBook(String transactionId, String condition) {
        System.out.println("Mencari transaksi dengan ID: " + transactionId);

        BookTransaction transaction = null;
        for (BookTransaction t : transactions) {
            System.out.println("Cek transaksi: " + t.getTransactionId() + " - Dikembalikan: " + t.isReturned());
            if (t.getTransactionId().equals(transactionId) && !t.isReturned()) {
                transaction = t;
                break;
            }
        }

        if (transaction == null) {
            System.out.println("Transaksi tidak ditemukan atau sudah dikembalikan!");

            // Coba cari dengan ID buku
            System.out.println("Mencari dengan ID buku...");
            for (BookTransaction t : transactions) {
                if (t.getBookId().equalsIgnoreCase(transactionId) && !t.isReturned()) {
                    transaction = t;
                    System.out.println("Ditemukan dengan ID buku: " + t.getBookId());
                    break;
                }
            }

            if (transaction == null) {
                System.out.println("Transaksi masih tidak ditemukan");
                return false;
            }
        }

        System.out.println("Transaksi ditemukan:");
        System.out.println("ID Transaksi: " + transaction.getTransactionId());
        System.out.println("ID Buku: " + transaction.getBookId());

        // Update transaksi
        transaction.setReturned(true);
        transaction.setActualReturnDate(LocalDate.now().format(DATE_FORMATTER));
        transaction.setCondition(condition);

        // Update buku
        Book book = getBookById(transaction.getBookId());
        if (book != null) {
            book.setStatus("Tersedia");
            book.setBorrower("");
            book.setBorrowDate("");
            book.setReturnDate("");
            System.out.println("Status buku diupdate: " + book.getId() + " -> Tersedia");
        } else {
            System.out.println("Buku dengan ID " + transaction.getBookId() + " tidak ditemukan!");
        }

        // Simpan perubahan ke file
        saveAllData();
        System.out.println("Data berhasil disimpan");

        return true;
    }

    // Cari transaksi berdasarkan ID atau ID buku
    public BookTransaction findTransaction(String searchId) {
        // Cari berdasarkan ID transaksi
        for (BookTransaction transaction : transactions) {
            if (transaction.getTransactionId().equals(searchId) && !transaction.isReturned()) {
                return transaction;
            }
        }

        // Cari berdasarkan ID buku
        for (BookTransaction transaction : transactions) {
            if (transaction.getBookId().equalsIgnoreCase(searchId) && !transaction.isReturned()) {
                return transaction;
            }
        }

        return null;
    }

    // Cari buku
    public List<Book> searchBooks(String keyword) {
        List<Book> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(lowerKeyword) ||
                    book.getAuthor().toLowerCase().contains(lowerKeyword) ||
                    book.getCategory().toLowerCase().contains(lowerKeyword) ||
                    book.getId().toLowerCase().contains(lowerKeyword)) {
                results.add(book);
            }
        }
        return results;
    }

    // Hitung statistik
    public int[] getStatistics() {
        int totalBooks = books.size();
        int borrowedBooks = 0;

        for (Book book : books) {
            if (book.getStatus().equals("Dipinjam")) {
                borrowedBooks++;
            }
        }

        int availableBooks = totalBooks - borrowedBooks;

        return new int[]{totalBooks, borrowedBooks, availableBooks};
    }

    // Get transaksi aktif
    public List<BookTransaction> getActiveTransactions() {
        List<BookTransaction> active = new ArrayList<>();
        for (BookTransaction transaction : transactions) {
            if (!transaction.isReturned()) {
                active.add(transaction);
            }
        }
        return active;
    }

    // Get transaksi berdasarkan ID buku
    public BookTransaction getTransactionByBookId(String bookId) {
        for (BookTransaction transaction : transactions) {
            if (transaction.getBookId().equalsIgnoreCase(bookId) && !transaction.isReturned()) {
                return transaction;
            }
        }
        return null;
    }

    // Simpan semua data
    private void saveAllData() {
        FileHandler.saveBooks(books);
        FileHandler.saveTransactions(transactions);
    }

    // Simpan buku saja
    private void saveBooks() {
        FileHandler.saveBooks(books);
    }

    // Generate tanggal pengembalian (7 hari dari sekarang)
    public static String generateReturnDate() {
        return LocalDate.now().plusDays(3).format(DATE_FORMATTER);
    }

    // Format tanggal Indonesia
    public static String formatIndonesianDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date, DATE_FORMATTER);
            String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni",
                    "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
            return localDate.getDayOfMonth() + " " + bulan[localDate.getMonthValue() - 1] + " " + localDate.getYear();
        } catch (Exception e) {
            return date;
        }
    }

    // Debug: print semua transaksi aktif
    public void printActiveTransactions() {
        System.out.println("\n=== DAFTAR TRANSAKSI AKTIF ===");
        int count = 0;
        for (BookTransaction transaction : transactions) {
            if (!transaction.isReturned()) {
                count++;
                System.out.println(count + ". ID Transaksi: " + transaction.getTransactionId());
                System.out.println("   ID Buku: " + transaction.getBookId());
                System.out.println("   Peminjam: " + transaction.getBorrowerName());
                System.out.println("   Tanggal Pinjam: " + transaction.getBorrowDate());
                System.out.println("   Tanggal Kembali: " + transaction.getReturnDate());
                System.out.println("--------------------------------");
            }
        }
        System.out.println("Total transaksi aktif: " + count);
    }

    // Debug: print semua buku yang dipinjam
    public void printBorrowedBooks() {
        System.out.println("\n=== DAFTAR BUKU DIPINJAM ===");
        int count = 0;
        for (Book book : books) {
            if (book.getStatus().equals("Dipinjam")) {
                count++;
                System.out.println(count + ". ID Buku: " + book.getId());
                System.out.println("   Judul: " + book.getTitle());
                System.out.println("   Peminjam: " + book.getBorrower());
                System.out.println("   Status: " + book.getStatus());
                System.out.println("--------------------------------");
            }
        }
        System.out.println("Total buku dipinjam: " + count);
    }
}
