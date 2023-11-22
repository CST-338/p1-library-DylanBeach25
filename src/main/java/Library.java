/**
 * This class represents a library object which will contain shelves that contains books
 * @author Dylan Beach
 * @since November 15, 2023
 */

import Utilities.Code;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Library {
  /**
   * This constant holds the max books that can be checked out by a reader at a time
   */
  public static final int LENDING_LIMIT = 5;
  /**
   * This string holds the name of the library
   */
  private String name;
  /**
   * This static int holds the current max library card number
   */
  private static int libraryCard;
  /**
   * This list holds a list of readers registered to the library
   */
  private List<Reader> readers = new ArrayList<>();
  /**
   * This Hashmap takes in a String representing a subject, and pairs it with a shelf object
   */
  private HashMap<String, Shelf> shelves = new HashMap<>();
  /**
   * This Hashmap takes in a Book as a key, and paris that with an integer count of those books
   */
  private HashMap<Book, Integer> books = new HashMap<>();

  /**
   * Constructor takes in a string as parameter and sets the instance field name to it
   * @param name
   */
  public Library(String name) {
    this.name = name;
  }

  /**
   * init does the bulk of the processing for the program. It takes a csv file in
   * in the form of a String filename parameter. It then opens that file and reads through
   * the lines to determine how many books, shelves, and readers to process respectively. Once
   * it knows how many of each object to process it calls their own respective init methods listed below and passess
   * the location of the scanner reading the file for hte other methods to continue where it left off.
   * If an error occurs while parsing these, books, shelves, or readers, the program returns that error. Otherwise
   * It will populate the library with the information in the csv
   * @param filename
   * @return
   */
  public Code init(String filename) {
    Scanner scan = null;
    Code book1;
    Code shelf1;
    Code reader1;
    File f = new File(filename);
    try {
      scan = new Scanner(f);
    } catch (FileNotFoundException e) {
      return Code.FILE_NOT_FOUND_ERROR;
    }
    //This is incorrect, it needs to know what error to take into the convertInt
    int bookParse = convertInt(scan.nextLine().trim(), Code.BOOK_COUNT_ERROR);
    if (bookParse < 0) {
      for (Code code : Code.values()) {
        if (code.getCode() == bookParse) {
          return code;
        }
      }
      return Code.UNKNOWN_ERROR;
    }
    book1 = initBooks(bookParse, scan);
    if (!book1.equals(Code.SUCCESS)) {
      return book1;
    }
    listBooks();
    int shelvesParse = convertInt(scan.nextLine().trim(), Code.SHELF_COUNT_ERROR);
    if (shelvesParse < 0) {
      for (Code code : Code.values()) {
        if (code.getCode() == shelvesParse) {
          return code;
        }
      }
      return Code.UNKNOWN_ERROR;
    }
    shelf1 = initShelves(shelvesParse, scan);
    if (!shelf1.equals(Code.SUCCESS)) {
      return shelf1;
    }
    listShelves();
    int readerParse = convertInt(scan.nextLine().trim(), Code.READER_COUNT_ERROR);
    if (readerParse < 0) {
      for (Code code : Code.values()) {
        if (code.getCode() == readerParse) {
          return code;
        }
      }
      return Code.UNKNOWN_ERROR;
    }
    reader1 = initReader(readerParse, scan);
    if (!reader1.equals(Code.SUCCESS)) {
      return reader1;
    }
    listReaders();

    //if(!book1.equals(Code.SUCCESS))
    //{
    //return book1;
    //} else if(!shelf1.equals((Code.SUCCESS))) {
    // return shelf1;
    //} else if(!reader1.equals(Code.SUCCESS)) {
    // return reader1;
    //} else {
    // return Code.SUCCESS;
  //}
    return Code.SUCCESS;

}

  /**
   * initBooks processes the books part of the csv, ensuring that it has enough fields
   * to create a book object once the line is split into an array of words. Values that need
   * integers are converetd from Strings to int using the convertInt method, with any errors
   * due to a numberformat exception being returned. It adds all of the books contained in the csv
   * to the books hashmap
   * @param bookCount
   * @param scan
   * @return
   */
  private Code initBooks(int bookCount, Scanner scan)
  {
    Book book;
    if(bookCount < 1)
    {
      return Code.LIBRARY_ERROR;
    }
    for (int i = 0; i < bookCount; i++)
    {
      String line = scan.nextLine().trim();
      String[] words = line.split(",");
      if(words.length < 6)
      {
        return Code.BOOK_RECORD_COUNT_ERROR;
      }
      if(convertInt(words[Book.PAGE_COUNT_],Code.PAGE_COUNT_ERROR) < 1)
      {
        return Code.PAGE_COUNT_ERROR;
      }
      if(convertDate(words[Book.DUE_DATE_],Code.SUCCESS)==null)
      {
        return Code.DATE_CONVERSION_ERROR;
      }
      book = new Book(words[Book.ISBN_],words[Book.TITLE_],words[Book.SUBJECT_],convertInt(words[Book.PAGE_COUNT_],Code.PAGE_COUNT_ERROR),words[Book.AUTHOR_],convertDate(words[Book.DUE_DATE_],Code.SUCCESS));
      addBook(book);
    }
    return Code.SUCCESS;
  }

  /**
   * init shelves takes the location of the scanner and processes the shelves in the
   * taken in csv file. It converts the shelf number to a string and passes it to the shelf constructor
   * and takes the subject in as well. It calls addShelf to each shelf created which adds them to the
   * shelves hashmap.
   * @param shelfCount
   * @param scan
   * @return
   */
  private Code initShelves(int shelfCount, Scanner scan)
  {
    Shelf shelf;
    if(shelfCount < 1)
    {
      return Code.SHELF_COUNT_ERROR;
    }
    for(int i = 0; i < shelfCount;i++) {
      String line = scan.nextLine().trim();
      String[] words = line.split(",");
      if (words.length < 2) {
        return Code.BOOK_RECORD_COUNT_ERROR;
      }
      if (convertInt(words[Shelf.SHELF_NUMBER_], Code.SHELF_NUMBER_PARSE_ERROR) < 1) {
        return Code.SHELF_NUMBER_PARSE_ERROR;
      } else {
        shelf = new Shelf(convertInt(words[Shelf.SHELF_NUMBER_],Code.PAGE_COUNT_ERROR),words[Shelf.SUBJECT_]);
        addShelf(shelf);
      }
    }
    if(shelves.size()!=shelfCount)
    {
      System.out.println("Number of shelves doesn't match expected");
      return Code.SHELF_NUMBER_PARSE_ERROR;
    }
    return Code.SUCCESS;
  }

  /**
   * initReader works very similar to the previous too, however processes the information
   * provided about each reader. The difference with this method, is that each reader
   * may be in possession of books of their own. In this case the program, reads how many
   * books they are in possession of and begins to process them. It matches teh isbn with
   * isbns of books present in the library system. If the library has them then the book is checked
   * out to the reader and the remaining books are processed.
   * @param readerCount
   * @param scan
   * @return
   */
  private Code initReader(int readerCount, Scanner scan)
  {
    Reader read;
    int indexHolder;
    Book checkoutBook;
    Book foundBook;
   if(readerCount < 1)
   {
     return Code.READER_COUNT_ERROR;
   }
   for(int i = 0; i < readerCount; i++)
   {
     String line = scan.nextLine().trim();
     String[] words = line.split(",");

     if(words.length < 5)
     {
       return Code.BOOK_RECORD_COUNT_ERROR;
     }
     if(convertInt(words[Reader.CARD_NUMBER_], Code.PAGE_COUNT_ERROR) < 1)
     {
       return Code.READER_CARD_NUMBER_ERROR;
     }
     read = new Reader(convertInt(words[Reader.CARD_NUMBER_],Code.PAGE_COUNT_ERROR),words[Reader.NAME_],words[Reader.PHONE_]);
     addReader(read);
     indexHolder = Reader.BOOK_START_;
     //needs to multiply by 2 since each book has a due date and title
     for(int j = 0; j < convertInt(words[Reader.BOOK_COUNT_],Code.PAGE_COUNT_ERROR);j++)
     {
       //this is for the case in Library01, where a book count of 2 is provided, but with only one book
       if(words.length-(2*(convertInt(words[Reader.BOOK_COUNT_],Code.PAGE_COUNT_ERROR))) < 4)
       {
         System.out.println("Not enough books provided for book count");
         break;
       }
       //System.out.println(Reader.BOOK_START_);
       //System.out.println(indexHolder);
       if(getBookByISBN(words[indexHolder])==null)
       {
         System.out.println("Error");
       } else {
         foundBook = getBookByISBN(words[indexHolder]);
         checkoutBook = new Book(foundBook.getISBN(),foundBook.getTitle(), foundBook.getSubject(), foundBook.getPageCount(), foundBook.getAuthor(),convertDate(words[indexHolder+1],Code.SUCCESS));
         checkoutBook(read,checkoutBook);
       }
       indexHolder = indexHolder + 2;
     }
   }
   return Code.SUCCESS;
  }

  /**
   * Add book takes a book in as a parameter and adds it to the hashmap of books
   * If the book was already present in the hashmap its integer value is incremented by 1
   * if not it is placed as a new key in the hashmap with a value of 1. If any errors occur
   * they are returned as a code.
   * @param newBook
   * @return Code
   */
  public Code addBook(Book newBook) {
    if (books.containsKey(newBook)) {
      books.put(newBook, books.get(newBook) + 1);
      System.out.println(books.get(newBook) + " copies of " + newBook.getTitle() + " in the stacks");
    } else {
      books.put(newBook, 1);
      System.out.println(newBook.getTitle() + " added to the stacks");
    }
    if(shelves.containsKey(newBook.getSubject()))
    {
      shelves.get(newBook.getSubject()).addBook(newBook);
      //this was an earlier test
      //books.put(newBook,books.get(newBook) - 1);
      return Code.SUCCESS;
    } else {
      System.out.println("No shelf for " + newBook.getSubject() + " books");
      return Code.SHELF_EXISTS_ERROR;
    }
  }

  /**
   * returnBook takes a Reader and a Book as a parameter. It makes sure the reader
   * contains the book provided, as well as that the library has the book. If either of tehse
   * is false their respective errosr are returned, otherwise. The book is removed
   * from the reader using the removeBook method, and the returnBook(book) method is called with
   * the book being returned a sa parameter
   * @param reader
   * @param book
   * @return
   */
  public Code returnBook(Reader reader, Book book) {
    if (!reader.getBooks().contains(book)) {
      System.out.println(reader.getName() + " doesn't have " + book.getTitle() + " checked out");
      return Code.READER_DOESNT_HAVE_BOOK_ERROR;
    }
    if (!this.books.containsKey(book)) {
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    } else {
      System.out.println(reader.getName() + " is returning " + book.getTitle());
      //might have to run it twice not sure
      Code stored = reader.removeBook(book);
      if (stored.equals(Code.SUCCESS)) {
        return returnBook(book);

      } else {
        System.out.println("Could not return " + book);
        return stored;
      }
    }
  }

  /**
   * the returnBook method makes sure there is a shelf for the book nad returns
   * it to that shelf. Otherwise it returns a SHELF_EXISTS_ERROR
   * @param book
   * @return Code
   */
  public Code returnBook(Book book) {
    if (shelves.containsKey(book.getSubject())) {
      //could be incorrect
      return shelves.get(book.getSubject()).addBook(book);
    } else {
      System.out.println("No shelf for book " + book);
      return Code.SHELF_EXISTS_ERROR;
    }
  }

  /**
   * addBookToShelf makes sure to take a Book and a SHelf as parameters. If the
   * subjects of the two parameters match then the book is added to the shelf.
   * Otherwise if there is a problem the respective error code is returned
   * @param book
   * @param shelf
   * @return Code
   */
  private Code addBookToShelf(Book book, Shelf shelf) {
    Code holder;
    if (returnBook(book).equals(Code.SUCCESS)) {
      return Code.SUCCESS;
    }
    if (!book.getSubject().equals(shelf.getSubject())) {
      return Code.SHELF_SUBJECT_MISMATCH_ERROR;
    } else {
      holder = shelf.addBook(book);
    }
    if (holder.equals(Code.SUCCESS)) {
      System.out.println(book.getTitle() + " added to shelf");
      return Code.SUCCESS;
    } else {
      System.out.println("Could not add " + book + " to shelf");
      return holder;
    }
  }

  /**
   * listBooks lists all of teh books in the library and returns an int
   * representing how many there are
   * @return int
   */
  public int listBooks() {
    int bookTotal = 0;
   // for (String s : shelves.keySet()) {
     // for (Book b : shelves.get(s).getBooks().keySet()) {
       // bookTotal = bookTotal + shelves.get(s).getBookCount(b);
        //if(shelves.get(s).getBookCount(b)>0) {
         // System.out.println(shelves.get(s).getBookCount(b) + " copies of " + b.getTitle() + " by " + b.getAuthor() + " ISBN: " + b.getISBN());
        //}
      //}
      //shelves.get(s).listBooks();

    //}
    //test here
   // System.out.println("Shelves done");
    for (Book b : books.keySet()) {
      bookTotal = bookTotal + books.get(b);
      if(books.get(b)>0) {
        System.out.println(books.get(b) + " copies of " + b.getTitle() + " by " + b.getAuthor() + " ISBN: " + b.getISBN());
      }
      //System.out.println("Not implemented yet");
      //books.get(b)
    }
    //System.out.println("Not yet implemented");
    return bookTotal;
  }

  /**
   * checkoutBook takes a Reader and a Book as a parameter. It checks to make sure the reader is a part
   * of the library, if not an error is returned. It then checks to see if the reader provided
   * is over the lending limit for books, if so an error is returned. It then checks to see if the
   * library contians the book that is being checked out, if not an error is returned. It then
   * checks to see if a shelf exists for the book being checked out, if not an error is returned.
   * It makes sure there is more than 0 copies available of teh book to checkout. If all of this is true
   * then the books is added to the reader with teh addBook method, and a SUCCESS code is returned
   * @param reader
   * @param book
   * @return
   */
  public Code checkoutBook(Reader reader, Book book) {
    if (!readers.contains(reader)) {
      System.out.println(reader.getName() + " doesn't have an account here");
      return Code.READER_NOT_IN_LIBRARY_ERROR;
    }
    if (reader.getBooks().size() >= LENDING_LIMIT) {
      System.out.println(reader.getName() + " has reached the lending limit, " + LENDING_LIMIT);
      return Code.BOOK_LIMIT_REACHED_ERROR;
    }
    if (!books.containsKey(book)) {
      System.out.println("ERROR: could not find " + book.getTitle());
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }
    if (!shelves.containsKey(book.getSubject())) {
      System.out.println("no shelf for " + book.getSubject() + " books!");
      return Code.SHELF_EXISTS_ERROR;
    } else {
      if (shelves.get(book.getSubject()).getBookCount(book) < 1) {
        System.out.println("ERROR: no copies of " + book + " remain");
        return Code.BOOK_NOT_IN_INVENTORY_ERROR;
      }
    }
    Code holder = reader.addBook(book);
    if (!holder.equals(Code.SUCCESS)) {
      System.out.println("Couldn't checkout " + book);
      return holder;
    }
    System.out.println(shelves.get(book.getSubject()).getBookCount(book));
    Code holder2 = shelves.get(book.getSubject()).removeBook(book);
    if (holder2.equals(Code.SUCCESS)) {
      System.out.println(book + " checked out successfully");
      //test below
      System.out.println("Book: " + book.getTitle() + " " + shelves.get(book.getSubject()).getBookCount(book)+ " remaining");
    }
    return holder2;
  }

  /**
   * getBook by isbn searches through every key in the books keyset to see if anyof them
   * match the parameter of the isbn string. If they do it returns that book, otherwise
   * it returns null.
   * @param isbn
   * @return
   */
  public Book getBookByISBN(String isbn) {
    for (Book b : books.keySet()) {
      if (b.getISBN().equals(isbn)) {
        return b;
      }
    }
    System.out.println("ERROR: Could not find a book with isbn: " + isbn);
    return null;
  }

  /**
   * listShelves returns an int that is gained from calling listShelves(false)
   * @return int
   */
  public int listShelves() {
    return listShelves(false);
  }

  /**
   * listShelves takes a boolean showbooks. if it is true, then the books in each shelf are printed
   * along with the shelves themselves. If it is false just the information about each shelf is
   * printed. It returns the amount of shelfs present in a library.
   * @param showbooks
   * @return
   */
  public int listShelves(boolean showbooks) {
    int shelfCounter = 0;
    if (showbooks) {
      for (String s : shelves.keySet()) {
        //System.out.println("Books in shelf " + s);
        System.out.println(shelves.get(s).listBooks());
        shelfCounter = shelfCounter + 1;
      }
    } else {
      for (String s : shelves.keySet()) {
        System.out.println(shelves.get(s).toString());
        shelfCounter = shelfCounter + 1;
      }
    }
    return shelfCounter;
  }

  /**
   * addShelf takes a String parameter and creates a Shelf object with the shelf constructor
   * passing the shelfSubject parameter into it.
   * @param shelfSubject
   * @return
   */
  public Code addShelf(String shelfSubject) {
    Shelf shelf = new Shelf(shelves.size() + 1, shelfSubject);
    return addShelf(shelf);
  }

  /**
   * addShelf takes in a shelf parameter and checks if this shelf is already present
   * in the library. If not it adds the shelf to teh library then goes through all the books
   * in the library and if they match the subject of hte shelf that was just added
   * those books are added to the shelf. IF something prevents the code from running
   * an error Code is returned.
   * @param shelf
   * @return Code
   */
  public Code addShelf(Shelf shelf) {
    if (shelves.containsValue(shelf)) {
      System.out.println("ERROR: Shelf already exists " + shelf);
      return Code.SHELF_EXISTS_ERROR;
    }
    shelf.setShelfNumber(shelves.size() + 1);
    shelves.put(shelf.getSubject(), shelf);
    for (Book b : books.keySet()) {
      //if (b.getSubject().equals(shelves.get(shelf.getSubject()).getSubject())) {
      if(b.getSubject().equals(shelf.getSubject())) {
        //for loop was added for test
        for (int j = 0; j < books.get(b); j++) {
          shelves.get(shelf.getSubject()).addBook(b);
          //this is a test
          //System.out.println("J value for add shelf: " + j);

          //books.put(b,books.get(b)-1);
        }
      }

    }
    return Code.SUCCESS;
  }

  /**
   * getShelf takes a Shelf Number as a parameter and searches through the shelves hashmap
   * for a shelf that matches the shelfNumber. If it finds one it returns that shelf object, if
   * not it returns null and prints that no shelf number matches the parameter.
   * @param shelfNumber
   * @return Shelf
   */
  public Shelf getShelf(Integer shelfNumber) {
    for (String s : shelves.keySet()) {
      if (Integer.valueOf(shelves.get(s).getShelfNumber()).equals(shelfNumber)) {
        return shelves.get(s);
      }
    }
    System.out.println("No shelf number " + shelfNumber + " found");
    return null;
  }

  /**
   * getShelf takes in a String as a parameter and searches the shelves hashmap for a
   * shelf that matches the subject. If it finds one that shelf is returned, otherwise null is
   * returned.
   * @param subject
   * @return Shelf
   */
  public Shelf getShelf(String subject) {
    if (shelves.containsKey(subject)) {
      return shelves.get(subject);
    } else {
      System.out.println("No shelf for " + subject + "books");
      return null;
    }
  }

  /**
   * listReaders lists all of the readers present in the library system and
   * returns the amount of readers in the system.
   * @return int.
   */
  public int listReaders() {
    int totalReaders = 0;
    for (Reader r : readers) {
      System.out.println(r.toString());
      totalReaders = totalReaders + 1;
    }
    return totalReaders;
  }

  /**
   * listReaders takes in a boolean that determines if the books are shown or not. If the parameter
   * taken in is true the reader as well as all books they have are printed out. If the parameter is false,
   * then only the readers and their information is printed out.
   * @param showBooks
   * @return
   */
  public int listReaders(boolean showBooks) {
    int totalReaders = 0;
    if (showBooks) {
      for (Reader r : readers) {
        totalReaders = totalReaders + 1;
        System.out.println(r.getName() + "(#" + readers.indexOf(r) + 1 + ") has the following books: ");
        for (Book b : r.getBooks()) {
          System.out.println(b.toString());
        }
      }
      return totalReaders;
    } else {
      for (Reader r : readers) {
        totalReaders = totalReaders + 1;
        System.out.println(r.toString());
      }
      return totalReaders;
    }
  }

  /**
   * getReader by card takes an int as a parameter representing a card number. It searches
   * through teh readers list for a reader that contains that card number. If a reader is
   * found to have a matching card number that reader is returned, otherwise null is returned.
   * @param cardNumber
   * @return
   */
  public Reader getReaderByCard(int cardNumber) {
    for (Reader r : readers) {
      if (r.getCardNumber() == cardNumber) {
        return r;
      }
    }
    System.out.println("Could not find a reader with card #" + cardNumber);
    return null;
  }

  /**
   * addReader takes a reader as a parameter and tries to add this reader to the
   * list of readers in the library. If the reader already exists an error is returned.
   * If the reader has a card number that is already given to a different reader, an
   * error is returned. Otherwise a SUCCESS code is returned and the reader is added
   * to the readers list.
   * @param reader
   * @return Code
   */
  public Code addReader(Reader reader) {
    if (readers.contains(reader)) {
      System.out.println(reader.getName() + " already has an account!");
      return Code.READER_ALREADY_EXISTS_ERROR;
    }
    for (Reader r : readers) {
      if (reader.getCardNumber() == r.getCardNumber()) {
        System.out.println(r.getName() + " and " + reader.getName() + " have the same card number!");
        return Code.READER_CARD_NUMBER_ERROR;
      }
    }
    readers.add(reader);
    System.out.println(reader.getName() + " added to the library!");
    if (reader.getCardNumber() > libraryCard) {
      libraryCard = reader.getCardNumber();
    }
    return Code.SUCCESS;
  }

  /**
   * removeReader takes a reader as a parameter. It makes sure the reader has ont books,
   * as well as that the reader is part of the library. If either of these are false their
   * respective errors are returned. Otherwise the reader is removed from tehe readers
   * list in the library.
   * @param reader
   * @return
   */
  public Code removeReader(Reader reader) {
    if (!reader.getBooks().isEmpty()) {
      System.out.println(reader.getName() + " must return all books!");
      return Code.READER_STILL_HAS_BOOKS_ERROR;
    }
    if (!readers.contains(reader)) {
      System.out.println(reader.getName() + " is not part of this Library");
      return Code.READER_NOT_IN_LIBRARY_ERROR;
    }
    readers.remove(reader);
    return Code.SUCCESS;
  }

  /**
   * convertInt takes in a String as well as a Code as a parameter. It then
   * tries to convert the string provided to an int using Integer.parseInt. If
   * there is a NumberFormatException it catches it and returns a message and code
   * matches the code that was passed in. If the code isn't provided in the table of
   * codes with message responses in the if statements below, and unknown error code
   * is returned. If it is successful the converted int is returned
   * @param recordCountString
   * @param code
   * @return int
   */
  public static int convertInt(String recordCountString, Code code) {
    int convertedString;
    try {
      convertedString = Integer.parseInt(recordCountString.trim());
    } catch (NumberFormatException e) {
      if (code.equals(Code.BOOK_COUNT_ERROR)) {
        System.out.println("Value which caused the error: " + recordCountString);
        System.out.println("Error: Could not read number of books");
        return Code.BOOK_COUNT_ERROR.getCode();
      } else if (code.equals(Code.PAGE_COUNT_ERROR)) {
        System.out.println("Value which caused the error: " + recordCountString);
        System.out.println("Error: Could not parse page count");
        return Code.PAGE_COUNT_ERROR.getCode();
      } else if (code.equals(Code.DATE_CONVERSION_ERROR)) {
        System.out.println("Value which caused the error: " + recordCountString);
        System.out.println("Error: Could not parse date component");
        return Code.DATE_CONVERSION_ERROR.getCode();
      } else if (code.equals(Code.SHELF_COUNT_ERROR)) {
        System.out.println("Value which caused the error: " + recordCountString);
        System.out.println("Error: Could read shelf count");
        return Code.SHELF_COUNT_ERROR.getCode();
      } else if (code.equals(Code.SHELF_NUMBER_PARSE_ERROR)) {
        System.out.println("Value which caused the error: " + recordCountString);
        System.out.println("Error: Could not parse shelf number");
        return Code.SHELF_NUMBER_PARSE_ERROR.getCode();
      }
      {
        System.out.println("Value which caused the error: " + recordCountString);
        System.out.println("Error: Unknown conversion error");
        return Code.UNKNOWN_ERROR.getCode();
      }
    }
    return convertedString;
  }

  /**
   * LocalDate takes a string and a Code as a parameter. It converts the string
   * to a Local date matching the format of "yyyy-mm-dd" If there is an error
   * with the value provided to convert, a default date of "1970-01-01" is returned.
   * Otherwise the converted date is returned.
   * @param date
   * @param errorCode
   * @return LocalDate
   */
  public static LocalDate convertDate(String date, Code errorCode)
  {
    LocalDate localDate;
    String[] words = date.split("-");
    if(words.length != 3)
    {
      System.out.println("Error: date conversion error, could not parse " + date);
      System.out.println("Using default date (01-jan-1970)");
      localDate = LocalDate.parse("1970-01-01");
      return localDate;
    }
    if(words[0].length() < 0 || words[1].length()<0 || words[2].length()<0)
    {
      //Perhaps fix this
      System.out.println("Error converting date: Year " + words[0]);
      System.out.println("Error converting date: Month " + words[1]);
      System.out.println("Error converting date: Day " + words[2]);
      System.out.println("Using default date (01-jan-1970)");
      localDate = LocalDate.parse("1970-01-01");
      return localDate;
    }
    localDate = LocalDate.parse(date);
    return localDate;
  }

  /**
   * getLibraryCardNumber returns libraryCard+1
   * @return int
   */
  public int getLibraryCardNumber()
  {
    return libraryCard+1;
  }

  /**
   * errorCode is unused, however it would take in an int, and
   * match that int with its respective Code enum to retrieve the error code.
   * @param codeNumber
   * @return
   */
  private Code errorCode(int codeNumber)
  {
    for(Code code : Code.values())
    {
      if(code.getCode() == codeNumber) {
        return code;
      }
    }
    return Code.UNKNOWN_ERROR;
  }
//Everything under here is standard getters and setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Reader> getReaders() {
    return readers;
  }

  public void setReaders(List<Reader> readers) {
    this.readers = readers;
  }

  public HashMap<String, Shelf> getShelves() {
    return shelves;
  }

  public void setShelves(HashMap<String, Shelf> shelves) {
    this.shelves = shelves;
  }

  public HashMap<Book, Integer> getBooks() {
    return books;
  }

  public void setBooks(HashMap<Book, Integer> books) {
    this.books = books;
  }
}