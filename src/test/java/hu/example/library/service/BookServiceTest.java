package hu.example.library.service;

import hu.example.library.model.Book;
import hu.example.library.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @Before
    public void setUp() {
        sampleBook = new Book("Clean Code", "Robert C. Martin", "9780132350884", new Date());
        sampleBook.setId(1L);
    }

    @Test
    public void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(sampleBook));

        List<Book> books = bookService.getAllBooks();

        assertEquals(1, books.size());
        assertEquals("Clean Code", books.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testGetBookById() {
        when(bookRepository.findOne(1L)).thenReturn(sampleBook);

        Book found = bookService.getBookById(1L);

        assertNotNull(found);
        assertEquals("Clean Code", found.getTitle());
    }

    @Test
    public void testGetBookByIdNotFound() {
        when(bookRepository.findOne(99L)).thenReturn(null);

        Book found = bookService.getBookById(99L);

        assertNull(found);
    }

    @Test
    public void testCreateBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        Book created = bookService.createBook(sampleBook);

        assertNotNull(created);
        assertEquals("Clean Code", created.getTitle());
        verify(bookRepository, times(1)).save(sampleBook);
    }

    @Test
    public void testDeleteBookSuccess() {
        when(bookRepository.findOne(1L)).thenReturn(sampleBook);
        doNothing().when(bookRepository).delete(1L);

        boolean deleted = bookService.deleteBook(1L);

        assertTrue(deleted);
    }

    @Test
    public void testDeleteBookNotFound() {
        when(bookRepository.findOne(99L)).thenReturn(null);

        boolean deleted = bookService.deleteBook(99L);

        assertFalse(deleted);
    }
}
