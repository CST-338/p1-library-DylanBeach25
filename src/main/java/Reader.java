/**
 * File Name: Reader.java
 * Abstract: This class represents a reader at the library. They have
 *            a card number, name, phone number, and a list of books
 *            that have been checked out
 * @author Dylan Beach
 * @since November 2nd, 2023
 */

import Utilities.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reader {
    /**
     * The belows constant ints are used to hold the index value
     * that the field names will appear in a string
     */
    public static final int CARD_NUMBER_ = 0;
    public static final int NAME_ = 1;
    public static final int PHONE_ = 2;
    public static final int BOOK_COUNT_ = 3;
    public static final int BOOK_START_ = 4;
    /**
     * This int is used to represent the reader's library card #
     */
    private int cardNumber;
    /**
     * This string is used to represent the reader's name
     */
    private String name;
    /**
     * This string is used to represent the reader's phone number
     */
    private String phone;
    /**
     * This List stores the Book objects the reader has checked out
     */
    private List<Book> books;

    /**
     * The constructor constructs a Reader object with a cardNumber, name, and phonenumber
     * while also instantiating a new ArrayList for their books
     * @param cardNumber
     * @param name
     * @param phone
     */
    public Reader(int cardNumber, String name, String phone) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.phone = phone;
        books = new ArrayList<Book>();
    }

    /**
     * The addBook method attempts to add a book to a reader's books ArrayList
     * If succesful the book is added and a success code is returned
     * If the book is already in the list, a book already checkout error Code is returned
     * @param book
     * @return Code
     */
    public Code addBook(Book book)
    {
        if(books.contains(book))
        {
            return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
        } else {
            books.add(book);
            return Code.SUCCESS;
        }
    }

    /**
     * The removeBook method checks to see if a book object is in the Arraylist
     * if it is the book is removed and a Success code is returned
     * If it is not a Reader doesn't have book error code is returned
     * All other exceptions will return the Reader could not remove book error code
     * @param book
     * @return Code
     */
    public Code removeBook(Book book) {
        if (!books.contains(book)) {
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        } else {
            try {
                books.remove(book);
                return Code.SUCCESS;
            } catch (Exception e) {
                return Code.READER_COULD_NOT_REMOVE_BOOK_ERROR;
            }
        }
    }

    /**
     * The hasBook method checks the arrayList books to see if it has the book param in it
     * if it does it returns true, otherwise, false
     * @param book
     * @return boolean
     */
    public boolean hasBook(Book book)
    {
        return books.contains(book);
    }

    /**
     * Checks object equivalence based on cardnumber, name, and phone number
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return cardNumber == reader.cardNumber && Objects.equals(name, reader.name) && Objects.equals(phone, reader.phone);
    }

    /**
     * Checks hashCode based on cardNumber, name, and phone number
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, name, phone);
    }

    /**
     * Returns string representation of object that has all of their info
     * @return String
     */
    @Override
    public String toString() {
        return name + "(#" + cardNumber + ") has checked out { " + books + "}";
    }

    /**
     * Standard getter
     * @return cardNumber
     */
    public int getCardNumber() {
        return cardNumber;
    }

    /**
     * standard Setter
     * @param cardNumber
     */
    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Standard getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * standard Setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * standard Getter
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * standard Setter
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * standard Getter
     * @return books
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * standard Setter
     * @param books
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * returns the amount of books the reader has checked out
     * @return int, books.size()
     */
    public int getBookCount()
    {
        return books.size();
    }

}