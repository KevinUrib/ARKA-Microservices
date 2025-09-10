package com.arka.msclients.clients.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.web.server.ResponseStatusException;

import com.arka.msclients.clients.dto.ClientDto;
import com.arka.msclients.clients.mapper.ClientMapper;
import com.arka.msclients.clients.model.Client;
import com.arka.msclients.clients.repository.ClientRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ClientServiceTest {

    private ClientRepository clientRepository;
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        clientRepository = Mockito.mock(ClientRepository.class);
        clientService = new ClientService(clientRepository);
    }

    @Test
    void testFindAllClients() {
        ClientDto client1 = new ClientDto(1L, "John Doe", "john.doe@example.com", "1234567890", "123 Main St");
        ClientDto client2 = new ClientDto(2L, "Jane Doe", "jane.doe@example.com", "0987654321", "456 Elm St");

        when(clientRepository.findAll())
                .thenReturn(Flux.just(ClientMapper.toEntity(client1), ClientMapper.toEntity(client2)));

        StepVerifier.create(clientService.findAll())
                .expectNextMatches(c -> c.getEmail().equals("john.doe@example.com"))
                .expectNextMatches(c -> c.getEmail().equals("jane.doe@example.com"))
                .verifyComplete();
    }

    @Test
    void testFindClientById() {
        ClientDto clientDto = new ClientDto(1L, "John Doe", "john.doe@example.com", "1234567890", "123 Main St");
        when(clientRepository.findById(1L)).thenReturn(Mono.just(ClientMapper.toEntity(clientDto)));

        StepVerifier.create(clientService.findById(1L))
                .expectNextMatches(c -> c.getEmail().equals("john.doe@example.com"))
                .verifyComplete();
    }

    @Test
    void testFindClientByEmail() {
        ClientDto clientDto = new ClientDto(1L, "John Doe", "john.doe@example.com", "1234567890", "123 Main St");
        when(clientRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Mono.just(ClientMapper.toEntity(clientDto)));

        StepVerifier.create(clientService.findByEmail("john.doe@example.com"))
                .expectNextMatches(c -> c.getEmail().equals("john.doe@example.com"))
                .verifyComplete();
    }

    @Test
    void testCreateClient() {
        ClientDto clientDto = new ClientDto(null, "John Doe", "john.doe@example.com", "1234567890", "123 Main St");
        Client clientEntity = ClientMapper.toEntity(clientDto);

        when(clientRepository.findById(anyLong())).thenReturn(Mono.empty());
        when(clientRepository.findByEmail(anyString())).thenReturn(Mono.empty());
        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(clientEntity));

        StepVerifier.create(clientService.create(clientDto))
                .expectNextMatches(saved -> saved.getEmail().equals("john.doe@example.com"))
                .verifyComplete();
    }

    @Test
    void testFindByIdNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(clientService.findById(1L))
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode().value() == 404)
                .verify();
    }

    @Test
    void testDeleteClient() {
        Client clientEntity = new Client(1L, "John Doe", "john.doe@example.com", "1234567890", "123 Main St", null,
                null);

        when(clientRepository.findById(1L)).thenReturn(Mono.just(clientEntity));
        when(clientRepository.delete(any(Client.class))).thenReturn(Mono.empty());

        StepVerifier.create(clientService.delete(1L))
                .expectNext(true)
                .verifyComplete();
    }

}
