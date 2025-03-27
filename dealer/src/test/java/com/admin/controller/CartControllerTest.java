package com.admin.controller;

import com.admin.entity.CartEntity;
import com.admin.service.cart_services.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 class CartControllerTest {

    @Mock
    private CartServiceImpl cartService;

    @InjectMocks
    private CartController cartController;

    @Autowired
    private MockMvc mockMvc;

    private CartEntity cartEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        // Initialize a CartEntity object
        cartEntity = new CartEntity();
        cartEntity.setId(1L);
    }

    @Test
    void testAddCarToCart_Success() throws Exception {
        // Arrange
        when(cartService.addCarToCart(1L, 1L, 2)).thenReturn(cartEntity);

        // Act & Assert
        mockMvc.perform(post("/api/dealer/carts/addItem")
                        .param("dealerId", "1")
                        .param("carId", "1")
                        .param("quantity", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testRemoveCartItem_Success() throws Exception {
        // Arrange
        when(cartService.removeCartItem(1L, 1L)).thenReturn(Optional.of(cartEntity));

        // Act & Assert
        mockMvc.perform(delete("/api/dealer/carts/removeCartItem")
                        .param("dealerId", "1")
                        .param("cartItemId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testRemoveCartItem_NotFound() throws Exception {
        // Arrange
        when(cartService.removeCartItem(1L, 1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/dealer/carts/removeCartItem")
                        .param("dealerId", "1")
                        .param("cartItemId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCartByDealer_Success() throws Exception {
        // Arrange
        when(cartService.getCartItemsByDealer(1L)).thenReturn(Optional.of(cartEntity));

        // Act & Assert
        mockMvc.perform(get("/api/dealer/carts/byDealer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetCartByDealer_NotFound() throws Exception {
        // Arrange
        when(cartService.getCartItemsByDealer(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/dealer/carts/byDealer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
