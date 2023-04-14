package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.PRODUCT_LIST;
import static com.api.piotr.constant.ApiPaths.PRODUCT_PATH;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.MockMvc;

import com.api.piotr.ObjectRandomizer;
import com.api.piotr.dto.OrderDetDto;
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
    public void testGetAllProducts() throws Exception {
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
    }

    @Test
    public void testGetOrderById() throws Exception {
        // Prepare test data
        OrderDetDto order = ObjectRandomizer.getRandomObject(OrderDetDto.class);

        given(orderService.getOrderById(anyLong())).willReturn(order);

        // Perform the request
        mockMvc.perform(get("/api/orders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customer", is("John Doe")))
                .andExpect(jsonPath("$.deliveryOption", is("Express")))
                .andExpect(jsonPath("$.billingOption", is("Credit card")))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.comment", is("Test comment")))
                .andExpect(jsonPath("$.shippingAddress", is("Test shipping address")))
                .andExpect(jsonPath("$.billingAddress", is("Test billing address")))
                .andExpect(jsonPath("$.cart.id", is(1)))
                .andExpect(jsonPath("$.cart.freeShipping", is(true)))
                .andExpect(jsonPath("$.cart.itemCount", is(2)))
                .andExpect(jsonPath("$.cart.cartPrice", is(100)))
                .andExpect(jsonPath("$.cart.cartLines").isArray())
                .andExpect(jsonPath("$.cart.cartLines", hasSize(2)))
                .andExpect(jsonPath("$.cart.cartLines[0].id", is(1)))
                .andExpect(jsonPath("$.cart.cartLines[0].productId", is(1)))
                .andExpect(jsonPath("$.cart.cartLines[0].productName", is("Product 1")))
                .andExpect(jsonPath("$.cart.cartLines[0].price", is(50)))
                .andExpect(jsonPath("$.cart.cartLines[0].quantity", is(1)))
                .andExpect(jsonPath("$.cart.cartLines[1].id", is(2)))
                .andExpect(jsonPath("$.cart.cartLines[1].productId", is(1)))
                .andExpect(jsonPath("$.cart.cartLines[1].productName", is("Product 2")))
                .andExpect(jsonPath("$.cart.cartLines[1].price", is(50)))
                .andExpect(jsonPath("$.cart.cartLines[1].quantity", is(1)));
    }
}
