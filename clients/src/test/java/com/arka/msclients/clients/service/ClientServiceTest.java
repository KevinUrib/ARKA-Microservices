package com.arka.msclients.clients.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.arka.msclients.clients.dto.ClientDto;
import com.arka.msclients.clients.mapper.ClientMapper;
import com.arka.msclients.clients.model.Client;
import com.arka.msclients.clients.repository.ClientRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ClientServiceTest {

        private ClientService clientService;
        private ClientRepository clientRepository;

        @BeforeEach
        public void setUp() {
                clientRepository = Mockito.mock(ClientRepository.class);
                clientService = new ClientService(clientRepository);
        }

        @Test
        public void testGetAllClients() {
                ClientDto clientDto1 = new ClientDto(1L, "John Doe", "john.doe@example.com", "1234567890",
                                "123 Main St");
                ClientDto clientDto2 = new ClientDto(2L, "Jane Smith", "jane.smith@example.com", "0987654321",
                                "456 Elm St");

                when(clientRepository.findAll())
                                .thenReturn(Flux.just(ClientMapper.toEntity(clientDto1),
                                                ClientMapper.toEntity(clientDto2)));

                StepVerifier.create(clientService.findAll())
                                .expectNext(clientDto1)
                                .expectNext(clientDto2)
                                .verifyComplete();

                verify(clientRepository, times(1)).findAll();
        }

        @Test
        public void testGetClientById() {
                ClientDto clientDto = new ClientDto(1L, "John Doe", "john.doe@example.com", "1234567890",
                                "123 Main St");

                when(clientRepository.findById(1L))
                                .thenReturn(Mono.just(ClientMapper.toEntity(clientDto)));

                StepVerifier.create(clientService.findById(1L))
                                .expectNext(clientDto)
                                .verifyComplete();
                verify(clientRepository, times(1)).findById(1L);
                verify(clientRepository, times(1)).findById(1L);
        }

        @Test
        public void testCreateClient() {
                ClientDto clientDTO = new ClientDto(1L, "Kevin Uribe", "kevin@mail.com", "2257-7777", "Santa Tecla");
                Client clientEntity = new Client(1L, "Kevin Uribe", "kevin@mail.com", "2257-7777", "Santa Tecla", null,
                                null);

                // Simula que no existe el cliente con ese email
                when(clientRepository.findByEmail(anyString())).thenReturn(Mono.empty());

                // Simula que el repositorio guarda correctamente
                when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(clientEntity));

                Mono<ClientDto> result = clientService.create(clientDTO);

                StepVerifier.create(result)
                                .expectNextMatches(saved -> saved.getEmail().equals("kevin@mail.com"))
                                .verifyComplete();
        }

        @Test
        public void testUpdateClient() {
                ClientDto clientDto = new ClientDto(1L, "John Doe", "john.doe@example.com", "1234567890",
                                "123 Main St");
                Client clientEntity = new Client(1L, "John Doe", "john.doe@example.com", "1234567890",
                                "123 Main St", null, null);

                // Valido existencia
                when(clientRepository.findById(1L)).thenReturn(Mono.just(clientEntity));
                // Simulo guardado
                when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(clientEntity));
                Mono<ClientDto> dto = clientService.update(1L, clientDto);

                StepVerifier.create(dto)
                                .expectNextMatches(prdct -> prdct.getId().equals(1L)
                                                && prdct.getName().equals("John Doe"))
                                .verifyComplete();
        }

        @Test
        public void testDeleteClient() {
                Client clientEntity = new Client(1L, "John Doe", "john.doe@example.com", "1234567890",
                                "123 Main St", null, null);
                when(clientRepository.findById(1L)).thenReturn(Mono.just(clientEntity));
                when(clientRepository.deleteById(1L)).thenReturn(Mono.empty());
                Mono<Void> result = clientService.delete(1L);

                StepVerifier.create(result)
                                .verifyComplete();
        }

        @Test
        public void testGetClientByIdNotFound() {
                when(clientRepository.findById(1L)).thenReturn(Mono.empty());

                StepVerifier.create(clientService.findById(1L))
                                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                                                ((ResponseStatusException) throwable)
                                                                .getStatusCode() == HttpStatus.NOT_FOUND
                                                &&
                                                throwable.getMessage().contains("Client not found"))
                                .verify();
        }

        @Test
        public void testCreateClientWithExistingId() {
                ClientDto clientDto = new ClientDto(1L, "John Doe", "john.doe@example.com", "1234567890",
                                "123 Main St");
                Client existingClient = ClientMapper.toEntity(clientDto);

                when(clientRepository.findById(1L)).thenReturn(Mono.just(existingClient));

                StepVerifier.create(clientService.create(clientDto))
                                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                                                ((ResponseStatusException) throwable)
                                                                .getStatusCode() == HttpStatus.CONFLICT
                                                &&
                                                throwable.getMessage().contains("ID already exists"))
                                .verify();

                verify(clientRepository, times(1)).findById(1L);
        }

        @Test
        public void testUpdateClientNotFound() {
                when(clientRepository.findById(1L)).thenReturn(Mono.empty());

                StepVerifier.create(clientService.findById(1L))
                                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                                                ((ResponseStatusException) throwable)
                                                                .getStatusCode() == HttpStatus.NOT_FOUND
                                                &&
                                                throwable.getMessage().contains("Client not found"))
                                .verify();
        }

        @Test
        public void testFindClientByEmail() {
                ClientDto clientDto = new ClientDto(1L, "Kevin Uribe", "kevin@mail.com", "257-7777",
                                "Altos de Santa Monica");
                when(clientRepository.findByEmail("kevin@mail.com"))
                                .thenReturn(Mono.just(ClientMapper.toEntity(clientDto)));

                StepVerifier.create(clientService.findByEmail("kevin@mail.com"))
                                .expectNext(clientDto)
                                .verifyComplete();
                verify(clientRepository, times(1)).findByEmail("kevin@mail.com");

        }

}
