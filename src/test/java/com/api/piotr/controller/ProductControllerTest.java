package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.IMAGE;
import static com.api.piotr.constant.ApiPaths.PRODUCT_CREATE;
import static com.api.piotr.constant.ApiPaths.PRODUCT_LIST;
import static com.api.piotr.constant.ApiPaths.PRODUCT_PATH;
import static com.api.piotr.util.ObjectRandomizer.generateRandomObject;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.api.piotr.dto.ProductDetDto;
import com.api.piotr.dto.ProductNewDto;
import com.api.piotr.dto.ProductRowDto;
import com.api.piotr.repository.ImageRepository;
import com.api.piotr.service.ImageTableService;
import com.api.piotr.service.OrderService;
import com.api.piotr.service.ProductService;
import com.api.piotr.util.Duration;
import com.api.piotr.util.ObjectRandomizer;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    private static Random random = new Random();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ImageRepository ImageRepository;

    @MockBean
    private ImageTableService imageService;

    @Test
    public void getAllProducts() throws Exception {
        assertTimeout(ofMillis(Duration.LEVEL_I.getValue()), () -> {
            List<ProductRowDto> productsList = new ArrayList<>();
            productsList.add(new ProductRowDto(
                    1L,
                    ObjectRandomizer.generateRandomString(5),
                    BigDecimal.valueOf(random.nextDouble()),
                    ObjectRandomizer.generateRandomString(25),
                    10,
                    true));
            productsList.add(new ProductRowDto(
                    2L,
                    ObjectRandomizer.generateRandomString(5),
                    BigDecimal.valueOf(random.nextDouble()),
                    ObjectRandomizer.generateRandomString(25),
                    12,
                    true));
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
        assertTimeout(ofMillis(Duration.LEVEL_I.getValue()), () -> {
            ProductDetDto detail = generateRandomObject(ProductDetDto.class);

            given(productService.getProductById(detail.id())).willReturn(detail);

            mockMvc.perform(get(format("%s/%s", PRODUCT_PATH, detail.id())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(is(detail.id())))
                    .andExpect(jsonPath("$.name").value(is(detail.name())))
                    .andExpect(jsonPath("$.price").value(detail.price()))
                    .andExpect(jsonPath("$.description").value(is(detail.description())))
                    .andExpect(jsonPath("$.quantity").value(is(detail.quantity())))
                    .andExpect(jsonPath("$.valid").value(is(detail.valid())));

            verify(productService, times(1)).getProductById(detail.id());
            verifyNoMoreInteractions(productService);
        });
    }

    @Test
    public void getImageByProductId() throws Exception {
        Long id = 1L;
        byte[] imageBytes = "IMAGE LIKE TEST STRING".getBytes();

        StreamingResponseBody stream = outputStream -> {
            outputStream.write(imageBytes);
        };

        given(imageService.getImageByProductId(id)).willReturn(stream);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get(PRODUCT_PATH + IMAGE, id))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertThat(response.getContentType()).isEqualTo(IMAGE_JPEG_VALUE);
        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertArrayEquals(response.getContentAsByteArray(), imageBytes);
    }

    @Test
    public void createProduct() throws Exception {
        assertTimeout(ofMillis(Duration.LEVEL_I.getValue()), () -> {
            ProductNewDto product = generateRandomObject(ProductNewDto.class);
            Long id = 1L;
            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "contract.jpeg",
                    APPLICATION_PDF_VALUE,
                    "<<image data>>".getBytes(UTF_8));

            ObjectMapper objectMapper = new ObjectMapper();

            MockMultipartFile dto = new MockMultipartFile(
                    "product",
                    "product",
                    APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsString(product).getBytes(UTF_8));

            given(productService.createProduct(any(), any())).willReturn(id);

            mockMvc.perform(
                    multipart(format("%s%s", PRODUCT_PATH, PRODUCT_CREATE))
                            .file(dto)
                            .file(image)
                            .accept(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", format("http://localhost/api/product/%s", id)));

            verify(productService, times(1)).createProduct(any(), any());
            verifyNoMoreInteractions(productService);
        });

    }
}