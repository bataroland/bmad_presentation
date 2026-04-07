package hu.example.library.service;

import hu.example.library.model.Book;
import hu.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
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
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        Book found = bookService.getBookById(1L);

        assertNotNull(found);
        assertEquals("Clean Code", found.getTitle());
    }

    @Test
    public void testGetBookByIdNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

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
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        doNothing().when(bookRepository).deleteById(1L);

        boolean deleted = bookService.deleteBook(1L);

        assertTrue(deleted);
    }

    @Test
    public void testDeleteBookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        boolean deleted = bookService.deleteBook(99L);

        assertFalse(deleted);
    }
}
