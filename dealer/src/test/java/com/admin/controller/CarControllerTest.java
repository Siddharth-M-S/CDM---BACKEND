package com.admin.controller;

import com.admin.entity.Car;
import com.admin.service.car_service.CarServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

 class CarControllerTest {

    @Mock
    private CarServices carServices;

    @InjectMocks
    private CarController carController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
    }

    @Test
    void testGetAllCars() throws Exception {
        // Arrange
        Car car1 = new Car();
        car1.setId(1);
        car1.setName("Car1");
        car1.setCategory("Sedan");
        car1.setPrice(BigDecimal.valueOf(100000.00));

        Car car2 = new Car();
        car2.setId(2);
        car2.setName("Car2");
        car2.setCategory("SUV");
        car2.setPrice(BigDecimal.valueOf(200000.00));

        when(carServices.getAllCars()).thenReturn(Arrays.asList(car1, car2));

        // Act & Assert
        mockMvc.perform(get("/api/admin/cars/viewAllCars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Car1"))
                .andExpect(jsonPath("$[1].name").value("Car2"));
    }

    @Test
    void testGetCarById_Success() throws Exception {
        // Arrange
        Car car = new Car();
        car.setId(1);
        car.setName("Car1");
        car.setCategory("Sedan");
        car.setPrice(BigDecimal.valueOf(100000.00));

        when(carServices.getCarById(1)).thenReturn(car);

        // Act & Assert
        mockMvc.perform(get("/api/admin/cars/viewById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Car1"));
    }

    @Test
    void testGetCarById_NotFound() throws Exception {
        // Arrange
        when(carServices.getCarById(1)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/admin/cars/viewById/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddCar_Success() throws Exception {
        // Arrange
        Car car = new Car();
        car.setId(1);
        car.setName("Car1");
        car.setCategory("Sedan");
        car.setPrice(BigDecimal.valueOf(100000.00));

        when(carServices.addCar(any(Car.class))).thenReturn(car);

        // Act & Assert
        mockMvc.perform(post("/api/admin/cars/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Car1\",\"type\":\"Sedan\",\"price\":100000.00}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Car1"));
    }

    @Test
    void testAddCar_InternalServerError() throws Exception {
        // Arrange
        when(carServices.addCar(any(Car.class))).thenThrow(new RuntimeException());

        // Act & Assert
        mockMvc.perform(post("/api/admin/cars/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Car1\",\"type\":\"Sedan\",\"price\":100000.00}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        // Arrange
        Car car = new Car();
        car.setId(1);
        car.setName("Car1");

        when(carServices.getCarById(1)).thenReturn(car);
        doNothing().when(carServices).deleteCar(1);

        // Act & Assert
        mockMvc.perform(delete("/api/admin/cars/deleteById/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted"));
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {
        // Arrange
        when(carServices.getCarById(1)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(delete("/api/admin/cars/deleteById/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Car not found"));
    }

    @Test
    void testUpdateCar() throws Exception {
        // Arrange
        Car car = new Car();
        car.setId(1);
        car.setName("UpdatedCar");
        car.setCategory("SUV");
        car.setPrice(BigDecimal.valueOf(150000.00));

        when(carServices.updateCar(eq(1), any(Car.class))).thenReturn(car);

        // Act & Assert
        mockMvc.perform(put("/api/admin/cars/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedCar\",\"type\":\"SUV\",\"price\":150000.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedCar"));
    }

    @Test
    void testCarByName() throws Exception {
        // Arrange
        Car car = new Car();
        car.setId(1);
        car.setName("Car1");
        car.setCategory("Sedan");

        when(carServices.getCarByName("Car1")).thenReturn(car);

        // Act & Assert
        mockMvc.perform(get("/api/admin/cars/byName/Car1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Car1"));
    }

    @Test
    void testUpdateAvail() throws Exception {
        // Arrange
        Car car = new Car();
        car.setId(1);
        car.setName("Car1");
        car.setCategory("Sedan");

        when(carServices.updateAvail(10, 1)).thenReturn(car);

        // Act & Assert
        mockMvc.perform(put("/api/admin/cars/updateAvailByCarId/10/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Car1"));
    }
}
