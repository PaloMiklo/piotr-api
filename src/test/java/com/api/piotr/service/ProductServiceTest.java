package com.api.piotr.service;

import static com.api.piotr.util.ObjectRandomizer.generateRandomObject;
import static com.api.piotr.util.Utils.mapToList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import com.api.piotr.dto.ProductDetDto;
import com.api.piotr.dto.ProductNewDto;
import com.api.piotr.dto.ProductRowDto;
import com.api.piotr.entity.Product;
import com.api.piotr.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private static Random random = new Random();

    @Test
    public void getAllProducts() throws Exception {
        assertTimeout(Duration.ofMillis(100), () -> {
            List<ProductRowDto> products = new ArrayList<ProductRowDto>();
            products.add(generateRandomObject(ProductRowDto.class));
            products.add(generateRandomObject(ProductRowDto.class));
            products.add(generateRandomObject(ProductRowDto.class));
            Page<ProductRowDto> page = new PageImpl<ProductRowDto>(products);
            List<Long> mockIds = mapToList(page.getContent(), ProductRowDto::id);

            when(productRepository.findAllProducts(any(Pageable.class))).thenReturn(page);

            Page<ProductRowDto> result = productService.getAllProducts(Pageable.ofSize(1));
            List<Long> resultIds = mapToList(result.getContent(), ProductRowDto::id);

            assertEquals(3, result.getContent().size());
            assertEquals(mockIds, resultIds);
        });
    }

    @Test
    public void getProductById() throws Exception {
        assertTimeout(Duration.ofMillis(100), () -> {
            ProductDetDto detail = generateRandomObject(ProductDetDto.class);
            Long id = detail.id();

            when(productRepository.findProductById(id)).thenReturn(Optional.of(detail));

            ProductDetDto result = productService.getProductById(id);

            verify(productRepository, times(1)).findProductById(id);

            assertEquals(id, result.id());
        });
    }

    @Test
    public void createProduct() throws Exception {
        assertTimeout(Duration.ofMillis(100), () -> {
            ProductNewDto newProduct = generateRandomObject(ProductNewDto.class);
            Product product = newProduct.toEntity();
            Long id = random.nextLong();
            product.setId(id);
            byte[] imageBytes = "Test".getBytes();
            MockMultipartFile mockImage = new MockMultipartFile("image", "test.jpg", "image/jpeg", imageBytes);

            when(productRepository.persist(newProduct.toEntity())).thenReturn(product);

            Long result = productService.createProduct(newProduct, mockImage);

            verify(productRepository, times(1)).persist(newProduct.toEntity());

            assertEquals(result, product.getId());
        });
    }
}
