package hu.example.library.controller;

import hu.example.library.model.Book;
import hu.example.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    public void testGetAllBooks() throws Exception {
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884", LocalDate.of(2008, 8, 1));
        book.setId(1L);

        given(bookService.getAllBooks()).willReturn(Arrays.asList(book));

        mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Clean Code")));
    }

    @Test
    public void testGetBookByIdNotFound() throws Exception {
        given(bookService.getBookById(99L)).willReturn(null);

        mockMvc.perform(get("/api/books/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
