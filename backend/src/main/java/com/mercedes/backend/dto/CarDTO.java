// CarDTO.java
package com.mercedes.backend.dto;

import com.mercedes.backend.dto.CarImageDTO;

import java.util.List;

public class CarDTO {
    private Long id;
    private String model;
    private String type;
    private Double price;
    private Integer maxSpeed;
    private String torque;
    private Integer km;
    private Integer horsepower;
    private String fuelType;
    private String transmission;
    private List<CarImageDTO> images;
    private boolean main;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(Integer maxSpeed) { this.maxSpeed = maxSpeed; }

    public String getTorque() { return torque; }
    public void setTorque(String torque) { this.torque = torque; }

    public Integer getKm() { return km; }
    public void setKm(Integer km) { this.km = km; }

    public Integer getHorsepower() { return horsepower; }
    public void setHorsepower(Integer horsepower) { this.horsepower = horsepower; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public List<CarImageDTO> getImages() { return images; }
    public void setImages(List<CarImageDTO> images) { this.images = images; }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }
}
