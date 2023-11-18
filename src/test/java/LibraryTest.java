import Utilities.Code;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @version  1.3.1.
 */

class LibraryTest {

    Library csumb = null;
    Reader read = new Reader(1, "Jennifer Clinkenbeard", "831-555-6284");
    Shelf shelf = new Shelf(1,"Adventure");
    Book book = new Book("5297", "Count of Monte Cristo", "Adventure", 999, "Alexandrea Dumas", LocalDate.of(2021, 1, 1));
    Book book2 = new Book("5296", "Count of Monte Cristo", "Sci-fi", 999, "Alexandrea Dumas", LocalDate.of(2021, 1, 1));

    String library00 = "Library00.csv";
    String library01 = "Library01.csv";
    String badBooks0 = "badBooks0.csv";
    String badBooks1 = "badBooks1.csv";
    String badShelves0 = "badShelves0.csv";
    String badShelves1 = "badShelves1.csv";
    String badReader0 = "badReader0.csv";
    String badReader1 = "badReader1.csv";


    @BeforeEach
    void setUp() {
        csumb= null;
        assertNull(csumb);
        csumb = new Library("CSUMB");
        assertNotNull(csumb);
    }

    @AfterEach
    void tearDown() {
        csumb = null;
    }

    @Test
    void init_test() {
        //Bad file
        assertEquals(Code.FILE_NOT_FOUND_ERROR, csumb.init("nope.csv"));
        assertEquals(Code.BOOK_COUNT_ERROR, csumb.init(badBooks0));
        assertEquals(Code.BOOK_COUNT_ERROR, csumb.init(badBooks1) );
        assertEquals( Code.SHELF_COUNT_ERROR,csumb.init(badShelves0));
        assertEquals( Code.SHELF_NUMBER_PARSE_ERROR,csumb.init(badShelves1));


    }

    @Test
    void init_goodFile_test() {
        assertEquals(Code.SUCCESS,csumb.init(library00));
        assertNotEquals(true, csumb.getBooks().containsKey(book));
        //csumb.addBook(book);
        //assertEquals(true, csumb.getBooks().containsKey(book));
        assertEquals(9,csumb.listBooks());
    }

    @Test
    void addBook() {
        assertNotEquals(true,csumb.getBooks().containsKey(book));
        csumb.addShelf(shelf);
        assertEquals(Code.SUCCESS,csumb.addBook(book));
        assertEquals(true,csumb.getBooks().containsKey(book));
        assertEquals(Code.SHELF_EXISTS_ERROR,csumb.addBook(book2));
        //this last test ensures that 2 copies of the same book can be added increasing their count
        assertEquals(Code.SUCCESS,csumb.addBook(book));


    }

    @Test
    void returnBook() {
        assertEquals(Code.READER_DOESNT_HAVE_BOOK_ERROR, csumb.returnBook(read,book));
        read.addBook(book);
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR,csumb.returnBook(read,book));
        csumb.addBook(book);
        csumb.addShelf(shelf);
        assertEquals(Code.SUCCESS,csumb.returnBook(read,book));
    }

    @Test
    void testReturnBook() {
        read.addBook(book);
        csumb.addBook(book);
        csumb.addShelf(shelf);
        csumb.returnBook(book);
        assertEquals(1,csumb.getBooks().get(book));
        //not finished yet

    }

    @Test
    void listBooks() {
        csumb.addBook(book);
        //csumb.addShelf("Sci-fi");
        csumb.addBook(book2);
        csumb.addBook(book2);
        csumb.addShelf("Sci-fi");
        assertEquals(3,csumb.listBooks());

    }

    @Test
    void checkOutBook() {
    }

    @Test
    void getBookByISBN() {
        assertEquals(null,csumb.getBookByISBN("5297"));
        csumb.addShelf(shelf);
        csumb.addBook(book);
        assertEquals(book,csumb.getBookByISBN("5297"));
    }

    @Test
    void listShelves() {
    }

    @Test
    void addShelf() {
    }

    @Test
    void testAddShelf() {
    }

    @Test
    void getShelf() {
    }

    @Test
    void testGetShelf() {
    }

    @Test
    void listReaders() {
    }

    @Test
    void testListReaders() {
    }

    @Test
    void getReaderByCard() {
    }

    @Test
    void addReader() {
    }

    @Test
    void removeReader() {
    }

    @Test
    void convertInt() {
    }

    @Test
    void convertDate() {
    }

    @Test
    void getLibraryCardNumber() {
    }
}