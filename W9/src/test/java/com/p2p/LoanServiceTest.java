package com.p2p;

import com.p2p.domain.*;
import com.p2p.service.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoanServiceTest {

    private static final Logger log = LogManager.getLogger(LoanServiceTest.class);


    //TC - 01
    @Test
    void shouldRejectLoanWhenBorrowerNotVerified() {
        // ====================================================
        // SCENARIO:
        // Borrower tidak terverifikasi (KYC = false)
        // Ketika borrower mengajukan pinjaman
        // Maka sistem harus menolak dengan melempar exception
        // =====================================================
        // =========================
        // Arrange (Initial Condition)
        // =========================
        // Borrower belum lolos proses KYC
        Borrower borrower = new Borrower(false, 700);
        // Service untuk pengajuan loan
        LoanService loanService = new LoanService();

        // Jumlah pinjaman valid
        BigDecimal amount = BigDecimal.valueOf(1000);

        log.info("TC-01: Mencoba buat loan dengan borrower belum KYC");
        // =========================
        // Act (Action)
        // =========================
        // Borrower mencoba mengajukan loan
        IllegalArgumentException temp = assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(borrower, amount);
        });
        log.warn("TC-01: Loan gagal dibuat: {}", temp.getMessage());

    }

    // TC-02

    @Test
    void shouldRejectLoanWhenAmountIsZeroOrNegative() {
        // Arrange
        Borrower borrower = new Borrower(true, 700);
        LoanService loanService = new LoanService();
        BigDecimal amountZero = BigDecimal.ZERO;
        BigDecimal amountNegatif = BigDecimal.valueOf(-500);

        log.info("TC-02: Test amount tidak valid");

        IllegalArgumentException exNol = assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(borrower, amountZero);
        });
        log.error("TC-02: Amount nol ditolak: {}", exNol.getMessage());
 
        IllegalArgumentException exNegatif = assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(borrower, amountNegatif);
        });
        log.error("TC-02: Amount negatif ditolak: {}", exNegatif.getMessage());
    }

    // TC-03
    @Test
    void shouldApproveLoanWhenCreditScoreHigh() {
        // Arrange
        Borrower borrower = new Borrower(true, 700); // score >= 600
        LoanService loanService = new LoanService();
        BigDecimal amount = BigDecimal.valueOf(5000);

        log.info("TC-03: Borrower verified, credit score tinggi, mengajukan loan");
        // Act
        Loan loan = loanService.createLoan(borrower, amount);

        // Assert
        assertEquals(Loan.Status.APPROVED, loan.getStatus());

        log.info("TC-03: Status loan: {}", loan.getStatus());
    }
    // TC-04
    @Test
    void shouldRejectLoanWhenCreditScoreLow() {
        // Arrange
        Borrower borrower = new Borrower(true, 500); // score < 600
        LoanService loanService = new LoanService();
        BigDecimal amount = BigDecimal.valueOf(5000);
        log.info("TC-04: Borrower verified tapi credit score rendah");
        // Act
        Loan loan = loanService.createLoan(borrower, amount);

        // Assert
        assertEquals(Loan.Status.REJECTED, loan.getStatus());

        log.warn("TC-04: Loan ditolak, status: {}", loan.getStatus());
    }

}