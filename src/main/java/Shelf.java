import Utilities.Code;

import java.util.HashMap;
import java.util.Objects;

public class Shelf {

    public static final int SHELF_NUMBER_ = 0;
    public static final int SUBJECT_ = 1;

    private HashMap<Book, Integer> books = new HashMap<Book,Integer>();

    private int shelfNumber;
    private String subject;


    public Shelf() {
    }

    public Shelf(int shelfNumber, String subject) {
        this.shelfNumber = shelfNumber;
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return shelfNumber == shelf.shelfNumber && Objects.equals(subject, shelf.subject);
    }

    @Override
    public String toString() {
        return shelfNumber + " : " + subject;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shelfNumber, subject);
    }

    public int getBookCount(Book book)
    {
        if(books.containsKey(book))
        {
            return books.get(book);
        } else {
            return -1;
        }
    }

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


    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}