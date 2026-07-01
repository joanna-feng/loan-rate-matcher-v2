package com.loanmatcher.controller;

import com.loanmatcher.model.Client;
import com.loanmatcher.service.LoanRateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanRateController {

    private final LoanRateService loanRateService;

    public LoanRateController(LoanRateService loanRateService) {
        this.loanRateService = loanRateService;
    }

    @GetMapping("/api/loan-rate")
    public LoanRateResponse getLoanRate(@RequestParam int creditScore) {
        Client client = new Client(0, "", creditScore);
        double loanRate = loanRateService.calculateLoanRate(client);
        return new LoanRateResponse(creditScore, loanRate);
    }

    record LoanRateResponse(int creditScore, double loanRate) {
    }

}
