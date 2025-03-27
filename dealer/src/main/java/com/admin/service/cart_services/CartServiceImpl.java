package com.admin.service.cart_services;

import com.admin.entity.Car;
import com.admin.entity.CartEntity;
import com.admin.entity.CartItemEntity;
import com.admin.entity.DealerInfoEntity;
import com.admin.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static com.admin.service.LoginService.logger;

@Service
public class CartServiceImpl implements CartServices {

    private final CarRepository carRepo;

    private final DealerInfoRepository dealerInfoRepository;

    private final CartRepository cartRepository;

    private final CartItemsRepository cartItemsRepository;

    CartServiceImpl(CarRepository carRepo, DealerInfoRepository dealerInfoRepository, CartRepository cartRepository,CartItemsRepository cartItemsRepository) {
        this.carRepo = carRepo;
        this.dealerInfoRepository = dealerInfoRepository;
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
    }


    @Transactional
    @Override
    public CartEntity addCarToCart(Long dealerId, Long carId, Integer quantity) {

        Car car = carRepo.findById(Math.toIntExact(carId)).orElseThrow(() -> new RuntimeException("Car not found"));

        DealerInfoEntity dealerInfoEntity = dealerInfoRepository.findById(dealerId).orElseThrow(() -> new RuntimeException("Dealer not found"));

        CartEntity cart = cartRepository.findByDealerInfoEntityId(dealerId).orElseGet(() -> {

            CartEntity newCart = new CartEntity();

            newCart.setDealerInfoEntity(dealerInfoEntity);

            newCart.setItems(new ArrayList<>()); // Initialize the items list

            return cartRepository.save(newCart);

        });

        CartItemEntity cartItem = new CartItemEntity();

        cartItem.setCar(car);

        cartItem.setQuantity(quantity);

        cartItem.setCart(cart);

        cartItemsRepository.save(cartItem);

        cart.getItems().add(cartItem);

        return cartRepository.save(cart);
    }
    @Override
    public Optional<CartEntity> getCartItemsByDealer(Long dealerId) {
        return cartRepository.findByDealerInfoEntityId(dealerId);
    }

    @Transactional
    @Override
    public Optional<CartEntity> removeCartItem(Long dealerId, Long cartItemId) {
        // Log the received parameters for debugging
        logger.info("Attempting to remove cart item. Dealer ID: {}, CartItem ID: {}", dealerId, cartItemId);

        // Fetch the cart for the given dealer
        Optional<CartEntity> cartEntityOptional = cartRepository.findByDealerInfoEntityId(dealerId);
        if (cartEntityOptional.isEmpty()) {
            logger.error("Cart not found for dealer ID: {}", dealerId);
            return Optional.empty();
        }

        CartEntity cart = cartEntityOptional.get();

        logger.info("Found cart for dealer ID: {}", dealerId);

        Optional<CartItemEntity> cartItemOptional = cartItemsRepository.findById(cartItemId);
        if (cartItemOptional.isEmpty()) {
            logger.error("Cart item with ID: {} not found in the cart.", cartItemId);
            return Optional.empty();
        }

        CartItemEntity cartItem = cartItemOptional.get();

        logger.info("Found cart item with ID: {}", cartItemId);

        cart.getItems().remove(cartItem);

        logger.info("Removing cart item with ID: {}", cartItemId);

        cartItemsRepository.delete(cartItem);

        cartRepository.save(cart);


        logger.info("Successfully removed cart item with ID: {}", cartItemId);

        return Optional.of(cart);
    }
}
