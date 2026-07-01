package com.loanmatcher.service;

import com.loanmatcher.model.Client;
import org.springframework.stereotype.Service;

@Service
public class LoanRateService {

    public double calculateLoanRate(Client client) {
        int creditScore = client.creditScore();
        if (creditScore < 600) {
            return 10.0;
        } else if (creditScore < 700) {
            return 7.0;
        } else {
            return 4.5;
        }
    }

}
