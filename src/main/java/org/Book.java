package org;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String author;
    private String year;
    private String status; // "Tersedia" atau "Dipinjam"
    private String category;
    private String borrower; // Nama peminjam (jika dipinjam)
    private String borrowDate;
    private String returnDate;

    public Book(String id, String title, String author, String year, String status, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.status = status;
        this.category = category;
        this.borrower = "";
        this.borrowDate = "";
        this.returnDate = "";
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBorrower() { return borrower; }
    public void setBorrower(String borrower) { this.borrower = borrower; }

    public String getBorrowDate() { return borrowDate; }
    public void setBorrowDate(String borrowDate) { this.borrowDate = borrowDate; }

    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }

    @Override
    public String toString() {
        return id + "|" + title + "|" + author + "|" + year + "|" +
                status + "|" + category + "|" + borrower + "|" +
                borrowDate + "|" + returnDate;
    }

    public static Book fromString(String line) {
        String[] parts = line.split("\\|");
        Book book = new Book(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        if (parts.length > 6) book.setBorrower(parts[6]);
        if (parts.length > 7) book.setBorrowDate(parts[7]);
        if (parts.length > 8) book.setReturnDate(parts[8]);
        return book;
    }
}