package com.arka.msproducts.products.mapper;

import com.arka.msproducts.products.dto.ProductDto;
import com.arka.msproducts.products.model.Product;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
       return new ProductDto(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getCategory()
       );
    }

    public static Product toEntity(ProductDto productDto){
        return new Product(
            productDto.getId(),
            productDto.getName(),
            productDto.getDescription(),
            productDto.getPrice(),
            productDto.getStock(),
            productDto.getCategory()
        );
            
    }
}
