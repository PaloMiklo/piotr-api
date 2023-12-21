package com.api.piotr.service;

import static com.api.piotr.util.ObjectRandomizer.generateRandomObject;
import static com.api.piotr.util.PaidOptionItemWrite.createInstance;
import static com.api.piotr.util.Utils.mapToList;
import static java.time.Duration.ofMillis;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.api.piotr.dto.AddressNewDto;
import com.api.piotr.dto.CartLineRowDto;
import com.api.piotr.dto.OrderDetDto;
import com.api.piotr.dto.OrderNewDto;
import com.api.piotr.dto.OrderRowDto;
import com.api.piotr.entity.Address;
import com.api.piotr.entity.Cart;
import com.api.piotr.entity.CartLine;
import com.api.piotr.entity.Customer;
import com.api.piotr.entity.OrderTable;
import com.api.piotr.repository.AddressRepository;
import com.api.piotr.repository.CartLineRepository;
import com.api.piotr.repository.CartRepository;
import com.api.piotr.repository.CustomerRepository;
import com.api.piotr.repository.OrderRepository;
import com.api.piotr.repository.ProductRepository;
import com.api.piotr.util.Duration;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CartLineRepository cartLineRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void getAllOrders() throws Exception {
        assertTimeout(ofMillis(Duration.LEVEL_I.getValue()), () -> {
            List<OrderRowDto> orders = new ArrayList<OrderRowDto>();
            orders.add(generateRandomObject(OrderRowDto.class));
            orders.add(generateRandomObject(OrderRowDto.class));
            orders.add(generateRandomObject(OrderRowDto.class));
            Page<OrderRowDto> page = new PageImpl<OrderRowDto>(orders);
            List<Long> mockIds = mapToList(page.getContent(), OrderRowDto::id);

            when(orderRepository.findAllOrders(any())).thenReturn(page);

            Page<OrderRowDto> result = orderService.getAllOrders(Pageable.ofSize(1));
            List<Long> resultIds = mapToList(result.getContent(), OrderRowDto::id);

            verify(orderRepository, times(1)).findAllOrders(any());

            assertEquals(3, result.getContent().size());
            assertEquals(mockIds, resultIds);
        });
    }

    @Test
    public void getOrderById() throws Exception {
        assertTimeout(ofMillis(Duration.LEVEL_I.getValue()), () -> {
            OrderDetDto orderDetail = generateRandomObject(OrderDetDto.class);
            Long orderId = orderDetail.id();
            Set<CartLineRowDto> cartLineRows = Set.of(
                    generateRandomObject(CartLineRowDto.class),
                    generateRandomObject(CartLineRowDto.class));

            when(orderRepository.findOrderById(orderId)).thenReturn(Optional.of(orderDetail));
            when(cartLineRepository.findCartLinesByOrderId(orderId)).thenReturn(Optional.of(cartLineRows));

            OrderDetDto result = orderService.getOrderById(orderId);

            verify(orderRepository, times(1)).findOrderById(orderId);
            verify(cartLineRepository, times(1)).findCartLinesByOrderId(orderId);

            assertEquals(orderId, result.id());
        });
    }

    @Test
    public void createOrder() throws Exception {
        assertTimeout(ofMillis(Duration.LEVEL_I.getValue()), () -> {
            OrderNewDto newOrder = generateRandomObject(OrderNewDto.class);
            List<CartLine> cartlines = List.of(
                    generateRandomObject(CartLine.class),
                    generateRandomObject(CartLine.class));

            Customer savedCustomer = Customer.builder()
                    .firstName(newOrder.customer().firstName())
                    .lastName(newOrder.customer().lastName())
                    .email(newOrder.customer().email())
                    .build();

            Cart savedCart = Cart.builder()
                    .freeShipping(newOrder.cart().freeShipping())
                    .itemCount(newOrder.cart().itemCount())
                    .cartPrice(newOrder.cart().cartPrice())
                    .build();

            List<CartLine> savedLines = newOrder.cart().lines()
                    .stream().map(cartLineNewDto -> cartLineNewDto.toEntity(savedCart)).collect(toList());

            List<Address> savedAddresses = Stream.of(newOrder.shippingAddress(), newOrder.billingAddress())
                    .map(AddressNewDto::toEntity)
                    .collect(toList());

            OrderTable finalOrder = new OrderTable();
            finalOrder.setCustomer(savedCustomer);
            finalOrder.setDeliveryOption(createInstance(newOrder.deliveryOptionItemCode()));
            finalOrder.setBillingOption(createInstance(newOrder.billingOptionItemCode()));
            finalOrder.setCreatedUi(newOrder.createdUi());
            finalOrder.setNote(newOrder.note());
            finalOrder.setShippingAddress(savedAddresses.get(0));
            finalOrder.setBillingAddress(savedAddresses.get(1));
            finalOrder.setCart(savedCart);

            when(customerRepository.persist(savedCustomer)).thenReturn(savedCustomer);
            when(cartRepository.persist(savedCart)).thenReturn(savedCart);
            when(cartLineRepository.persistAll(savedLines)).thenReturn(cartlines);
            when(addressRepository.persistAll(savedAddresses)).thenReturn(savedAddresses);
            when(orderRepository.persist(finalOrder)).thenReturn(finalOrder);

            Long result = orderService.createOrder(newOrder);

            verify(customerRepository, times(1)).persist(savedCustomer);
            verify(cartRepository, times(1)).persist(savedCart);
            verify(cartLineRepository, times(1)).persistAll(savedLines);
            verify(addressRepository, times(1)).persistAll(savedAddresses);
            verify(orderRepository, times(1)).persist(finalOrder);

            assertEquals(result, finalOrder.getId());
        });
    }
}
