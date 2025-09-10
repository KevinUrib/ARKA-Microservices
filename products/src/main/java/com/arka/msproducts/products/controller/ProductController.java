package com.arka.msproducts.products.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arka.msproducts.products.dto.ProductDto;
import com.arka.msproducts.products.service.ProductService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<ProductDto> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getProductById(@PathVariable String id){
        return productService.getById(id);
    }

    @GetMapping("/category/{category}")
    public Flux<ProductDto> getProductsByCategories(@PathVariable String category){
        return productService.getProductsByCategory(category);
    }

    @PostMapping
    public Mono<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto){
        return productService.createProduct(productDto);
    }

    @PutMapping("/{id}")
    public Mono<ProductDto> updateProduct(@PathVariable String id, @Valid @RequestBody ProductDto dto){
        return productService.updateProduct(id, dto);
    }

    @PatchMapping("/{id}")
    public Mono<ProductDto> patchProduct(@PathVariable String id, @RequestBody ProductDto dto){
        return productService.patchProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id, @RequestBody ProductDto dto){
        return productService.deleteById(id, dto);
    }

}
