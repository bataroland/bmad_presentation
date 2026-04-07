package hu.example.library.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "loan_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date loanDate;

    @Column(name = "due_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(name = "return_date")
    @Temporal(TemporalType.DATE)
    private Date returnDate;

    public Loan() {
    }

    /**
     * Calendar-alapú dátumszámítás — tipikus Java 8 előtti minta.
     */
    public Loan(Book book, Member member, int loanDays) {
        this.book = book;
        this.member = member;
        this.loanDate = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(this.loanDate);
        cal.add(Calendar.DAY_OF_MONTH, loanDays);
        this.dueDate = cal.getTime();
    }

    public boolean isOverdue() {
        if (returnDate != null) {
            return false;
        }
        return new Date().after(dueDate);
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
