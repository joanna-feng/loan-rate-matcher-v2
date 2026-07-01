package com.loanmatcher.service;

import com.loanmatcher.model.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanRateServiceTest {

    private final LoanRateService loanRateService = new LoanRateService();

    @Test
    void creditScoreInMiddleOfLowestTier_returnsTenPercent() {
        Client client = new Client(1, "Alice", 550);
        assertEquals(10.0, loanRateService.calculateLoanRate(client));
    }

    @Test
    void creditScoreInMiddleOfMidTier_returnsSevenPercent() {
        Client client = new Client(2, "Bob", 650);
        assertEquals(7.0, loanRateService.calculateLoanRate(client));
    }

    @Test
    void creditScoreInMiddleOfHighestTier_returnsFourPointFivePercent() {
        Client client = new Client(3, "Carol", 750);
        assertEquals(4.5, loanRateService.calculateLoanRate(client));
    }

    @Test
    void creditScoreJustBelowSixHundred_returnsTenPercent() {
        Client client = new Client(4, "Dave", 599);
        assertEquals(10.0, loanRateService.calculateLoanRate(client));
    }

    @Test
    void creditScoreExactlySixHundred_returnsSevenPercent() {
        Client client = new Client(5, "Eve", 600);
        assertEquals(7.0, loanRateService.calculateLoanRate(client));
    }

    @Test
    void creditScoreJustBelowSevenHundred_returnsSevenPercent() {
        Client client = new Client(6, "Frank", 699);
        assertEquals(7.0, loanRateService.calculateLoanRate(client));
    }

    @Test
    void creditScoreExactlySevenHundred_returnsFourPointFivePercent() {
        Client client = new Client(7, "Grace", 700);
        assertEquals(4.5, loanRateService.calculateLoanRate(client));
    }

}
