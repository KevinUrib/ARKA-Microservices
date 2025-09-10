package com.arka.msproducts.products.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.arka.msproducts.products.model.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    Flux<Product> findByCategory(String category);
    Mono<Product> findByName(String name);
}
