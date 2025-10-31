package com.mercedes.backend.controller;

import com.mercedes.backend.dto.CarDTO;
import com.mercedes.backend.mapper.CarMapper;
import com.mercedes.backend.service.CarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<CarDTO> getCars(@RequestParam(required = false) String q) {
        return carService.getAllCars(q)
                .stream()
                .map(CarMapper::toDTO)
                .toList();
    }
    
    @GetMapping("/{id}")
    public CarDTO getCarById(@PathVariable Long id) {
        CarDTO carDTO = carService.getCarById(id);
        if (carDTO == null) {
            throw new RuntimeException("Car not found with id: " + id);
        }
        return carDTO;
    }

    @PostMapping
    public CarDTO createCar(@RequestBody CarDTO carDTO) {
        return carService.createCar(carDTO);
    }

    @PutMapping("/{id}")
    public CarDTO updateCar(@PathVariable Long id, @RequestBody CarDTO carDTO) {
        return carService.updateCar(id, carDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }
}