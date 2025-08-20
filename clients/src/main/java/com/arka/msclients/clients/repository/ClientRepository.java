package com.arka.msclients.clients.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.arka.msclients.clients.model.Client;

import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveCrudRepository<Client, Long> {
    Mono<Client> findByEmail(String email);

}
