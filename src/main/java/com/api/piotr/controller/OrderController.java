package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.ORDER_CREATE;
import static com.api.piotr.constant.ApiPaths.ORDER_LIST;
import static com.api.piotr.constant.ApiPaths.ORDER_PATH;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.piotr.dto.OrderDetDto;
import com.api.piotr.dto.OrderNewDto;
import com.api.piotr.dto.OrderRowDto;
import com.api.piotr.service.OrderService;

@RestController
@RequestMapping(ORDER_PATH)
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping(ORDER_LIST)
    public ResponseEntity<Page<OrderRowDto>> getAllOrders(Pageable pageable) {
        Page<OrderRowDto> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @PostMapping(path = ORDER_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDetDto> create(@RequestBody OrderNewDto orderDto) {
        OrderDetDto order = orderService.createOrder(orderDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.id())
                .toUri();
        return ResponseEntity.created(location).body(order);
    }
}
