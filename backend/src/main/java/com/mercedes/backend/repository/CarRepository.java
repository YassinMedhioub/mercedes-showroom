package com.mercedes.backend.repository;

import com.mercedes.backend.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
