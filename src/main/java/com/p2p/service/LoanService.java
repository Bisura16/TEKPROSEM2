package com.p2p.service;
 
import com.p2p.domain.*;
import java.math.BigDecimal;
 
public class LoanService {
 
    // Threshold credit score untuk persetujuan loan
    private static final int CREDIT_SCORE_THRESHOLD = 600;
 
    public Loan createLoan(Borrower borrower, BigDecimal amount) {
        validateBorrower(borrower);
        // TC-02: validasi amount
        validateAmount(amount);
        Loan loan = new Loan();
        
        if (borrower.getCreditScore() >= CREDIT_SCORE_THRESHOLD) {
            loan.approve();
        } else {
            loan.reject();
        }
 
        return loan;
    }
    private void validateBorrower(Borrower borrower) {
        if (!borrower.canApplyLoan()) {
            throw new IllegalArgumentException("Borrower not verified");
        }
    }
 
    // Validasi amount untuk TC-02
    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan amount must be greater than 0");
        }
    }
}