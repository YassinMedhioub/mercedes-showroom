package com.mercedes.backend.entity;

import com.mercedes.backend.converter.ImageListConverter;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String date;
    private String location;
    private String price;

    @Column(name = "images")
    @Convert(converter = ImageListConverter.class)
    private List<String> images;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    @Override
    public String toString() {
        return "Event{id=" + id + ", title='" + title + "', images=" + images + "}";
    }
}