package com.api.piotr.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.piotr.dto.CartDetDto;
import com.api.piotr.dto.CartLineNewDto;
import com.api.piotr.dto.OrderDetDto;
import com.api.piotr.dto.OrderNewDto;
import com.api.piotr.dto.OrderRowDto;
import com.api.piotr.dto.PayedOptionDetDto;
import com.api.piotr.entity.Cart;
import com.api.piotr.entity.CartLine;
import com.api.piotr.entity.Customer;
import com.api.piotr.entity.OrderTable;
import com.api.piotr.entity.PayedOption;
import com.api.piotr.entity.PayedOptionItem;
import com.api.piotr.error.ResourceNotFoundException;
import com.api.piotr.repository.AddressRepository;
import com.api.piotr.repository.CartLineRepository;
import com.api.piotr.repository.CartRepository;
import com.api.piotr.repository.CustomerRepository;
import com.api.piotr.repository.OrderRepository;
import com.api.piotr.repository.PayedOptionItemRepository;
import com.api.piotr.repository.PayedOptionRepository;
import com.api.piotr.repository.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class OrderService {
        private final CartRepository cartRepository;
        private final OrderRepository orderRepository;
        private final ProductRepository productRepository;
        private final AddressRepository addressRepository;
        private final CustomerRepository customerRepository;
        private final CartLineRepository cartLineRepository;
        private final PayedOptionRepository payedOptionRepository;
        private final PayedOptionItemRepository payedOptionItemRepository;

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
                                orderDetWthtLines.created(),
                                orderDetWthtLines.comment(),
                                orderDetWthtLines.shippingAddress(),
                                orderDetWthtLines.billingAddress(),
                                cartWthLines);
        }

        public Long createOrder(OrderNewDto order) {
                var deliveryOptionItem = prepareDeliveryOptionItem("Payed delivery option", order);
                var billingOptionItem = prepareBillingOptionItem("Payed billing option", order);

                var savedCustomer = customerRepository.save((Customer.builder()
                                .firstName(order.customer().firstName())
                                .lastName(order.customer().lastName())
                                .email(order.customer().email()))
                                .build());

                var savedCart = cartRepository.save((Cart.builder()
                                .freeShipping(order.cart().freeShipping())
                                .itemCount(order.cart().itemCount())
                                .cartPrice(order.cart().cartPrice())
                                .build()));

                var cartLines = prepareCartLines(order.cart().lines(), savedCart);

                var savedLines = cartLineRepository.saveAll(cartLines);
                savedCart.setLines(savedLines);

                var shippingAddress = addressRepository.save(order.shippingAddress().toEntity());
                var billingAddress = addressRepository.save(order.billingAddress().toEntity());

                var finalOrder = OrderTable.builder()
                                .customer(savedCustomer)
                                .deliveryOption(deliveryOptionItem)
                                .billingOption(billingOptionItem)
                                .created(order.created())
                                .comment(order.comment())
                                .shippingAddress(shippingAddress)
                                .billingAddress(billingAddress)
                                .cart(savedCart)
                                .build();

                return orderRepository.save(finalOrder).getId();
        }

        private PayedOptionItem prepareDeliveryOptionItem(String exlicit, OrderNewDto order) {
                var deliveryOptionItemCode = order.deliveryOptionItemCode();
                var deliveryOption = getPayedOption(exlicit, deliveryOptionItemCode);
                return getPayedOptionItem(exlicit + " item", deliveryOptionItemCode, deliveryOption);
        }

        private PayedOptionItem prepareBillingOptionItem(String exlicit, OrderNewDto order) {
                var billingOptionItemCode = order.billingOptionItemCode();
                var billingOption = getPayedOption(exlicit, billingOptionItemCode);
                return getPayedOptionItem(exlicit + " item", billingOptionItemCode, billingOption);

        }

        private PayedOption getPayedOption(String explicit, String optionItemCode) {
                return payedOptionRepository.findPayedOptionByItemCode(optionItemCode)
                                .map(PayedOptionDetDto::toEntity)
                                .orElseThrow(() -> new ResourceNotFoundException(explicit, optionItemCode));
        }

        private PayedOptionItem getPayedOptionItem(String explicit, String optionItemCode, PayedOption option) {
                return payedOptionItemRepository.findPayedOptionItemByCode(optionItemCode)
                                .map(dto -> dto.toEntity(option))
                                .orElseThrow(() -> new ResourceNotFoundException(explicit, optionItemCode));
        }

        private List<CartLine> prepareCartLines(Set<CartLineNewDto> lineDtos, Cart cart) {
                return lineDtos.stream().map(cartLineNewDto -> {
                        var productId = cartLineNewDto.productId();
                        var product = productRepository.findById(productId)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Product", String.valueOf(productId)));
                        var cartLine = cartLineNewDto.toEntity(product);
                        cartLine.setCart(cart);
                        return cartLine;
                }).collect(Collectors.toList());
        }
}
