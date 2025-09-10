package com.arka.msclients.clients.service;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.arka.msclients.clients.dto.ClientDto;
import com.arka.msclients.clients.mapper.ClientMapper;
import com.arka.msclients.clients.model.Client;
import com.arka.msclients.clients.repository.ClientRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientService {

    public final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Flux<ClientDto> findAll() {
        return clientRepository.findAll()
                .map(ClientMapper::toDto);
    }

    public Mono<ClientDto> findById(Long id) {
        return clientRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "Client not found")))
                .map(ClientMapper::toDto);
    }

    public Mono<ClientDto> findByEmail(String email) {
        return clientRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "Client not found by email")))
                .map(ClientMapper::toDto);
    }

    public Mono<ClientDto> create(ClientDto dto) {
        if (dto.getId() != null) {
            return clientRepository.findById(dto.getId())
                    .flatMap(existing -> Mono
                            .<ClientDto>error(new ResponseStatusException(CONFLICT, "ID already exists")))
                    .switchIfEmpty(
                            clientRepository.findByEmail(dto.getEmail())
                                    .flatMap(existing -> Mono.<ClientDto>error(
                                            new ResponseStatusException(CONFLICT, "Email already exists")))
                                    .switchIfEmpty(clientRepository.save(ClientMapper.toEntity(dto))
                                            .cast(Client.class)
                                            .map(ClientMapper::toDto)));
        }
        return clientRepository.findByEmail(dto.getEmail())
                .flatMap(existing -> Mono.error(new ResponseStatusException(CONFLICT, "Email already exists")))
                .switchIfEmpty(clientRepository.save(ClientMapper.toEntity(dto)))
                .cast(Client.class)
                .map(ClientMapper::toDto);
    }

    public Mono<ClientDto> update(Long id, ClientDto dto) {
        return clientRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "Client not found")))
                .flatMap(found -> {
                    Client toUpdate = ClientMapper.toEntity(dto);
                    toUpdate.setId(found.getId());
                    toUpdate.setCreatedAt(found.getCreatedAt());
                    return clientRepository.save(toUpdate);
                })
                .map(ClientMapper::toDto);
    }

    public Mono<Boolean> delete(Long id) {
        return clientRepository.findById(id)
                .flatMap(client -> clientRepository.delete(client).thenReturn(true))
                .defaultIfEmpty(false);
    }

}
