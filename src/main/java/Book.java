/*
    File Name: Book.java
    Abstract: This class represents a book object with an isbn, title, subject, pageCount, author, and dueDate
               it will be used in conjunction with a shelf, library and reader class to create a functioning library
    @author Dylan Beach
    @since November 2nd, 2023
 */

import java.time.LocalDate;
import java.util.Objects;

public class Book {
    /**
     * The below constant ints are used to hold the index value
     * that the field names will appear in a string
     */
    public final static int ISBN_ = 0;
    public final static int TITLE_ = 1;
    public final static int SUBJECT_ = 2;
    public final static int PAGE_COUNT_ = 3;
    public final static int AUTHOR_ = 4;
    public final static int DUE_DATE_ = 5;
    /**
     * This string is used to represent the isbn book number the book holds for organization
     */
    private String isbn;
    /**
     * This string holds the title of the book
     */
    private String title;
    /**
     * This string holds the subject of the book
     */
    private String subject;
    /**
     * This integer holds the amount of pages in the book
     */
    private int pageCount;
    /**
     * This string holds the author's name
     */
    private String author;
    /**
     * This LocalDate holds the due date of the book
     */
    private LocalDate dueDate;

    /**
     *
     * @param isbn sets respective instance field
     * @param title sets respective instance field
     * @param subject sets respective instance field
     * @param pageCount sets respective instance field
     * @param author sets respective instance field
     * @param dueDate sets respective instance field
     */
    public Book(String isbn, String title, String subject, int pageCount, String author, LocalDate dueDate) {
        this.isbn = isbn;
        this.title = title;
        this.subject = subject;
        this.pageCount = pageCount;
        this.author = author;
        this.dueDate = dueDate;
    }

    /**
     * standard getter
     * @return isbn
     */
    public String getISBN() {
        return isbn;
    }

    /**
     * standard Setter
     * @param isbn
     */
    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    /**
     * standard getter
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * standard setter
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * standard getter
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * standard setter
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * standard getter
     * @return pageCount
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * standard setter
     * @param pageCount
     */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * standard getter
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * standard setter
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * standard getter
     * @return dueDate
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * standard setter
     * @param dueDate
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * checks equivalence of two objects based on pageCount,
     * isbn, title, subject, and author
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return getPageCount() == book.getPageCount() && Objects.equals(isbn, book.isbn) && Objects.equals(getTitle(), book.getTitle()) && Objects.equals(getSubject(), book.getSubject()) && Objects.equals(getAuthor(), book.getAuthor());
    }

    /**
     * checks hashcode for pageCount, isbn, title, subject, and author
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(isbn, getTitle(), getSubject(), getPageCount(), getAuthor());
    }

    /**
     * returns a string that has the book title followed by its author and isbn
     * @return String
     */
    @Override
    public String toString() {
        return title + " by " + author + " ISBN: " + isbn;
    }
}