package hu.example.library.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String title;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String author;

    @Column(unique = true, length = 13)
    private String isbn;

    @Column(name = "published_date")
    @Temporal(TemporalType.DATE)
    private Date publishedDate;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(nullable = false)
    private Boolean available = true;

    public Book() {
    }

    public Book(String title, String author, String isbn, Date publishedDate) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
        this.available = true;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

    /**
     * Formázott dátum — tipikus Java 8-as minta, amit java.time-ra kéne cserélni.
     */
    public String getFormattedPublishedDate() {
        if (publishedDate == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(publishedDate);
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
