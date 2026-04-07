package hu.example.library.repository;

import hu.example.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthor(String author);

    Book findByIsbn(String isbn);

    List<Book> findByAvailableTrue();

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchByTitle(@Param("keyword") String keyword);
}
