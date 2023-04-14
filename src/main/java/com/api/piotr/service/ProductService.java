package com.api.piotr.service;

import static com.api.piotr.util.Utils.getApply;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.piotr.dto.ProductDetDto;
import com.api.piotr.dto.ProductNewDto;
import com.api.piotr.dto.ProductRowDto;
import com.api.piotr.entity.ImageTable;
import com.api.piotr.entity.Product;
import com.api.piotr.error.ProcessingException;
import com.api.piotr.error.ResourceNotFoundException;
import com.api.piotr.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductRowDto> getAllProducts(Pageable pageable) {
        return productRepository.findAllProducts(pageable);
    }

    public ProductDetDto getProductById(Long id) {
        return productRepository.findProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", String.valueOf(id)));
    }

    public Long createProduct(ProductNewDto product, MultipartFile image) {
        Product newProduct = null;
        try {
            var imageTable = new ImageTable(null, image.getBytes());
            newProduct = getApply(product.toEntity(), e -> e.setImage(imageTable));
        } catch (IOException ex) {
            throw new ProcessingException("image", ex);
        }
        return productRepository.persist(newProduct).getId();
    }
}
