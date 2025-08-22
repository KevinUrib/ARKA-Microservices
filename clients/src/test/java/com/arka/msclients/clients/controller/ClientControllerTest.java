package com.arka.msclients.clients.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;

import com.arka.msclients.clients.dto.ClientDto;
import com.arka.msclients.clients.service.ClientService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class ClientControllerTest {

    private WebTestClient webTestClient;
    private ClientService clientService;

    @BeforeEach
    public void setUp(){
        clientService = Mockito.mock(ClientService.class);
        ClientController clientController = new ClientController(clientService);
        webTestClient = WebTestClient.bindToController(clientController).build();
    }

    @Test
    public void testGetAllClients(){
        ClientDto clientDto1 = new ClientDto(1L, "John Doe", "john.doe@example.com", "1234567890", "123 Main St");

        when(clientService.findAll())
        .thenReturn(Flux.just(clientDto1));

        webTestClient.get()
        .uri("/api/v1/clients")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(ClientDto.class)
        .hasSize(1)
        .contains(clientDto1);
    }

    @Test
    public void testGetClientById(){
        ClientDto clientDto = new ClientDto(1L, "John Doe", "john.doe@example.com", "1234567890", "123 Main St");
        when(clientService.findById(1L)).thenReturn(Mono.just(clientDto));

        webTestClient.get()
        .uri("/api/v1/clients/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ClientDto.class)
        .isEqualTo(clientDto);
        verify(clientService, times(1)).findById(1L);
    }

    @Test
    public void testCreateClient(){
        ClientDto clientDto = new ClientDto(1L, "John Doe", "john.doe@example.com", "1234567890", "123 Main St");
        when(clientService.create(clientDto)).thenReturn(Mono.just(clientDto));

        webTestClient.post()
        .uri("/api/v1/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(clientDto)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(ClientDto.class)
        .isEqualTo(clientDto);
        verify(clientService, times(1)).create(clientDto);
    }

    @Test
    public void testUpdateClient(){
        ClientDto clientDto = new ClientDto(1L, "John Doe", "john.doe@example.com", "1234567890", "123 Main St");
        when(clientService.update(1L, clientDto)).thenReturn(Mono.just(clientDto));

        webTestClient.put()
        .uri("/api/v1/clients/1")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(clientDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ClientDto.class)
        .isEqualTo(clientDto);
        verify(clientService, times(1)).update(1L, clientDto);
    }

    @Test
    public void testDeleteClient(){
        when(clientService.delete(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
        .uri("/api/v1/clients/1")
        .exchange()
        .expectStatus().isNoContent();
        verify(clientService, times(1)).delete(1L);
    }

    @Test
    public void testGetClientByIdNotFound(){
        when(clientService.findById(1L)).thenReturn(Mono.empty());

        webTestClient.get()
        .uri("/api/v1/clients/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
        
        verify(clientService, times(1)).findById(1L);
    }

    @Test
    public void testCreateClientBadRequest(){
        ClientDto clientDto = new ClientDto(null, "John Doe", "john.doe@example.com", "1234567890", "123 Main St");
        when(clientService.create(clientDto)).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)));

        webTestClient.post()
        .uri("/api/v1/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(clientDto)
        .exchange()
        .expectStatus().isBadRequest();

        verify(clientService, times(1)).create(clientDto);
    }

    @Test
    public void testUpdateClientNotFound(){
        ClientDto clientDto = new ClientDto(55L, "Maria Antonieta", "maria.antonieta@example.com", "1234567890", "123 Main St");
        when(clientService.update(55L, clientDto)).thenReturn(Mono.empty());

        webTestClient.put()
        .uri("/api/v1/clients/55")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(clientDto)
        .exchange()
        .expectStatus().isNotFound();

        verify(clientService, times(1)).update(55L, clientDto);
    }

    @Test
    public void testDeleteClientNotFound(){
        when(clientService.delete(55L)).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));

        webTestClient.delete()
        .uri("/api/v1/clients/55")
        .exchange()
        .expectStatus().isNotFound();

        verify(clientService, times(1)).delete(55L);
    }
}
