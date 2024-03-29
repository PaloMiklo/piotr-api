package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.ORDER_CREATE;
import static com.api.piotr.constant.ApiPaths.ORDER_DETAIL;
import static com.api.piotr.constant.ApiPaths.ORDER_LIST;
import static com.api.piotr.constant.ApiPaths.ORDER_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.piotr.dto.OrderDetDto;
import com.api.piotr.dto.OrderNewDto;
import com.api.piotr.dto.OrderRowDto;
import com.api.piotr.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(ORDER_PATH)
@Validated
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(ORDER_LIST)
    public ResponseEntity<Page<OrderRowDto>> getAllOrders(Pageable pageable) {
        Page<OrderRowDto> orders = orderService.getAllOrders(pageable);
        return ok(orders);
    }

    @GetMapping(ORDER_DETAIL)
    public ResponseEntity<OrderDetDto> getOrderById(@PathVariable Long id) {
        OrderDetDto order = orderService.getOrderById(id);
        return ok(order);
    }

    @PostMapping(path = ORDER_CREATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> create(@RequestBody @Valid OrderNewDto orderDto) {
        Long orderId = orderService.createOrder(orderDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderId)
                .toUri();
        return created(location).body(orderId);
    }
}
