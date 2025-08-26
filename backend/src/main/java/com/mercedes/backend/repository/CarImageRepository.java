package com.mercedes.backend.repository;

import com.mercedes.backend.entity.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarImageRepository extends JpaRepository<CarImage, Long> {
}
