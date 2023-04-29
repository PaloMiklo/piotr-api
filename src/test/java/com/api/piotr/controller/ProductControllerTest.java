package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.PRODUCT_CREATE;
import static com.api.piotr.constant.ApiPaths.PRODUCT_LIST;
import static com.api.piotr.constant.ApiPaths.PRODUCT_PATH;
import static com.api.piotr.util.ObjectRandomizer.generateRandomObject;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.api.piotr.dto.ProductDetDto;
import com.api.piotr.dto.ProductNewDto;
import com.api.piotr.dto.ProductRowDto;
import com.api.piotr.service.OrderService;
import com.api.piotr.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;

    @Test
    public void getAllProducts() throws Exception {
        assertTimeout(Duration.ofMillis(100), () -> {
            List<ProductRowDto> productsList = new ArrayList<>();
            productsList.add(new ProductRowDto(1L));
            productsList.add(new ProductRowDto(2L));
            Page<ProductRowDto> products = new PageImpl<>(productsList);
            given(productService.getAllProducts(any(Pageable.class))).willReturn(products);

            mockMvc.perform(get(PRODUCT_PATH + PRODUCT_LIST))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.content[0].id", is(1)))
                    .andExpect(jsonPath("$.content[1].id", is(2)));

            verify(productService, times(1)).getAllProducts(any(Pageable.class));
            verifyNoMoreInteractions(productService);
        });
    }

    @Test
    public void getProductById() throws Exception {
        assertTimeout(Duration.ofMillis(100), () -> {
            ProductDetDto detail = generateRandomObject(ProductDetDto.class);

            given(productService.getProductById(detail.id())).willReturn(detail);

            mockMvc.perform(get(String.format("%s/%s", PRODUCT_PATH, detail.id())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(is(detail.id())))
                    .andExpect(jsonPath("$.name").value(is(detail.name())))
                    .andExpect(jsonPath("$.price").value(is(Long.valueOf(String.valueOf(detail.price())))))
                    .andExpect(jsonPath("$.description").value(is(detail.description())))
                    .andExpect(jsonPath("$.quantity").value(is(detail.quantity())))
                    .andExpect(jsonPath("$.valid").value(is(detail.valid())));

            verify(productService, times(1)).getProductById(detail.id());
            verifyNoMoreInteractions(productService);
        });
    }

    @Test
    public void createProduct() throws Exception {
        assertTimeout(Duration.ofMillis(100), () -> {
            ProductNewDto product = generateRandomObject(ProductNewDto.class);
            Long id = 1L;
            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "contract.jpeg",
                    MediaType.APPLICATION_PDF_VALUE,
                    "<<image data>>".getBytes(StandardCharsets.UTF_8));

            ObjectMapper objectMapper = new ObjectMapper();

            MockMultipartFile dto = new MockMultipartFile(
                    "product",
                    "product",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsString(product).getBytes(StandardCharsets.UTF_8));

            given(productService.createProduct(any(), any())).willReturn(id);

            mockMvc.perform(
                    multipart(String.format("%s%s", PRODUCT_PATH, PRODUCT_CREATE))
                            .file(dto)
                            .file(image)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", String.format("http://localhost/api/product/%s", id)))
                    .andDo(MockMvcResultHandlers.print());

            verify(productService, times(1)).createProduct(any(), any());
            verifyNoMoreInteractions(productService);
        });
    }

}