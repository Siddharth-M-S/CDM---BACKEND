package com.admin.service.order_service;

import com.admin.entity.*;
import com.admin.exception.CustomException;
import com.admin.repository.*;
import com.admin.service.EmailService;
import com.admin.service.car_service.CarServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.admin.service.LoginService.logger;

@Service
public class OrderServiceImpl implements OrderServices {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;


    private final CartRepository cartRepository;
    private final EmailService emailService;

    OrderServiceImpl(OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository,  CartRepository cartRepository, EmailService emailService) {

        this.orderItemsRepository = orderItemsRepository;
        this.ordersRepository = ordersRepository;
        this.cartRepository = cartRepository;
        this.emailService = emailService;


    }

    @Override
    public List<OrderEntity> getAllOrders() {
        // Fetch all orders
        return ordersRepository.findAll();
    }

    @Override
    public OrderEntity getOrderById(Long orderId) {
        return null;
    }


    @Transactional
    @Override
    public OrderEntity placeOrder(Long dealerId, Long cartId, String address) {
        CartEntity cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        if (!cart.getDealerInfoEntity().getId().equals(dealerId)) {
            throw new CustomException("Cart does not belong to the dealer");
        }

        OrderEntity order = new OrderEntity();

        order.setOrderDate(LocalDateTime.now());

        order.setTotalPrice(BigDecimal.ZERO);

        order.setItems(new ArrayList<>());

        order.setDealerInfoEntity(cart.getDealerInfoEntity());
        order.setAddresss(address);
        logger.debug("address{} ", order.getAddresss());

        for (CartItemEntity cartItem : cart.getItems()) {

            OrderItemEntity orderItem = new OrderItemEntity();

            orderItem.setCar(cartItem.getCar());

            orderItem.setQuantity(cartItem.getQuantity());

            orderItem.setOrder(order);

            orderItemsRepository.save(orderItem);

            order.getItems().add(orderItem);

        }

        updateTotalPrice(order);

        OrderEntity savedOrder = ordersRepository.save(order);

        cartRepository.delete(cart);




        emailService.orderMessage(order.getDealerInfoEntity().getEmail(), order.getOrderDate(), order.getId(), order.getDealerInfoEntity().getUsername());

        emailService.paymentSuccessMessage("siddharthkani2004@gmail.com",order.getOrderDate(),order.getId(),order.getDealerInfoEntity().getUsername(),order.getTotalPrice(),"carquest2k25@gmail.com");
        return savedOrder;

    }

    private void updateTotalPrice(OrderEntity order) {

        BigDecimal totalPrice = order.getItems().stream()

                .map(item -> item.getCar().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))

                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);

    }

    @Override
    public List<OrderEntity> getOrdersByDealer(Long dealerId) {
        return ordersRepository.findByDealerInfoEntityId(dealerId);
    }

    @Override
    public List<Map<String, Object>> getMaxCarSales() {
        return ordersRepository.getMaxCarSales();
    }

    public boolean softDeleteOrderById(Long id) {
        try {
            Optional<OrderEntity> order = ordersRepository.findById(id);
            if (order.isPresent()) {
                OrderEntity existingOrder = order.get();
                existingOrder.setDeleted(true); // Mark the order as deleted\
                ordersRepository.save(existingOrder);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new CustomException("Failed to mark the order as deleted.");
        }
    }



}
