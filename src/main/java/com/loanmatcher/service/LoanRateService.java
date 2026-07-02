package com.loanmatcher.service;

import org.springframework.stereotype.Service;

@Service
public class LoanRateService {

    public double calculateLoanRate(int creditScore) {
        if (creditScore < 600) {
            return 10.0;
        } else if (creditScore < 700) {
            return 7.0;
        } else {
            return 4.5;
        }
    }

}
