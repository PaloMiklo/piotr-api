package com.api.piotr.service;

import static com.api.piotr.util.ObjectRandomizer.generateRandomObject;
import static com.api.piotr.util.Utils.mapToList;
import static java.time.Duration.ofMillis;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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
import com.api.piotr.util.Duration;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private static Random random = new Random();

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void getAllProducts() throws Exception {
        assertTimeout(ofMillis(Duration.LEVEL_I.getValue()), () -> {
            List<ProductRowDto> products = new ArrayList<ProductRowDto>(3);
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
        assertTimeout(ofMillis(Duration.LEVEL_I.getValue()), () -> {
            ProductDetDto detail = generateRandomObject(ProductDetDto.class);
            Long id = detail.id();

            when(productRepository.findProductById(id)).thenReturn(of(detail));

            ProductDetDto result = productService.getProductById(id);

            verify(productRepository, times(1)).findProductById(id);

            assertEquals(id, result.id());
        });
    }

    @Test
    public void createProduct() throws Exception {
        assertTimeout(ofMillis(Duration.LEVEL_I.getValue()), () -> {
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
