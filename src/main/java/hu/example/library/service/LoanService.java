package hu.example.library.service;

import hu.example.library.model.Book;
import hu.example.library.model.Loan;
import hu.example.library.model.Member;
import hu.example.library.repository.BookRepository;
import hu.example.library.repository.LoanRepository;
import hu.example.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LoanService {

    private static final int DEFAULT_LOAN_DAYS = 14;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public Loan borrowBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            throw new IllegalArgumentException("Book not found: " + bookId);
        }
        if (!book.getAvailable()) {
            throw new IllegalStateException("Book is not available: " + book.getTitle());
        }

        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            throw new IllegalArgumentException("Member not found: " + memberId);
        }
        if (!member.getActive()) {
            throw new IllegalStateException("Member is not active: " + member.getName());
        }

        Loan loan = new Loan(book, member, DEFAULT_LOAN_DAYS);
        book.setAvailable(false);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElse(null);
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found: " + loanId);
        }
        if (loan.isReturned()) {
            throw new IllegalStateException("Book already returned");
        }

        loan.setReturnDate(new Date());
        loan.getBook().setAvailable(true);
        bookRepository.save(loan.getBook());

        return loanRepository.save(loan);
    }

    public List<Loan> getActiveLoans() {
        return loanRepository.findByReturnDateIsNull();
    }

    /**
     * Lejárt kölcsönzések — iteráció + manuális szűrés, stream nélkül.
     */
    public List<Loan> getOverdueLoans() {
        List<Loan> activeLoans = loanRepository.findByReturnDateIsNull();
        List<Loan> overdueLoans = new ArrayList<Loan>();
        for (Loan loan : activeLoans) {
            if (loan.isOverdue()) {
                overdueLoans.add(loan);
            }
        }
        return overdueLoans;
    }

    public List<Loan> getMemberLoans(Long memberId) {
        return loanRepository.findByMemberId(memberId);
    }
}
