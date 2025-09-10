package com.arka.msproducts.products.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.arka.msproducts.products.dto.ProductDto;
import com.arka.msproducts.products.mapper.ProductMapper;
import com.arka.msproducts.products.model.Product;
import com.arka.msproducts.products.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    public final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<ProductDto> getAllProducts() {
        return productRepository.findAll().map(ProductMapper::toDto);
    }

    public Flux<ProductDto> getProductsByCategory(String category) {
        return productRepository.findByCategory(category)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "No products found in this category")))
                .map(ProductMapper::toDto);
    }

    public Mono<ProductDto> getById(String id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "Product not found")))
                .map(ProductMapper::toDto);
    }

    public Mono<ProductDto> createProduct(ProductDto productDto) {
        if (productDto == null) {
            return Mono.error(new ResponseStatusException(BAD_REQUEST, "Product data is required"));
        }
        if (productDto.getName() == null || productDto.getName().trim().isEmpty()) {
            return Mono.error(new ResponseStatusException(BAD_REQUEST, "Product name is required"));
        }
        if (productDto.getPrice() == 0 || productDto.getPrice() <= 0) {
            return Mono.error(new ResponseStatusException(BAD_REQUEST, "Price must be greater than 0"));
        }
        if (productDto.getStock() == 0 || productDto.getStock() <= 0) {
            return Mono.error(new ResponseStatusException(BAD_REQUEST, "Stock must be greater than 0"));
        }

        return productRepository.findByName(productDto.getName())
                .flatMap(existing -> Mono.<ProductDto>error(
                        new ResponseStatusException(CONFLICT, "Product with this name already exists")))
                .switchIfEmpty(
                        productRepository.save(ProductMapper.toEntity(productDto))
                                .map(ProductMapper::toDto));
    }

    public Mono<ProductDto> updateProduct(String id, ProductDto productDto) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "Product not found")))
                .flatMap(existing -> {
                    Product updated = ProductMapper.toEntity(productDto);
                    updated.setId(id);
                    return productRepository.save(updated);
                })
                .map(ProductMapper::toDto);
    }

    public Mono<ProductDto> patchProduct(String id, ProductDto productDto) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "Product not found")))
                .flatMap(existing -> {
                    if (productDto.getName() != null) {
                        existing.setName(productDto.getName());
                    }
                    if (productDto.getDescription() != null) {
                        existing.setDescription(productDto.getDescription());
                    }
                    if (productDto.getPrice() != 0) {
                        existing.setPrice(productDto.getPrice());
                    }
                    if (productDto.getStock() != 0) {
                        existing.setStock(productDto.getStock());
                    }
                    if (productDto.getCategory() != null) {
                        existing.setCategory(productDto.getCategory());
                    }

                    return productRepository.save(existing);
                })
                .map(ProductMapper::toDto);
    }

    public Mono<Void> deleteById(String id, ProductDto productDto) {
        return productRepository.findById(id)
                .flatMap(toDelete -> {
                    if (toDelete.getStock() > 0) {
                        return Mono.error(new ResponseStatusException(CONFLICT, "Cannot delete product with stock"));
                    } else {
                        return productRepository.deleteById(id);
                    }
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "Product not found")));
    }

}
