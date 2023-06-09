package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.DETAIL;
import static com.api.piotr.constant.ApiPaths.PRODUCT_CREATE;
import static com.api.piotr.constant.ApiPaths.PRODUCT_DETAIL;
import static com.api.piotr.constant.ApiPaths.PRODUCT_LIST;
import static com.api.piotr.constant.ApiPaths.PRODUCT_PATH;
import static com.api.piotr.constant.ApiPaths.PRODUCT_IMAGE;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.piotr.dto.ProductDetDto;
import com.api.piotr.dto.ProductNewDto;
import com.api.piotr.dto.ProductRowDto;
import com.api.piotr.service.ImageTableService;
import com.api.piotr.service.ProductService;

@RestController
@RequestMapping(PRODUCT_PATH)
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageTableService imageService;;

    @GetMapping(PRODUCT_LIST)
    public ResponseEntity<Page<ProductRowDto>> getAllProducts(Pageable pageable) {
        Page<ProductRowDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping(PRODUCT_DETAIL)
    public ResponseEntity<ProductDetDto> getProductById(@PathVariable Long id) {
        ProductDetDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping(PRODUCT_IMAGE)
    public ResponseEntity<StreamingResponseBody> getImageByProductId(@PathVariable Long id) {
        StreamingResponseBody responseBody = imageService.getImageByProductId(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }

    @PostMapping(path = PRODUCT_CREATE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createProduct(
            @RequestPart("product") ProductNewDto productDto,
            @RequestPart("image") MultipartFile image) {
        Long productId = productService.createProduct(productDto, image);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(DETAIL)
                .buildAndExpand(productId)
                .toUri();
        return ResponseEntity.created(location).body(productId);
    }
}
