package com.admin.service.car_service;

import com.admin.entity.Car;
import com.admin.exception.CustomException;

import com.admin.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCars() {
        List<Car> cars = new ArrayList<>();
        when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.getAllCars();

        assertEquals(cars, result);
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testGetCarById() {
        int carId = 1;
        Car car = new Car();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Car result = carService.getCarById(carId);

        assertEquals(car, result);
        verify(carRepository, times(1)).findById(carId);
    }

    @Test
    void testGetCarByIdNotFound() {
        int carId = 1;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        Car result = carService.getCarById(carId);

        assertNull(result);
        verify(carRepository, times(1)).findById(carId);
    }

    @Test
    void testGetAllCarsByPage() {
        int page = 0;
        int size = 10;
        List<Car> cars = new ArrayList<>();
        Page<Car> carPage = new PageImpl<>(cars);
        Pageable pageable = PageRequest.of(page, size);
        when(carRepository.findAll(pageable)).thenReturn(carPage);

        Page<Car> result = carService.getAllCarsByPage(page, size);

        assertEquals(carPage, result);
        verify(carRepository, times(1)).findAll(pageable);
    }

    @Test
    void testAddCar() {
        Car car = new Car();
        when(carRepository.save(car)).thenReturn(car);

        Car result = carService.addCar(car);

        assertEquals(car, result);
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void testDeleteCar() {
        int carId = 1;
        doNothing().when(carRepository).deleteById(carId);

        carService.deleteCar(carId);

        verify(carRepository, times(1)).deleteById(carId);
    }

    @Test
    void testUpdateCar() {
        int carId = 1;
        Car existingCar = new Car();
        Car updatedCar = new Car();
        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(existingCar)).thenReturn(existingCar);

        Car result = carService.updateCar(carId, updatedCar);

        assertEquals(existingCar, result);
        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, times(1)).save(existingCar);
    }

    @Test
    void testUpdateCarNotFound() {
        int carId = 1;
        Car updatedCar = new Car();
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            carService.updateCar(carId, updatedCar);
        });

        assertEquals("Car with ID " + carId + " not found.", exception.getMessage());
    }

    @Test
    void testGetCarByName() {
        String carName = "Test Car";
        Car car = new Car();
        when(carRepository.findByName(carName)).thenReturn(car);

        Car result = carService.getCarByName(carName);

        assertEquals(car, result);
        verify(carRepository, times(1)).findByName(carName);
    }

    @Test
    void testUpdateAvail() {
        int carId = 1;
        int quantity = 2;
        Car existingCar = new Car();
        existingCar.setAvailable("10");
        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(existingCar)).thenReturn(existingCar);

        Car result = carService.updateAvail(quantity, carId);

        assertEquals(existingCar, result);
        assertEquals("8", existingCar.getAvailable());
        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, times(1)).save(existingCar);
    }

    @Test
    void testUpdateAvailCarNotFound() {
        int carId = 1;
        int quantity = 2;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            carService.updateAvail(quantity, carId);
        });

        assertEquals("Car id is not found", exception.getMessage());
    }
}