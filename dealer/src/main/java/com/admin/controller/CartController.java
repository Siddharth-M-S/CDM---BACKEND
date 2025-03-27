package com.admin.controller;

import com.admin.entity.CartEntity;
import com.admin.service.cart_services.CartServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/dealer/carts")
public class CartController {

    private final CartServiceImpl cartService;

    CartController(CartServiceImpl cartService){
        this.cartService=cartService;
    }

    @PostMapping("/addItem")
    public ResponseEntity<CartEntity> addCarToCart(@RequestParam Long dealerId, @RequestParam Long carId, @RequestParam Integer quantity) {
        CartEntity cart = cartService.addCarToCart(dealerId, carId, quantity);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    @DeleteMapping("/removeCartItem")
    public ResponseEntity<CartEntity> removeCartItem(@RequestParam Long dealerId, @RequestParam Long cartItemId) {
        Optional<CartEntity> updatedCart = cartService.removeCartItem(dealerId, cartItemId);
        if (updatedCart.isPresent()) {
            return ResponseEntity.ok(updatedCart.get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if cart item is not found
        }
    }

    @GetMapping("/byDealer/{dealerId}")
    public ResponseEntity<CartEntity> getCartByDealer(@PathVariable Long dealerId) {
        Optional<CartEntity> cartEntity = cartService.getCartItemsByDealer(dealerId);
        if (cartEntity.isPresent()) {
            return ResponseEntity.ok(cartEntity.get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if cart not found
        }
    }



}
