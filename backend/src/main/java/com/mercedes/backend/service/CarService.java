package com.mercedes.backend.service;

import com.mercedes.backend.dto.CarDTO;
import com.mercedes.backend.mapper.CarMapper;
import com.mercedes.backend.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarDTO> getAllCars() {
        return carRepository.findAll().stream()
                .map(CarMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CarDTO getCarById(Long id) {
        return carRepository.findById(id)
                .map(CarMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

}
