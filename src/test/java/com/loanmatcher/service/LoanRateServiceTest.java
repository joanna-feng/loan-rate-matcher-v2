package com.loanmatcher.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanRateServiceTest {

    private final LoanRateService loanRateService = new LoanRateService();

    @Test
    void creditScoreInMiddleOfLowestTier_returnsTenPercent() {
        assertEquals(10.0, loanRateService.calculateLoanRate(550));
    }

    @Test
    void creditScoreInMiddleOfMidTier_returnsSevenPercent() {
        assertEquals(7.0, loanRateService.calculateLoanRate(650));
    }

    @Test
    void creditScoreInMiddleOfHighestTier_returnsFourPointFivePercent() {
        assertEquals(4.5, loanRateService.calculateLoanRate(750));
    }

    @Test
    void creditScoreJustBelowSixHundred_returnsTenPercent() {
        assertEquals(10.0, loanRateService.calculateLoanRate(599));
    }

    @Test
    void creditScoreExactlySixHundred_returnsSevenPercent() {
        assertEquals(7.0, loanRateService.calculateLoanRate(600));
    }

    @Test
    void creditScoreJustBelowSevenHundred_returnsSevenPercent() {
        assertEquals(7.0, loanRateService.calculateLoanRate(699));
    }

    @Test
    void creditScoreExactlySevenHundred_returnsFourPointFivePercent() {
        assertEquals(4.5, loanRateService.calculateLoanRate(700));
    }

}
