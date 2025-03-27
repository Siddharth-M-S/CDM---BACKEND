package com.admin.service.order_service;

import com.admin.entity.Car;
import com.admin.entity.OrderEntity;
import com.admin.entity.OrderItemEntity;
import com.admin.repository.OrdersRepository;
import com.lowagie.text.DocumentException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderPdfServiceTest {

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private OrderPdfService orderPdfService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateOrderPdf() throws IOException, DocumentException {
        Long orderId = 1L;
        OrderEntity order = new OrderEntity();
        order.setId(orderId);

        // Use DateTimeFormatter to parse the date string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime orderDate = LocalDateTime.parse("2025-03-25 00:00:00", formatter);
        order.setOrderDate(orderDate);

        order.setTotalPrice(new java.math.BigDecimal("1000.00"));

        List<OrderItemEntity> items = new ArrayList<>();
        OrderItemEntity item = new OrderItemEntity();
        item.setQuantity(2);

        Car car = new Car();
        car.setName("Test Car");
        car.setPrice(new java.math.BigDecimal("500.00"));
        item.setCar(car);

        items.add(item);
        order.setItems(items);

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Mocking ServletOutputStream
        ServletOutputStream outputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        orderPdfService.generateOrderPdf(orderId, response);

        verify(response, times(1)).setContentType("application/pdf");
        verify(response, times(1)).setHeader("Content-Disposition", "attachment; filename=order_" + orderId + ".pdf");
        verify(response, times(1)).getOutputStream();
    }

    @Test
    void testGenerateOrderPdfOrderNotFound() {
        Long orderId = 1L;

        when(ordersRepository.findById(orderId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderPdfService.generateOrderPdf(orderId, response);
        });

        assertEquals("Order not found", exception.getMessage());
    }
}