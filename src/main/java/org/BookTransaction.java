package org;

import java.io.Serializable;

public class BookTransaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private String transactionId;
    private String bookId;
    private String borrowerName;
    private String borrowDate;
    private String returnDate;
    private String actualReturnDate;
    private String condition;
    private boolean returned;

    public BookTransaction(String transactionId, String bookId, String borrowerName,
                           String borrowDate, String returnDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.borrowerName = borrowerName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.actualReturnDate = "";
        this.condition = "";
        this.returned = false;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public String getBookId() { return bookId; }
    public String getBorrowerName() { return borrowerName; }
    public String getBorrowDate() { return borrowDate; }
    public String getReturnDate() { return returnDate; }

    public String getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(String actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }

    @Override
    public String toString() {
        return transactionId + "|" + bookId + "|" + borrowerName + "|" +
                borrowDate + "|" + returnDate + "|" + actualReturnDate + "|" +
                condition + "|" + returned;
    }

    public static BookTransaction fromString(String line) {
        String[] parts = line.split("\\|");
        BookTransaction transaction = new BookTransaction(
                parts[0], parts[1], parts[2], parts[3], parts[4]
        );
        if (parts.length > 5) transaction.setActualReturnDate(parts[5]);
        if (parts.length > 6) transaction.setCondition(parts[6]);
        if (parts.length > 7) transaction.setReturned(Boolean.parseBoolean(parts[7]));
        return transaction;
    }
}
