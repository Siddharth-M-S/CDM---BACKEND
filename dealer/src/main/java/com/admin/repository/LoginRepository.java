package com.admin.repository;

import com.admin.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<LoginEntity,Integer> {

    LoginEntity findByDealerId(int dealerName);

}
