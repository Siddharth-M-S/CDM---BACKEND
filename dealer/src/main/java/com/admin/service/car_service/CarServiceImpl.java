package com.admin.service.car_service;

import com.admin.entity.Car;
import com.admin.exception.CustomException;
import com.admin.exception.FailedToFetchOrderException;
import com.admin.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarServices {

    private final CarRepository repo;


    public static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    CarServiceImpl(CarRepository repo) {
        this.repo = repo;
    }

    /// getAllCars
    @Override
    public List<Car> getAllCars() {
        try {
            return repo.findAll();
        } catch (DataAccessException e) {

            logger.error("Error occurred while fetching all cars: {}", e.getMessage());

            throw new FailedToFetchOrderException("Failed to fetch Cars. Please try again.");

        }
    }

    /// getCarById
    @Override
    public Car getCarById(int id) {
        try {
            Optional<Car> car = repo.findById(id);
            return car.orElse(null);
        } catch (DataAccessException e) {

            logger.error("Error occurred while fetching car with ID {}: ", id, e);
            throw new FailedToFetchOrderException("Failed to fetch Car by Id" + id + "Please try again.");

        }
    }

    /// getAllCarsByPage
    @Override
    public Page<Car> getAllCarsByPage(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return repo.findAll(pageable);
        } catch (DataAccessException e) {

            logger.error("Error occurred while fetching cars with pagination (page: {}, size: {} {} ): ", page, size, e.getMessage());
            throw new FailedToFetchOrderException("Failed to fetch cars by page" + page + "by size" + size);


        }
    }


    /// addCar
    @Override
    public Car addCar(Car car) {
        try {

            return repo.save(car);
        } catch (DataAccessException e) {

            logger.error("Error occurred while saving the car:{} ", e.getMessage());
            throw new FailedToFetchOrderException("Failed to save Car  Please try again.");


        }
    }

    /// deleteCar
    public void deleteCar(int id) {
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            throw new CustomException("Failed to delete Car: " + e.getMessage());
        }
    }


//     In the CarServiceImpl class

    @Override
    public Car updateCar(int id, Car updatedCar) {
        try {
            Optional<Car> existingCar = repo.findById(id);

            if (existingCar.isPresent()) {
                Car car = existingCar.get();

                // Update the car properties
                car.setCategory(updatedCar.getCategory());
                car.setName(updatedCar.getName());
                car.setBrand(updatedCar.getBrand());
                car.setYear(updatedCar.getYear());
                car.setColors(updatedCar.getColors());
                car.setPrice(updatedCar.getPrice());
                car.setSeaters(updatedCar.getSeaters());
                car.setAvailable(updatedCar.getAvailable());


                return repo.save(car);
            } else {
                throw new CustomException("Car with ID " + id + " not found.");
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred while updating the car with ID {}: {}", id, e.getMessage());
            throw new FailedToFetchOrderException("Failed to update car. Please try again.");
        }
    }


    public Car getCarByName(String name) {
        return repo.findByName(name);
    }

@Override
  public   Car updateAvail(int quantity,int id) {
    Optional<Car> existingCar = repo.findById(id);
    if (existingCar.isPresent()) {
        Car car = existingCar.get();
        String myVariable = car.getAvailable();
        int count = Integer.parseInt(myVariable) - quantity;

        car.setAvailable(Integer.toString(count));
        return repo.save(car);
    } else {
        throw new CustomException("Car id is not found");
    }
}


//    @Override
    @Override
    public   Car updateAvailAfterDelete(int quantity,int id) {
        Optional<Car> existingCar = repo.findById(id);

        if (existingCar.isPresent()) {
            Car car = existingCar.get();
            String myVariable = car.getAvailable();
            int count = Integer.parseInt(myVariable) +quantity;

            car .setAvailable(Integer.toString(count));
            return repo.save(car);
        } else {
            throw new CustomException("Car id is not found");
        }
    }





}





