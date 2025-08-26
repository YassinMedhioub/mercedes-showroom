package com.mercedes.backend.entity;

import jakarta.persistence.*;
import com.mercedes.backend.entity.Car;

@Entity
@Table(name = "car_images")
public class CarImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_main")   // 👈 very important
    private boolean main;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    // getters/setters
    public boolean isMain() {
        return main;
    }
    public void setMain(boolean main) {
        this.main = main;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
