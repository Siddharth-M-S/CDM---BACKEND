package com.admin.repository;

import com.admin.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car,Integer> {

    Car findByName(String name);
}
