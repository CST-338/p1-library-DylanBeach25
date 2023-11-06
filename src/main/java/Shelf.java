/**
 * File Name: Shelf.java
 * Abstract: This class represents a shelf that will hold
 *            Book objects, these shelves have a shelf number subject
 *            and a Hashmap of Books as keys with their quantity as int values
 * @author Dylan Beach
 * @since November 3rd, 2023
 */

import Utilities.Code;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Shelf {
    /**
     * The below constants represent shelf number and subjects index in a string
     */
    public static final int SHELF_NUMBER_ = 0;
    public static final int SUBJECT_ = 1;
    /**
     * books is a hashmap that has Book object as a key, and a quantity of that
     * book as an Integer value stored under the Book Key
     */
    private HashMap<Book, Integer> books;
            //= new HashMap<Book,Integer>();
    /**
     * This int is used to represent the number of the shelf
     */
    private int shelfNumber;
    /**
     * This String represents that subject of books that
     * are contained on this shelf
     */
    private String subject;

    /**
     * Deprecated constructor, will be phased out later, instantiates
     * the hashmap books so it won't throw a nullpointer exception
     */
    public Shelf() {
        books = new HashMap<Book, Integer>();
    }

    /**
     * Paramaeterized constructor, instantuates shelfNumber subject to the parameters
     * and instantiates books to a new Hashmap<Book,Integer></Book,Integer>
     * @param shelfNumber
     * @param subject
     */
    public Shelf(int shelfNumber, String subject) {
        this.shelfNumber = shelfNumber;
        this.subject = subject;
        books = new HashMap<Book, Integer>();
    }

    /**
     * Checks object equivalence based on shelfNumber and Subject
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return shelfNumber == shelf.shelfNumber && Objects.equals(subject, shelf.subject);
    }

    /**
     * Returns a string represenation of a shelf with its number and subject
     * @return String
     */
    @Override
    public String toString() {
        return shelfNumber + " : " + subject;
    }

    /**
     * Checks hashCode based on shelfNumber and subject
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(shelfNumber, subject);
    }

    /**
     * Searches books hashmap for key of book parameter
     * if the key is found the int representing copies held is returned
     * if not -1 is returned to indicate the shelf does not have that book
     * @param book
     * @return int
     */
    public int getBookCount(Book book)
    {
        if(books.containsKey(book))
        {
            return books.get(book);
        } else {
            return -1;
        }
    }

    /**
     * addBook first makes sure that the subject of the Book param matches
     * the subject of the shelf. If it doesn't match a mismatch code is returned
     * If it does it makes a check to see if the
     * books hashMap already contains a key matching Book
     * If it does its integer value is incremented
     * If it does not then it is placed in the Hashmap with a new key of book and
     * value of 1, returns a Success code
     * @param book
     * @return Code
     */
    public Code addBook(Book book)
    {
        if(book.getSubject().equals(this.getSubject())) {
            if (books.containsKey(book)) {
                books.put(book, books.get(book) + 1);
                System.out.println(book.toString() + " added to shelf " + this.toString());
                return Code.SUCCESS;
            } else {
                books.put(book, 1);
                return Code.SUCCESS;
            }
        } else {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
    }

    /**
     * removeBook checks to see if a shelf contains a certain Book param
     * If the Book is not in the hashmap it prints that it is not and returns a
     * book not in inventory code
     * If it is in the map it decrements the integer value of the Book key and returns
     * a Success code.
     * If the book is in the map, however contains no copies it is printed that no
     * copies remain and a not in inventory code is returned
     * @param book
     * @return Code
     */
    public Code removeBook(Book book)
    {
        if(!books.containsKey(book))
        {
            System.out.println(book.getTitle() + " is not on shelf " + this.getSubject());
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else if(books.get(book)==0) {
            System.out.println("No copies of " + book.getTitle() + " remain on shelf " + this.getSubject());
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else {
            books.put(book,books.get(book)-1);
            return Code.SUCCESS;
        }
    }

    /**
     * listBooks returns a string listing all the books contained in the shelf objects
     * It loops through the hashMap obtaining quantities of each book and appedning them
     * to a string builder, where all books and their amounts are printed
     * The stringbuilder is then converted to a string and returned
     * @return String
     */
    public String listBooks()
    {
        int bookCount=0;
        StringBuilder list = new StringBuilder();
        for(Book book:books.keySet())
        {
            bookCount = bookCount + books.get(book);
        }
        if(bookCount > 1 )
        {
            list.append(bookCount + " books on shelf: " +  this.toString());
            for(Book book: books.keySet())
            {
                list.append("\n" + book.toString() + " " + books.get(book));
            }

            return list.toString();
        } else {
            list.append(bookCount + " book on shelf: " + this.toString());
            for(Book book: books.keySet())
            {
                list.append("\n" + book.toString() + " " + books.get(book));
            }
            return list.toString();
        }


    }

    /**
     * Standard getter
     * @return books
     */
    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    /**
     * Standard setter
     * @param books
     */
    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    /**
     * standard getter
     * @return shelfNumber
     */
    public int getShelfNumber() {
        return shelfNumber;
    }

    /**
     * standard Setter
     * @param shelfNumber
     */
    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    /**
     * Standard getter
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * standard Setter
     * @param subject
     */

    public void setSubject(String subject) {
        this.subject = subject;
    }
}