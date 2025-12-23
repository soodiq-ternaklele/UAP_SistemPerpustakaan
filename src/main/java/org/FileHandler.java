package org;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String BOOKS_FILE = "books.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    // Simpan data buku
    public static void saveBooks(List<Book> books) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books) {
                writer.println(book.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    // Load data buku
    public static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        File file = new File(BOOKS_FILE);

        if (!file.exists()) {
            // Buat data buku default jika file tidak ada
            books.addAll(getDefaultBooks());
            saveBooks(books);
            return books;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                books.add(Book.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
        return books;
    }

    // Simpan transaksi
    public static void saveTransactions(List<BookTransaction> transactions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (BookTransaction transaction : transactions) {
                writer.println(transaction.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    // Load transaksi
    public static List<BookTransaction> loadTransactions() {
        List<BookTransaction> transactions = new ArrayList<>();
        File file = new File(TRANSACTIONS_FILE);

        if (!file.exists()) {
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactions.add(BookTransaction.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
        return transactions;
    }

    // Data buku default
    private static List<Book> getDefaultBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("B001", "Naruto Vol. 1", "Masashi Kishimoto", "1999", "Tersedia", "Manga"));
        books.add(new Book("B002", "One Piece Vol. 1", "Eiichiro Oda", "1997", "Tersedia", "Manga"));
        books.add(new Book("B003", "Attack on Titan Vol. 1", "Hajime Isayama", "2009", "Tersedia", "Manga"));
        books.add(new Book("B004", "Demon Slayer Vol. 1", "Koyoharu Gotouge", "2016", "Tersedia", "Manga"));
        books.add(new Book("B005", "Jujutsu Kaisen Vol. 1", "Gege Akutami", "2018", "Dipinjam", "Manga"));
        books.add(new Book("B006", "Harry Potter", "J.K. Rowling", "1997", "Tersedia", "Fantasi"));
        books.add(new Book("B007", "The Hobbit", "J.R.R. Tolkien", "1937", "Tersedia", "Fantasi"));
        books.add(new Book("B008", "Dilan 1990", "Pidi Baiq", "2014", "Tersedia", "Romantis"));
        books.add(new Book("B009", "Laskar Pelangi", "Andrea Hirata", "2005", "Tersedia", "Drama"));
        books.add(new Book("B010", "Bumi Manusia", "Pramoedya Ananta Toer", "1980", "Tersedia", "Sejarah"));
        books.add(new Book("B011", "Filosofi Teras", "Henry Manampiring", "2018", "Tersedia", "Filsafat"));
        books.add(new Book("B012", "Clean Code", "Robert C. Martin", "2008", "Tersedia", "Teknologi"));
        books.add(new Book("B013", "Atomic Habits", "James Clear", "2018", "Tersedia", "Pengembangan Diri"));
        books.add(new Book("B014", "Sapiens", "Yuval Noah Harari", "2011", "Tersedia", "Sejarah"));
        books.add(new Book("B015", "The Psychology of Money", "Morgan Housel", "2020", "Tersedia", "Ekonomi"));
        books.add(new Book("B016", "Teruslah bodoh jangan pintar", "Tere laiye", "2020", "Tersedia", "Novel"));
        return books;
    }
}
