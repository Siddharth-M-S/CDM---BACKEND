package com.admin.controller;

import com.admin.entity.OrderEntity;
import com.admin.exception.CustomException;
import com.admin.service.order_service.OrderPdfService;
import com.admin.service.order_service.OrderServices;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/orders")
public class OrdersController {

    private final OrderServices orderServices;
    private final OrderPdfService orderPdfService;

    OrdersController(OrderServices orderServices,OrderPdfService orderPdfService) {
        this.orderServices = orderServices;
        this.orderPdfService=orderPdfService;

    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        return new ResponseEntity<>(orderServices.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable Long orderId) {
        OrderEntity orders = orderServices.getOrderById(orderId);
        if (orders != null)
            return new ResponseEntity<>(orders, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/byDealer/{dealerId}")
    public ResponseEntity<List<OrderEntity>> getOrdersByDealer(@PathVariable Long dealerId) {
        List<OrderEntity> orderEntities = orderServices.getOrdersByDealer(dealerId);
        return ResponseEntity.ok(orderEntities);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity       <String> softDelete(@PathVariable Long orderId) {
        boolean isDeleted = orderServices.softDeleteOrderById(orderId);

        if (isDeleted) {
            return ResponseEntity.ok("Deleted");
        } else {
            return ResponseEntity.ok("NOT DELETED");
        }
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderEntity> placeOrder(@RequestParam Long dealerId, @RequestParam Long cartId,@RequestParam String  address) {
        OrderEntity order = orderServices.placeOrder(dealerId, cartId,address);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}/download")
    public void downloadOrderPdf(@PathVariable Long orderId, HttpServletResponse response) {
        try {
            orderPdfService.generateOrderPdf(orderId, response);
        } catch (Exception e) {
            throw new CustomException("Error generating PDF");
        }
    }

    @GetMapping("/getMaxCarSales")
    public List<Map<String,Object>> getMaxCarSales() {
        return orderServices.getMaxCarSales();}
}