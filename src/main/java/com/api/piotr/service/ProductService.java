package com.api.piotr.service;

import java.io.IOException;
import java.util.Optional;
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
        Optional<ProductDetDto> productDetail = productRepository.findProductById(id);
        return productDetail.orElseThrow(() -> new ResourceNotFoundException("Product", String.valueOf(id)));
    }

    public ProductDetDto createProduct(ProductNewDto product, MultipartFile image) {
        Product newProduct = product.toEntity();
        try {
            ImageTable imageTable = new ImageTable(null, image.getBytes());
            newProduct.setImage(imageTable);
        } catch (IOException ex) {
            throw new ProcessingException("image", ex);
        }
        Product saved = productRepository.save(newProduct);
        return getProductById(saved.getId());
    }

}
