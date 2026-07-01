package com.loanmatcher.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public HealthResponse health() {
        return new HealthResponse("ok", "loan-rate-matcher");
    }

    record HealthResponse(String status, String service) {
    }

}
