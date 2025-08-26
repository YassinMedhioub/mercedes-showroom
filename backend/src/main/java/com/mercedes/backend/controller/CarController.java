package com.mercedes.backend.controller;

import com.mercedes.backend.dto.CarDTO;
import com.mercedes.backend.service.CarService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<CarDTO> getCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public CarDTO getCarById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(">>> /api/cars/{id} accessed by " + auth.getName() +
                " with roles " + auth.getAuthorities());
        return carService.getCarById(id);
    }

}
