package com.admin.service.order_service;

import com.admin.entity.OrderEntity;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

public interface OrderServices {

    List<OrderEntity> getAllOrders();

    OrderEntity getOrderById(Long orderId);

    @Transactional
    OrderEntity placeOrder(Long dealerId, Long orderId,String address);

    @Transactional
    List<OrderEntity> getOrdersByDealer(Long dealerId);

    List<Map<String,Object>> getMaxCarSales();

    boolean  softDeleteOrderById(Long id);
}

