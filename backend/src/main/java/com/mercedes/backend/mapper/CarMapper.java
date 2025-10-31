package com.mercedes.backend.mapper;

import com.mercedes.backend.dto.CarDTO;
import com.mercedes.backend.dto.CarImageDTO;
import com.mercedes.backend.entity.Car;
import com.mercedes.backend.entity.CarImage;
import java.util.List;
import java.util.stream.Collectors;

public class CarMapper {

    public static CarDTO toDTO(Car car) {
        CarDTO dto = new CarDTO();
        dto.setId(car.getId());
        dto.setModel(car.getModel());
        dto.setType(car.getType());
        dto.setPrice(car.getPrice());
        dto.setMaxSpeed(car.getMaxSpeed());
        dto.setTorque(car.getTorque());
        dto.setKm(car.getKm());
        dto.setHorsepower(car.getHorsepower());
        dto.setFuelType(car.getFuelType());
        dto.setTransmission(car.getTransmission());

        List<CarImageDTO> imageDTOs = (car.getImages() != null)
                ? car.getImages().stream().map(CarMapper::toImageDTO).toList()
                : List.of();
        dto.setImages(imageDTOs);

        return dto;
    }


    private static CarImageDTO toImageDTO(CarImage img) {
        CarImageDTO dto = new CarImageDTO();
        dto.setId(img.getId());
        dto.setImageUrl(img.getImageUrl());
        dto.setMain(img.isMain());

        System.out.println("DEBUG: CarImage id=" + img.getId() +
                " url=" + img.getImageUrl() +
                " main=" + img.isMain());

        return dto;
    }

}
