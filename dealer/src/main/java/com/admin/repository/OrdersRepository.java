package com.admin.repository;

import com.admin.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface OrdersRepository extends JpaRepository<OrderEntity,Long> {
    List<OrderEntity> findByDealerInfoEntityId(Long dealerId);
    @Query(value = "SELECT c.name, SUM(oi.quantity) AS totalSales " +        "FROM order_item_entity oi " +        "JOIN car c ON oi.car_id = c.id " +        "GROUP BY c.name " +        "ORDER BY totalSales DESC LIMIT 5", nativeQuery = true)
    List<Map<String,Object>> getMaxCarSales();

    @Query(value = "SELECT d.username, COUNT(o.id) AS totalPurchase FROM order_entity o JOIN dealer_info_entity d ON o.dealer_id = d.id GROUP BY d.username ORDER BY totalPurchase DESC LIMIT 5", nativeQuery = true)
    List<Map<String,Object>> getMaxDealerPurchases();

    void deleteByDealerInfoEntityId(Long dealerId);



}
