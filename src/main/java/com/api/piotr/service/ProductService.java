package com.api.piotr.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.piotr.dto.ProductDetDto;
import com.api.piotr.dto.ProductRowDto;
import com.api.piotr.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public Page<ProductRowDto> getAllProducts(Pageable pageable) {
        return productRepository.findAllProducts(pageable);
    }

    public ProductDetDto getProductById(Long id) {
        Optional<ProductDetDto> optionalProductDetDto = productRepository.findProductById(id);
        return optionalProductDetDto.orElse(null);
    }
}
