/**
 * This class represents a library object which will contain shelves that contains books
 * @author Dylan Beach
 * @since November 15, 2023
 */

import Utilities.Code;

import java.time.LocalDate;
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
  private List<Reader> readers;
  /**
   * This Hashmap takes in a STting representing a subject, and pairs it with a shelf object
   */
  private HashMap<String, Shelf> shelves = new HashMap<>();
  /**
   * This Hashmap takes in a Book as a key, and paris that with an integer count of those books
   */
  private HashMap<Book, Integer> books = new HashMap<>();

  public Library(String name) {
    this.name = name;
    //System.out.println("Not implemented all the way check");
  }

  public Code init(String filename) {
    System.out.println("Not implemented");
    return Code.NOT_IMPLEMENTED_ERROR;
  }

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
      if(convertDate(words[Book.DUE_DATE_],Code.SUCCESS).equals(null))
      {
        return Code.DATE_CONVERSION_ERROR;
      }
      book = new Book(words[Book.ISBN_],words[Book.TITLE_],words[Book.SUBJECT_],convertInt(words[Book.PAGE_COUNT_],Code.PAGE_COUNT_ERROR),words[Book.AUTHOR_],convertDate(words[Book.DUE_DATE_],Code.SUCCESS));
      addBook(book);
    }
    return Code.SUCCESS;
  }

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
      if (convertInt(words[Shelf.SHELF_NUMBER_], Code.PAGE_COUNT_ERROR) < 1) {
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

  private Code initReader(int readerCount, Scanner scan)
  {
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
       //return
     }
   }
   System.out.println("Not implemented yet");
   return Code.SUCCESS;
  }

  public Code addBook(Book newBook) {
    if (books.containsKey(newBook)) {
      books.put(newBook, books.get(newBook) + 1);
      System.out.println(books.get(newBook) + "copies of " + newBook.getTitle() + " in the stacks");
    } else {
      books.put(newBook, 1);
      System.out.println(newBook.getTitle() + " added to the stacks");

    }
    if(shelves.containsKey(newBook.getSubject()))
    {
      shelves.get(newBook.getSubject()).addBook(newBook);
      return Code.SUCCESS;
    } else {
      System.out.println("No shelf for " + newBook.getSubject() + " books");
      return Code.SHELF_EXISTS_ERROR;
    }
  }

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

  public Code returnBook(Book book) {
    if (shelves.containsKey(book.getSubject())) {
      //could be incorrect
      return shelves.get(book.getSubject()).addBook(book);
    } else {
      System.out.println("No shelf for book " + book);
      return Code.SHELF_EXISTS_ERROR;
    }
  }

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

  public int listBooks() {
    int bookTotal = 0;
    for (String s : shelves.keySet()) {
      for (Book b : shelves.get(s).getBooks().keySet()) {
        bookTotal = bookTotal + shelves.get(s).getBookCount(b);
      }
      shelves.get(s).listBooks();
    }
    for (Book b : books.keySet()) {
      System.out.println("Not implemented yet");
    }
    //System.out.println("Not yet implemented");
    return bookTotal;
  }

  public Code checkoutBook(Reader reader, Book book) {
    if (!readers.contains(reader)) {
      System.out.println(reader.getName() + " doesn't have an account here");
      return Code.READER_NOT_IN_LIBRARY_ERROR;
    }
    if (reader.getBooks().size() > LENDING_LIMIT) {
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
    Code holder2 = shelves.get(book.getSubject()).removeBook(book);
    if (holder2.equals(Code.SUCCESS)) {
      System.out.println(book + " checked out successfully");
    }
    return holder2;
  }

  public Book getBookByISBN(String isbn) {
    for (Book b : books.keySet()) {
      if (b.getISBN().equals(isbn)) {
        return b;
      }
    }
    System.out.println("ERROR: Could not find a book with isbn: " + isbn);
    return null;
  }

  public int listShelves() {
    //listShelves(boolean) not yet implemented
    return listShelves(false);
  }

  public int listShelves(boolean showbooks) {
    int shelfCounter = 0;
    if (showbooks) {
      for (String s : shelves.keySet()) {
        shelves.get(s).listBooks();
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

  public Code addShelf(String shelfSubject) {
    Shelf shelf = new Shelf(shelves.size() + 1, shelfSubject);
    return addShelf(shelf);
  }

  public Code addShelf(Shelf shelf) {
    if (shelves.containsValue(shelf)) {
      System.out.println("ERROR: Shelf already exists " + shelf);
      return Code.SHELF_EXISTS_ERROR;
    }
    shelf.setShelfNumber(shelves.size() + 1);
    shelves.put(shelf.getSubject(), shelf);
    for (Book b : books.keySet()) {
      if (b.getSubject().equals(shelves.get(shelf.getSubject()).getSubject())) {
        shelves.get(shelf.getSubject()).addBook(b);
      }
    }
    return Code.SUCCESS;
  }

  public Shelf getShelf(Integer shelfNumber) {
    for (String s : shelves.keySet()) {
      if (Integer.valueOf(shelves.get(s).getShelfNumber()).equals(shelfNumber)) {
        return shelves.get(s);
      }
    }
    System.out.println("No shelf number " + shelfNumber + " found");
    return null;
  }

  public Shelf getShelf(String subject) {
    if (shelves.containsKey(subject)) {
      return shelves.get(subject);
    } else {
      System.out.println("No shelf for " + subject + "books");
      return null;
    }
  }

  public int listReaders() {
    int totalReaders = 0;
    for (Reader r : readers) {
      System.out.println(r.toString());
      totalReaders = totalReaders + 1;
    }
    return totalReaders;
  }

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

  public Reader getReaderByCard(int cardNumber) {
    for (Reader r : readers) {
      if (r.getCardNumber() == cardNumber) {
        return r;
      }
    }
    System.out.println("Could not find a reader with card #" + cardNumber);
    return null;
  }

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

  public static int convertInt(String recordCountString, Code code) {
    int convertedString;
    try {
      convertedString = Integer.parseInt(recordCountString);
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
      } else {
        System.out.println("Value which caused the error: " + recordCountString);
        System.out.println("Error: Unknown conversion error");
        return Code.UNKNOWN_ERROR.getCode();
      }
    }
    return convertedString;
  }

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

  public int getLibraryCardNumber()
  {
    return libraryCard+1;
  }

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static int getLibraryCard() {
    return libraryCard;
  }

  public static void setLibraryCard(int libraryCard) {
    Library.libraryCard = libraryCard;
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