package com.admin.repository;

import com.admin.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItemEntity, Long> {
}