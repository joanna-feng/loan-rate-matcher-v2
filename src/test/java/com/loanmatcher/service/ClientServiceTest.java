package com.loanmatcher.service;

import com.loanmatcher.model.Client;
import com.loanmatcher.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Test
    void addClient_nameNotTaken_savesAndReturnsClient() {
        ClientService clientService = new ClientService(clientRepository);
        when(clientRepository.existsByName("Alice")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Client> result = clientService.addClient("Alice", 650);

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
        assertEquals(650, result.get().getCreditScore());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void addClient_nameAlreadyTaken_returnsEmptyWithoutSaving() {
        ClientService clientService = new ClientService(clientRepository);
        when(clientRepository.existsByName("Alice")).thenReturn(true);

        Optional<Client> result = clientService.addClient("Alice", 650);

        assertTrue(result.isEmpty());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void updateCreditScore_clientExists_updatesAndReturnsClient() {
        ClientService clientService = new ClientService(clientRepository);
        Client existing = new Client("Alice", 650);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepository.save(existing)).thenReturn(existing);

        Optional<Client> result = clientService.updateCreditScore(1L, 720);

        assertTrue(result.isPresent());
        assertEquals(720, result.get().getCreditScore());
        verify(clientRepository).save(existing);
    }

    @Test
    void updateCreditScore_clientNotFound_returnsEmptyWithoutSaving() {
        ClientService clientService = new ClientService(clientRepository);
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.updateCreditScore(999L, 720);

        assertTrue(result.isEmpty());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void deleteClient_clientExists_deletesAndReturnsTrue() {
        ClientService clientService = new ClientService(clientRepository);
        when(clientRepository.existsById(1L)).thenReturn(true);

        boolean result = clientService.deleteClient(1L);

        assertTrue(result);
        verify(clientRepository).deleteById(1L);
    }

    @Test
    void deleteClient_clientNotFound_returnsFalseWithoutDeleting() {
        ClientService clientService = new ClientService(clientRepository);
        when(clientRepository.existsById(999L)).thenReturn(false);

        boolean result = clientService.deleteClient(999L);

        assertFalse(result);
        verify(clientRepository, never()).deleteById(any());
    }

}
