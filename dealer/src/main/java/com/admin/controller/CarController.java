package com.admin.controller;

import com.admin.entity.Car;
import com.admin.service.car_service.CarServices;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/cars")
public class CarController {

    private final CarServices service;

    CarController(CarServices service) {
        this.service = service;
    }

    @GetMapping("/viewAllCars")
    public ResponseEntity<List<Car>> getAllCars() {

        return new ResponseEntity<>(service.getAllCars(), HttpStatus.OK);
    }

    @GetMapping("/viewById/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable int id) {

        Car car = service.getCarById(id);

        if (car != null)
            return new ResponseEntity<>(car, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/byPage")
    public ResponseEntity<Page<Car>> getAllCarsByPage(@RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(service.getAllCarsByPage(page, size), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        try {
            Car car1 = service.addCar(car);
            return new ResponseEntity<>(car1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        Car car = service.getCarById(id);
        if (car != null) {
            service.deleteCar(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
        }

    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable int id, @RequestBody Car car) {
        Car updatedCar = service.updateCar(id, car);
        return new ResponseEntity<>(updatedCar, HttpStatus.OK);
    }

    @GetMapping("/byName/{carName}")
    public Car carByName(@PathVariable String carName) {
        return service.getCarByName(carName);
    }
    @PutMapping("/updateAvailByCarId/{quantity}/{carId}")
    public Car updateAvail(@PathVariable int  quantity,@PathVariable int carId)

    {
        return service.updateAvail(quantity,carId);
    }
@PutMapping ("/updateAvailByCarIdAfterDelete/{quantity}/{carId}")
    public Car updateAvailAfterDelete(@PathVariable   int  quantity,@PathVariable int carId)

    {
        return service.updateAvailAfterDelete(quantity,carId);
    }



}
