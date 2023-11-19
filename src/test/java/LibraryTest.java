import Utilities.Code;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @version  1.3.1.
 */

class LibraryTest {

    Library csumb = null;
    Reader read = new Reader(1, "Jennifer Clinkenbeard", "831-555-6284");
    Reader read2 = new Reader (2,"Dylan Beach","123-456-7890");
    Reader read3 = new Reader (2, "Noah Weatherbie", "111-111-1111");
    Shelf shelf = new Shelf(1,"Adventure");
    Book book = new Book("5297", "Count of Monte Cristo", "Adventure", 999, "Alexandrea Dumas", LocalDate.of(2021, 1, 1));
    Book book2 = new Book("5296", "Count of Monte Cristo", "Sci-fi", 999, "Alexandrea Dumas", LocalDate.of(2021, 1, 1));
    Book book3 = new Book("1111","Coding Adventure","Sci-fi",100,"Dylan Beach",LocalDate.of(2023,1,1));

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
        read.addBook(book3);
        assertEquals(true,read.getBooks().contains(book));
        csumb.addBook(book);
        csumb.addShelf(shelf);
        csumb.returnBook(read,book);
        assertEquals(1,csumb.getBooks().get(book));
        assertEquals(false,read.getBooks().contains(book));
        assertEquals(Code.READER_DOESNT_HAVE_BOOK_ERROR,csumb.returnBook(read,book2));
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR,csumb.returnBook(read,book3));

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
        assertEquals(Code.READER_NOT_IN_LIBRARY_ERROR,csumb.checkoutBook(read,book));
        csumb.addReader(read);
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR,csumb.checkoutBook(read,book));
        csumb.addBook(book);
        assertEquals(Code.SHELF_EXISTS_ERROR,csumb.checkoutBook(read,book));
        csumb.addShelf("Adventure");
        assertEquals(Code.SUCCESS,csumb.checkoutBook(read,book));
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR,csumb.checkoutBook(read,book));
        csumb.addBook(book);
        //checks to make sure they can't check the same book out twice
        assertNotEquals(Code.SUCCESS,csumb.checkoutBook(read,book));
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
       csumb.addShelf("Sci-fi");
       csumb.addShelf("Adventure");
       assertEquals(2,csumb.listShelves(false));
       System.out.println("Break");
       csumb.addBook(book);
       csumb.addBook(book2);
       assertEquals(true,csumb.getShelves().get("Sci-fi").getBooks().containsKey(book2));
       assertEquals(2,csumb.listShelves(true));
    }

    @Test
    void addShelf() {
        //Shelf being added
        assertNotEquals(true,csumb.getShelves().containsKey(shelf.getSubject()));
        csumb.addShelf(shelf);
        assertEquals(true,csumb.getShelves().containsKey(shelf.getSubject()));
    }

    @Test
    void testAddShelf() {
        csumb.addBook(book);
        assertEquals(Code.SUCCESS,csumb.addShelf(shelf));
        assertEquals(true, csumb.getShelves().get(shelf.getSubject()).getBooks().containsKey(book));
        assertEquals(Code.SHELF_EXISTS_ERROR,csumb.addShelf(shelf));
        csumb.addShelf("Mystery");
        assertEquals(true, csumb.getShelves().containsKey("Mystery"));
    }

    @Test
    void getShelf() {
        csumb.addShelf(shelf);
        csumb.addShelf("Mystery");
        assertEquals(csumb.getShelves().get(shelf.getSubject()),csumb.getShelf(1));
        assertEquals(csumb.getShelves().get("Mystery"),csumb.getShelf(2));
        assertEquals(null,csumb.getShelf(3));

    }

    @Test
    void testGetShelf() {
        csumb.addShelf("Mystery");
        assertEquals(csumb.getShelves().get("Mystery"), csumb.getShelf("Mystery"));
        assertEquals(null,csumb.getShelf("Sci-fi"));
    }

    @Test
    void listReaders() {
        csumb.addReader(read);
        csumb.addReader(read2);
        assertEquals(2,csumb.listReaders());
        csumb.removeReader(read2);
        assertEquals(1,csumb.listReaders());

    }

    @Test
    void testListReaders() {
        csumb.addReader(read);
        read.addBook(book);
        assertEquals(1, csumb.listReaders(true));
        //check to make sure the book is listed in the sout of this statement
        System.out.println("Break");
        read.addBook(book2);
        assertEquals(1, csumb.listReaders(true));
        System.out.println("Break");
        //ensures the sout is adding the book I just added
        assertEquals(1, csumb.listReaders(false));
        System.out.println("Break");
        //make sure nothing is printed with this statement

    }

    @Test
    void getReaderByCard() {
        assertEquals(null,csumb.getReaderByCard(4));
        csumb.addReader(read);
        csumb.addReader(read2);
        assertEquals(read2,csumb.getReaderByCard(2));
    }

    @Test
    void addReader() {
        csumb.addReader(read2);
        assertEquals(Code.READER_ALREADY_EXISTS_ERROR,csumb.addReader(read2));
        assertEquals(Code.READER_CARD_NUMBER_ERROR,csumb.addReader(read3));
        assertEquals(Code.SUCCESS,csumb.addReader(read));
        assertEquals(true,csumb.getReaders().contains(read2));
        assertEquals(true,csumb.getReaders().contains(read));
        assertEquals(2,csumb.listReaders(false));

    }

    @Test
    void removeReader() {
     assertEquals(Code.READER_NOT_IN_LIBRARY_ERROR,csumb.removeReader(read3));
        csumb.addReader(read3);
     read3.addBook(book);
     assertEquals(Code.READER_STILL_HAS_BOOKS_ERROR,csumb.removeReader(read3));
     read3.removeBook(book);
     assertEquals(Code.SUCCESS,csumb.removeReader(read3));
    }

    @Test
    void convertInt() {
        assertEquals(12, csumb.convertInt("12",Code.SUCCESS));
        assertEquals(-2,csumb.convertInt("h",Code.BOOK_COUNT_ERROR));
        assertEquals(-8,csumb.convertInt("h",Code.PAGE_COUNT_ERROR));
        assertEquals(-101,csumb.convertInt("h",Code.DATE_CONVERSION_ERROR));
        assertEquals(-999,csumb.convertInt("h",Code.DUE_DATE_ERROR));

    }

    @Test
    void convertDate() {
        LocalDate base = LocalDate.of(1970, Month.JANUARY,1);
        LocalDate workingDate = LocalDate.of(2023,Month.NOVEMBER,19);
        assertEquals(base,csumb.convertDate("2040-11-24-12",Code.SUCCESS));
        assertEquals(base,csumb.convertDate("11-10",Code.SUCCESS));
        assertEquals(workingDate,csumb.convertDate("2023-11-19",Code.SUCCESS));

    }

    @Test
    void getLibraryCardNumber() {
        assertEquals(1,csumb.getLibraryCardNumber());
        csumb.addReader(read);
        assertEquals(2,csumb.getLibraryCardNumber());
    }
}