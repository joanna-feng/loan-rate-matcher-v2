package com.loanmatcher.service;

import com.loanmatcher.model.Client;
import com.loanmatcher.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Optional<Client> addClient(String name, int creditScore) {
        if (clientRepository.existsByName(name)) {
            return Optional.empty();
        }
        return Optional.of(clientRepository.save(new Client(name, creditScore)));
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Optional<Client> updateCreditScore(Long id, int newCreditScore) {
        return clientRepository.findById(id)
                .map(client -> {
                    client.setCreditScore(newCreditScore);
                    return clientRepository.save(client);
                });
    }

    public boolean deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            return false;
        }
        clientRepository.deleteById(id);
        return true;
    }

    public List<Client> getClientsByMinCreditScore(int minCreditScore) {
        return clientRepository.findByCreditScoreGreaterThanEqual(minCreditScore);
    }

}
