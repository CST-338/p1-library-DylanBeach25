/**
 * This class represents a library object which will contain shelves that contains books
 * @author Dylan Beach
 * @since November 15, 2023
 */

import Utilities.Code;

import java.util.HashMap;
import java.util.List;

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
    System.out.println("Not implemented all the way check");
  }

  public Code init(String filename) {

    System.out.println("Not implemented");
    return Code.NOT_IMPLEMENTED_ERROR;
  }

  public Code addBook(Book newBook) {
    if (books.containsKey(newBook)) {
      books.put(newBook, books.get(newBook) + 1);
      System.out.println(books.get(newBook) + "copies of " + newBook.getTitle() + " in the stacks");
    } else {
      books.put(newBook, 1);
      System.out.println(newBook.getTitle() + " added to the stacks");

    }
    System.out.println("Not finished implementing");
    return Code.SUCCESS;
    //come back to after shelf
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
      for(Book b : shelves.get(s).getBooks().keySet())
      {
        bookTotal = bookTotal + shelves.get(s).getBookCount(b);
      }
      shelves.get(s).listBooks();
    }
    for(Book b:books.keySet())
    {
      System.out.println("Not implemented yet");
    }
    //System.out.println("Not yet implemented");
    return bookTotal;
  }

  public Code checkoutBook(Reader reader, Book book)
  {
    if(!readers.contains(reader))
    {
      System.out.println(reader.getName() + " doesn't have an account here");
      return Code.READER_NOT_IN_LIBRARY_ERROR;
    }
    if(reader.getBooks().size()>LENDING_LIMIT)
    {
      System.out.println(reader.getName() + " has reached the lending limit, " + LENDING_LIMIT);
      return Code.BOOK_LIMIT_REACHED_ERROR;
    }
    if(!books.containsKey(book))
    {
      System.out.println("ERROR: could not find " + book.getTitle());
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }
    if(!shelves.containsKey(book.getSubject()))
    {
      System.out.println("no shelf for " + book.getSubject() + " books!");
      return Code.SHELF_EXISTS_ERROR;
    } else {
      if(shelves.get(book.getSubject()).getBookCount(book) < 1)
      {
        System.out.println("ERROR: no copies of " + book + " remain");
        return Code.BOOK_NOT_IN_INVENTORY_ERROR;
      }
    }
    Code holder = reader.addBook(book);
    if(!holder.equals(Code.SUCCESS))
    {
      System.out.println("Couldn't checkout " + book);
      return holder;
    }
    Code holder2 = shelves.get(book.getSubject()).removeBook(book);
    if(holder2.equals(Code.SUCCESS))
    {
      System.out.println(book + " checked out successfully");
    }
    return holder2;
  }

  public Book getBookByISBN(String isbn)
  {
    for(Book b : books.keySet())
    {
      if(b.getISBN().equals(isbn))
      {
        return b;
      }
    }
    System.out.println("ERROR: Could not find a book with isbn: " + isbn);
    return null;
  }

  public int listShelves()
  {
    //listShelves(boolean) not yet implemented
    return listShelves(false);
  }

  public int listShelves(boolean showbooks)
  {
    int shelfCounter = 0;
    if(showbooks)
    {
      for(String s : shelves.keySet())
      {
        shelves.get(s).listBooks();
        shelfCounter = shelfCounter + 1;
      }
    } else {
      for(String s : shelves.keySet())
      {
        System.out.println(shelves.get(s).toString());
        shelfCounter = shelfCounter + 1;
      }
    }
    return shelfCounter;
  }












}