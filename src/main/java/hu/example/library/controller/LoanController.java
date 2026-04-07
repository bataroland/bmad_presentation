package hu.example.library.controller;

import hu.example.library.model.Loan;
import hu.example.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestParam Long bookId, @RequestParam Long memberId) {
        try {
            Loan loan = loanService.borrowBook(bookId, memberId);
            return new ResponseEntity<Loan>(loan, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", e.getMessage());
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", e.getMessage());
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        try {
            Loan loan = loanService.returnBook(id);
            return new ResponseEntity<Loan>(loan, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", e.getMessage());
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", e.getMessage());
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/active")
    public List<Loan> getActiveLoans() {
        return loanService.getActiveLoans();
    }

    @GetMapping("/overdue")
    public List<Loan> getOverdueLoans() {
        return loanService.getOverdueLoans();
    }

    @GetMapping("/member/{memberId}")
    public List<Loan> getMemberLoans(@PathVariable Long memberId) {
        return loanService.getMemberLoans(memberId);
    }
}
