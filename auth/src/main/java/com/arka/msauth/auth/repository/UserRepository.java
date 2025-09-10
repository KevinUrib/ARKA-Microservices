package com.arka.msauth.auth.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.arka.msauth.auth.model.User;
import reactor.core.publisher.Mono;


public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> findByUsername(String username);

}
