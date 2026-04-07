package hu.example.library.service;

import hu.example.library.model.Book;
import hu.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findOne(id);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findOne(id);
        if (book == null) {
            return null;
        }
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublishedDate(bookDetails.getPublishedDate());
        return bookRepository.save(book);
    }

    public boolean deleteBook(Long id) {
        Book book = bookRepository.findOne(id);
        if (book == null) {
            return false;
        }
        bookRepository.delete(id);
        return true;
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableTrue();
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchByTitle(keyword);
    }

    /**
     * Könyvek rendezése szerző szerint — anonymous Comparator, Java 8 előtti stílus.
     */
    public List<Book> getBooksSortedByAuthor() {
        List<Book> books = new ArrayList<Book>(bookRepository.findAll());
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                return b1.getAuthor().compareTo(b2.getAuthor());
            }
        });
        return books;
    }
}
