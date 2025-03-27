package com.admin.service.cart_services;

import com.admin.entity.CartEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CartServices {
    @Transactional
    CartEntity addCarToCart(Long dealerId, Long carId, Integer quantity);

    Optional<CartEntity> getCartItemsByDealer(Long dealerId);

    @Transactional
    Optional<CartEntity> removeCartItem(Long dealerId, Long cartItemId);
}
