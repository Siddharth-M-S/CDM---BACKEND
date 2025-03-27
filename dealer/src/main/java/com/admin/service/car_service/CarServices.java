package com.admin.service.car_service;

import com.admin.entity.Car;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CarServices {
    List<Car> getAllCars();

    Car getCarById(int id);

    Page<Car> getAllCarsByPage(int page, int size);

    Car addCar(Car car);

    void deleteCar(int id);
    Car updateCar(int id, Car car);



    Car getCarByName(String carName);

    Car updateAvail(int quantity,int id);
    Car updateAvailAfterDelete(int quantity,int id);
}
