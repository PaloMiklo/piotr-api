package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.PRODUCT_LIST;
import static com.api.piotr.constant.ApiPaths.PRODUCT_PATH;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.MockMvc;

import com.api.piotr.dto.ProductRowDto;
import com.api.piotr.service.OrderService;
import com.api.piotr.service.ProductService;

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
}
