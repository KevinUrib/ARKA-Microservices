package com.arka.msclients.clients.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arka.msclients.clients.dto.ClientDto;
import com.arka.msclients.clients.service.ClientService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public Flux<ClientDto> getAllClients(){
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ClientDto>> getById(@PathVariable Long id) {
        return clientService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<ClientDto>> createClient(@Valid @RequestBody ClientDto clientDto){
        return clientService.create(clientDto)
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ClientDto>> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDto clientDto){
        return clientService.update(id, clientDto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteClient(@PathVariable Long id){
        return clientService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

}
