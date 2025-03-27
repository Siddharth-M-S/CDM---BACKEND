package com.admin.controller;

import com.admin.entity.OrderEntity;
import com.admin.service.order_service.OrderPdfService;
import com.admin.service.order_service.OrderServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

 class OrdersControllerTest {

    @Mock
    private OrderServices orderServices;

    @Mock
    private OrderPdfService orderPdfService;

    @InjectMocks
    private OrdersController ordersController;

    @Autowired
    private MockMvc mockMvc;

    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ordersController).build();

        // Initialize sample OrderEntity
        orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setOrderDate(java.time.LocalDateTime.now());
        orderEntity.setAddresss("123 Main Street");
        orderEntity.setTotalPrice(new java.math.BigDecimal("1000.00"));
    }

    @Test
    void testGetAllOrders() throws Exception {
        List<OrderEntity> orders = Arrays.asList(orderEntity);
        when(orderServices.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/admin/orders/getAllOrders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].addresss").value("123 Main Street"));
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        when(orderServices.getOrderById(1L)).thenReturn(orderEntity);

        mockMvc.perform(get("/api/admin/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addresss").value("123 Main Street"));
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {
        when(orderServices.getOrderById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/admin/orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOrdersByDealer() throws Exception {
        List<OrderEntity> orders = Arrays.asList(orderEntity);
        when(orderServices.getOrdersByDealer(1L)).thenReturn(orders);

        mockMvc.perform(get("/api/admin/orders/byDealer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].addresss").value("123 Main Street"));
    }

    @Test
    void testSoftDelete_Success() throws Exception {
        when(orderServices.softDeleteOrderById(1L)).thenReturn(true);

        mockMvc.perform(put("/api/admin/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted"));
    }

    @Test
    void testSoftDelete_Failure() throws Exception {
        when(orderServices.softDeleteOrderById(1L)).thenReturn(false);

        mockMvc.perform(put("/api/admin/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("NOT DELETED"));
    }

    @Test
    void testPlaceOrder() throws Exception {
        when(orderServices.placeOrder(1L, 1L, "123 Main Street")).thenReturn(orderEntity);

        mockMvc.perform(post("/api/admin/orders/placeOrder")
                        .param("dealerId", "1")
                        .param("cartId", "1")
                        .param("address", "123 Main Street")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.addresss").value("123 Main Street"));
    }

    @Test
    void testDownloadOrderPdf() throws Exception {
        doNothing().when(orderPdfService).generateOrderPdf(eq(1L), any());

        mockMvc.perform(get("/api/admin/orders/1/download"))
                .andExpect(status().isOk());

        verify(orderPdfService, times(1)).generateOrderPdf(eq(1L), any());
    }

    @Test
    void testGetMaxCarSales() throws Exception {
        List<Map<String, Object>> carSales = Arrays.asList(
                Map.of("carModel", "Model X", "totalSales", 50)
        );
        when(orderServices.getMaxCarSales()).thenReturn(carSales);

        mockMvc.perform(get("/api/admin/orders/getMaxCarSales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].carModel").value("Model X"))
                .andExpect(jsonPath("$[0].totalSales").value(50));
    }
}
