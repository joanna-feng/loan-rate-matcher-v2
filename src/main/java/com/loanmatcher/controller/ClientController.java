package com.loanmatcher.controller;

import com.loanmatcher.model.Client;
import com.loanmatcher.service.ClientService;
import com.loanmatcher.service.LoanRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    private final LoanRateService loanRateService;

    public ClientController(ClientService clientService, LoanRateService loanRateService) {
        this.clientService = clientService;
        this.loanRateService = loanRateService;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> addClient(@RequestBody CreateClientRequest request) {
        Optional<Client> created = clientService.addClient(request.name(), request.creditScore());
        if (created.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created.get()));
    }

    @GetMapping
    public List<ClientResponse> getAllClients(@RequestParam(required = false) Integer minCreditScore) {
        List<Client> clients = minCreditScore != null
                ? clientService.getClientsByMinCreditScore(minCreditScore)
                : clientService.getAllClients();
        return clients.stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(client -> ResponseEntity.ok(toResponse(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateCreditScore(
            @PathVariable Long id, @RequestBody UpdateCreditScoreRequest request) {
        return clientService.updateCreditScore(id, request.creditScore())
                .map(client -> ResponseEntity.ok(toResponse(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        boolean deleted = clientService.deleteClient(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private ClientResponse toResponse(Client client) {
        double loanRate = loanRateService.calculateLoanRate(client.getCreditScore());
        return new ClientResponse(client.getId(), client.getName(), client.getCreditScore(), loanRate);
    }

    record CreateClientRequest(String name, int creditScore) {
    }

    record UpdateCreditScoreRequest(int creditScore) {
    }

    record ClientResponse(Long id, String name, int creditScore, double loanRate) {
    }

}
