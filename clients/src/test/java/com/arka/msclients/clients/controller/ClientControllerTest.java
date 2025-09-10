package com.arka.msclients.clients.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.arka.msclients.clients.dto.ClientDto;
import com.arka.msclients.clients.service.ClientService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ClientController.class)
public class ClientControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ClientService clientService;

    private ClientDto clientDto;
    private ClientDto clientDto2;

    @BeforeEach
    void setUp() {
        clientDto = new ClientDto(1L, "John Doe", "john.doe@example.com", "1234567890", "123 Main St");
        clientDto2 = new ClientDto(2L, "Jane Doe", "jane.doe@example.com", "0987654321", "456 Elm St");
    }

    @Test
    void testGetAllClients() {
        Mockito.when(clientService.findAll()).thenReturn(Flux.just(clientDto, clientDto2));

        webTestClient.get().uri("/api/v1/clients")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ClientDto.class)
                .hasSize(2)
                .contains(clientDto, clientDto2);
    }

    @Test
    void testGetClientById() {
        Mockito.when(clientService.findById(1L)).thenReturn(Mono.just(clientDto));

        webTestClient.get().uri("/api/v1/clients/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientDto.class)
                .isEqualTo(clientDto);
    }

    @Test
    void testGetClientById_NotFound() {
        Mockito.when(clientService.findById(99L)).thenReturn(Mono.empty());

        webTestClient.get().uri("/api/v1/clients/{id}", 99L)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetClientByEmail() {
        Mockito.when(clientService.findByEmail("john.doe@example.com")).thenReturn(Mono.just(clientDto));

        webTestClient.get().uri("/api/v1/clients/email/{email}", "john.doe@example.com")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientDto.class)
                .isEqualTo(clientDto);
    }

    @Test
    void testGetClientByEmail_NotFound() {
        Mockito.when(clientService.findByEmail("not.found@example.com")).thenReturn(Mono.empty());

        webTestClient.get().uri("/api/v1/clients/email/{email}", "not.found@example.com")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void testCreateClient(){
        Mockito.when(clientService.create(Mockito.any(ClientDto.class))).thenReturn(Mono.just(clientDto));

        webTestClient.post().uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(clientDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClientDto.class)
                .isEqualTo(clientDto);
    }

    @Test
    void testUpdateClient(){
        ClientDto updatedClient = new ClientDto(1L, "John Smith", "john.smith@example.com", "1234567890", "San Salvador");
        Mockito.when(clientService.update(Mockito.eq(1L), Mockito.any(ClientDto.class))).thenReturn(Mono.just(updatedClient));

        webTestClient.put().uri("/api/v1/clients/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedClient)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientDto.class)
                .isEqualTo(updatedClient);
    }

    @Test
    void testDeleteClient_Success(){
        Mockito.when(clientService.delete(1L)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/api/v1/clients/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteClient_NotFound() {
        Mockito.when(clientService.delete(99L)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/api/v1/clients/{id}", 99L)
                .exchange()
                .expectStatus().isNotFound();
    }

}
