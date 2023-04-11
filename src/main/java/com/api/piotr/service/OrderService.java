package com.api.piotr.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.piotr.dto.CartDetFullDto;
import com.api.piotr.dto.CartLineDetDto;
import com.api.piotr.dto.OrderDetFullDto;
import com.api.piotr.dto.OrderDetLightDto;
import com.api.piotr.dto.OrderNewDto;
import com.api.piotr.dto.OrderRowDto;
import com.api.piotr.dto.PayedOptionDetDto;
import com.api.piotr.entity.Customer;
import com.api.piotr.entity.OrderTable;
import com.api.piotr.entity.PayedOption;
import com.api.piotr.entity.PayedOptionItem;
import com.api.piotr.error.ResourceNotFoundException;
import com.api.piotr.repository.AddressRepository;
import com.api.piotr.repository.CartLineRepository;
import com.api.piotr.repository.CustomerRepository;
import com.api.piotr.repository.OrderRepository;
import com.api.piotr.repository.PayedOptionItemRepository;
import com.api.piotr.repository.PayedOptionRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class OrderService {
        private final OrderRepository orderRepository;
        private final AddressRepository addressRepository;
        private final CustomerRepository customerRepository;
        private final CartLineRepository cartLineRepository;
        private final PayedOptionRepository payedOptionRepository;
        private final PayedOptionItemRepository payedOptionItemRepository;

        public Page<OrderRowDto> getAllOrders(Pageable pageable) {
                return orderRepository.findAllOrders(pageable);
        }

        public OrderDetFullDto getFullOrderById(Long id) {
                OrderDetLightDto lighDetail = orderRepository.findOrderById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Order", String.valueOf(id)));

                var cartId = lighDetail.cart().id();

                Set<CartLineDetDto> cartLinesDetDto = cartLineRepository.findCartLinesByCartId(cartId)
                                .orElseThrow(() -> new ResourceNotFoundException("CartLine", String.valueOf(id), "Cart",
                                                String.valueOf(cartId)));

                var cartDetFullDto = new CartDetFullDto(
                                lighDetail.cart().id(),
                                lighDetail.cart().freeShipping(),
                                lighDetail.cart().itemCount(),
                                lighDetail.cart().cartPrice(),
                                cartLinesDetDto);

                var orderFullDetail = new OrderDetFullDto(
                                lighDetail.id(),
                                lighDetail.customer(),
                                lighDetail.deliveryOption(),
                                lighDetail.billingOption(),
                                lighDetail.created(),
                                lighDetail.comment(),
                                lighDetail.shippingAddress(),
                                lighDetail.billingAddress(),
                                cartDetFullDto);

                return orderFullDetail;
        }

        public OrderDetLightDto getLightOrderById(Long id) {
                return orderRepository.findOrderById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Order", String.valueOf(id)));
        }

        public OrderDetLightDto createOrder(OrderNewDto order) {
                PayedOptionItem deliveryOptionItem = prepareDeliveryOptionItem("Payed delivery option", order);
                PayedOptionItem billingOptionItem = prepareBillingOptionItem("Payed billing option", order);

                var customer = customerRepository.save(new Customer(
                                null,
                                order.customer().getFirstName(),
                                order.customer().getLastName(),
                                order.customer().getEmail()));
                var shippingAddress = addressRepository.save(order.shippingAddress());
                var billingAddress = addressRepository.save(order.billingAddress());

                var finalOrder = OrderTable.builder()
                                .customer(customer)
                                .deliveryOption(deliveryOptionItem)
                                .billingOption(billingOptionItem)
                                .created(order.created())
                                .comment(order.comment())
                                .shippingAddress(shippingAddress)
                                .billingAddress(billingAddress)
                                .build();

                OrderTable saved = orderRepository.save(finalOrder);
                return this.getLightOrderById(saved.getId());
        }

        private PayedOptionItem prepareDeliveryOptionItem(String exlicit, OrderNewDto order) {
                String deliveryOptionItemCode = order.deliveryOptionItemCode();
                PayedOption deliveryOption = getPayedOption(exlicit, deliveryOptionItemCode);
                return getPayedOptionItem(exlicit + " item", deliveryOptionItemCode, deliveryOption);
        }

        private PayedOptionItem prepareBillingOptionItem(String exlicit, OrderNewDto order) {
                String billingOptionItemCode = order.billingOptionItemCode();
                PayedOption billingOption = getPayedOption(exlicit, billingOptionItemCode);
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
}
