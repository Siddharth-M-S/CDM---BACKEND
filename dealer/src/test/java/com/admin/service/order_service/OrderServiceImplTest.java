package com.admin.service.order_service;

import com.admin.entity.*;
import com.admin.exception.CustomException;
import com.admin.repository.*;
import com.admin.service.EmailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private OrderItemsRepository orderItemsRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private EmailService emailService;

    private CartEntity cartEntity;
    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cartEntity = new CartEntity();
        cartEntity.setId(1L);
        cartEntity.setDealerInfoEntity(new DealerInfoEntity());
        cartEntity.getDealerInfoEntity().setId(1L);
        cartEntity.setItems(new ArrayList<>());

        OrderItemEntity orderItem = new OrderItemEntity();
        Car car = new Car();
        car.setPrice(new BigDecimal("10000"));
        orderItem.setCar(car);
        orderItem.setQuantity(2);

        cartEntity.getItems().add(new CartItemEntity(1L, car, 2, cartEntity));

        orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setItems(new ArrayList<>());
    }

    @Test
    void testPlaceOrder_Success() {
        // Set up DealerInfoEntity with valid data
        DealerInfoEntity dealerInfoEntity = new DealerInfoEntity();
        dealerInfoEntity.setId(1L);
        dealerInfoEntity.setEmail("dealer@example.com");
        dealerInfoEntity.setUsername("DealerUser");

        // Attach DealerInfoEntity to CartEntity
        cartEntity.setDealerInfoEntity(dealerInfoEntity);

        // Mocking behavior
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cartEntity));
        when(ordersRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> {
            OrderEntity order = invocation.getArgument(0);
            order.setId(1L); // Simulate database-generated ID
            return order;
        });

        // Call the service
        OrderEntity result = orderService.placeOrder(1L, 1L, "Test Address");

        // Assertions
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartRepository, times(1)).delete(cartEntity);
        verify(emailService, times(1)).orderMessage(
                eq("dealer@example.com"), // Match valid email
                any(LocalDateTime.class),
                eq(1L), // Match order ID
                eq("DealerUser") // Match valid username
        );
    }



    @Test
    void testPlaceOrder_CartNotFound() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.placeOrder(1L, 1L, "Test Address"));
        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void testPlaceOrder_InvalidDealer() {
        cartEntity.getDealerInfoEntity().setId(2L);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cartEntity));

        Exception exception = assertThrows(CustomException.class, () -> orderService.placeOrder(1L, 1L, "Test Address"));
        assertEquals("Cart does not belong to the dealer", exception.getMessage());
    }

    @Test
    void testSoftDeleteOrder_Success() {
        when(ordersRepository.findById(1L)).thenReturn(Optional.of(orderEntity));

        boolean result = orderService.softDeleteOrderById(1L);

        assertTrue(result);
        assertTrue(orderEntity.isDeleted());
        verify(ordersRepository, times(1)).save(orderEntity);
    }

    @Test
    void testSoftDeleteOrder_NotFound() {
        when(ordersRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = orderService.softDeleteOrderById(1L);

        assertFalse(result);
    }
}
