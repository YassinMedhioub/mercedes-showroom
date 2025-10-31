package com.mercedes.backend.service;

import com.mercedes.backend.dto.CarDTO;
import com.mercedes.backend.entity.Car;
import com.mercedes.backend.mapper.CarMapper;
import com.mercedes.backend.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars(String q) {
        if (q == null || q.isBlank()) {
            return carRepository.findAll();
        }
        return carRepository.findByModelContainingIgnoreCaseOrTypeContainingIgnoreCase(q, q);
    }

    public CarDTO getCarById(Long id) {
        return carRepository.findById(id)
                .map(CarMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }


    public CarDTO updateCar(Long id, CarDTO carDTO) {
        Optional<Car> optionalCar = carRepository.findById(id);

        if (optionalCar.isEmpty()) {
            throw new RuntimeException("Car not found with id " + id);
        }

        Car car = optionalCar.get();

        car.setModel(carDTO.getModel());
        car.setType(carDTO.getType());
        car.setPrice(carDTO.getPrice());
        car.setMaxSpeed(carDTO.getMaxSpeed());
        car.setTorque(carDTO.getTorque());
        car.setKm(carDTO.getKm());
        car.setHorsepower(carDTO.getHorsepower());
        car.setFuelType(carDTO.getFuelType());
        car.setTransmission(carDTO.getTransmission());



        Car updated = carRepository.save(car);
        return CarMapper.toDTO(updated);
    }

    public CarDTO createCar(CarDTO carDTO) {
        Car car = new Car();
        car.setModel(carDTO.getModel());
        car.setType(carDTO.getType());
        car.setPrice(carDTO.getPrice());
        car.setMaxSpeed(carDTO.getMaxSpeed());
        car.setTorque(carDTO.getTorque());
        car.setKm(carDTO.getKm());
        car.setHorsepower(carDTO.getHorsepower());
        car.setFuelType(carDTO.getFuelType());
        car.setTransmission(carDTO.getTransmission());

        Car saved = carRepository.save(car);
        return CarMapper.toDTO(saved);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

}
