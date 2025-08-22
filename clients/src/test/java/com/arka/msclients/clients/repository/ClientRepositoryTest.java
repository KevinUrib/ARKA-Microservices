package com.arka.msclients.clients.repository;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import com.arka.msclients.clients.model.Client;

import reactor.test.StepVerifier;

@DataR2dbcTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void testSaveAndFind() {
        Client client = new Client(null, "Kevin", "kevin@mail.com", "123456", "Santa Tecla", LocalDateTime.now(), LocalDateTime.now());

        StepVerifier.create(clientRepository.save(client)
                        .flatMap(saved -> clientRepository.findById(saved.getId())))
                .expectNextMatches(c -> c.getName().equals("Kevin"))
                .verifyComplete();
    }

}
