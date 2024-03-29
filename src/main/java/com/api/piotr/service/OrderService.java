package com.api.piotr.service;

import static com.api.piotr.util.PaidOptionItemWrite.createInstance;
import static java.util.stream.Collectors.toList;

import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.piotr.dto.AddressNewDto;
import com.api.piotr.dto.CartDetDto;
import com.api.piotr.dto.OrderDetDto;
import com.api.piotr.dto.OrderNewDto;
import com.api.piotr.dto.OrderRowDto;
import com.api.piotr.entity.Cart;
import com.api.piotr.entity.Customer;
import com.api.piotr.entity.OrderTable;
import com.api.piotr.error.ResourceNotFoundException;
import com.api.piotr.repository.AddressRepository;
import com.api.piotr.repository.CartLineRepository;
import com.api.piotr.repository.CartRepository;
import com.api.piotr.repository.CustomerRepository;
import com.api.piotr.repository.OrderRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class OrderService {
        private final CartRepository cartRepository;
        private final OrderRepository orderRepository;
        private final AddressRepository addressRepository;
        private final CustomerRepository customerRepository;
        private final CartLineRepository cartLineRepository;

        public Page<OrderRowDto> getAllOrders(Pageable pageable) {
                return orderRepository.findAllOrders(pageable);
        }

        public OrderDetDto getOrderById(Long id) {
                OrderDetDto orderDetWthtLines = orderRepository.findOrderById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Order", String.valueOf(id)));

                CartDetDto cartWthLines = cartLineRepository.findCartLinesByOrderId(id)
                                .map(cartLineDetDtos -> new CartDetDto(
                                                orderDetWthtLines.cart().id(),
                                                orderDetWthtLines.cart().freeShipping(),
                                                orderDetWthtLines.cart().itemCount(),
                                                orderDetWthtLines.cart().cartPrice(),
                                                cartLineDetDtos))
                                .orElseThrow(() -> new ResourceNotFoundException("CartLine", String.valueOf(id)));

                return new OrderDetDto(orderDetWthtLines.id(),
                                orderDetWthtLines.customer(),
                                orderDetWthtLines.deliveryOption(),
                                orderDetWthtLines.billingOption(),
                                orderDetWthtLines.createdUi(),
                                orderDetWthtLines.note(),
                                orderDetWthtLines.shippingAddress(),
                                orderDetWthtLines.billingAddress(),
                                cartWthLines);
        };

        public Long createOrder(OrderNewDto order) {
                var savedCustomer = customerRepository.persist((Customer.builder()
                                .firstName(order.customer().firstName())
                                .lastName(order.customer().lastName())
                                .email(order.customer().email()))
                                .build());

                var savedCart = cartRepository.persist((Cart.builder()
                                .freeShipping(order.cart().freeShipping())
                                .itemCount(order.cart().itemCount())
                                .cartPrice(order.cart().cartPrice())
                                .build()));

                var savedLines = cartLineRepository.persistAll(order.cart().lines()
                                .stream().map(cartLineNewDto -> {
                                        return cartLineNewDto.toEntity(savedCart);
                                }).collect(toList()));

                savedCart.setLines(savedLines);

                var savedAddresses = addressRepository.persistAll(
                                Stream.of(order.shippingAddress(), order.billingAddress())
                                                .map(AddressNewDto::toEntity)
                                                .collect(toList()));

                var finalOrder = new OrderTable();
                finalOrder.setCustomer(savedCustomer);
                finalOrder.setDeliveryOption(createInstance(order.deliveryOptionItemCode()));
                finalOrder.setBillingOption(createInstance(order.billingOptionItemCode()));
                finalOrder.setCreatedUi(order.createdUi());
                finalOrder.setNote(order.note());
                finalOrder.setShippingAddress(savedAddresses.get(0));
                finalOrder.setBillingAddress(savedAddresses.get(1));
                finalOrder.setCart(savedCart);

                return orderRepository.persist(finalOrder).getId();
        }
}
