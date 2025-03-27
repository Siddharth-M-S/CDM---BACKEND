package com.admin.service.cart_services;

import com.admin.entity.Car;
import com.admin.entity.CartEntity;
import com.admin.entity.CartItemEntity;
import com.admin.entity.DealerInfoEntity;
import com.admin.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CarRepository carRepo;

    @Mock
    private DealerInfoRepository dealerInfoRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemsRepository cartItemsRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCarToCart() {
        Long dealerId = 1L;
        Long carId = 1L;
        Integer quantity = 2;

        Car car = new Car();
        DealerInfoEntity dealerInfoEntity = new DealerInfoEntity();
        CartEntity cart = new CartEntity();
        CartItemEntity cartItem = new CartItemEntity();

        when(carRepo.findById(Math.toIntExact(carId))).thenReturn(Optional.of(car));
        when(dealerInfoRepository.findById(dealerId)).thenReturn(Optional.of(dealerInfoEntity));
        when(cartRepository.findByDealerInfoEntityId(dealerId)).thenReturn(Optional.of(cart));
        when(cartItemsRepository.save(any(CartItemEntity.class))).thenReturn(cartItem);
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cart);

        CartEntity result = cartService.addCarToCart(dealerId, carId, quantity);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        verify(cartItemsRepository, times(1)).save(any(CartItemEntity.class));
        verify(cartRepository, times(1)).save(any(CartEntity.class));
    }

    @Test
    void testGetCartItemsByDealer() {
        Long dealerId = 1L;
        CartEntity cart = new CartEntity();

        when(cartRepository.findByDealerInfoEntityId(dealerId)).thenReturn(Optional.of(cart));

        Optional<CartEntity> result = cartService.getCartItemsByDealer(dealerId);

        assertTrue(result.isPresent());
        assertEquals(cart, result.get());
    }

    @Test
    void testRemoveCartItem() {
        Long dealerId = 1L;
        Long cartItemId = 1L;
        CartEntity cart = new CartEntity();
        CartItemEntity cartItem = new CartItemEntity();

        when(cartRepository.findByDealerInfoEntityId(dealerId)).thenReturn(Optional.of(cart));
        when(cartItemsRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        Optional<CartEntity> result = cartService.removeCartItem(dealerId, cartItemId);

        assertTrue(result.isPresent());
        verify(cartItemsRepository, times(1)).delete(cartItem);
        verify(cartRepository, times(1)).save(cart);
    }
}