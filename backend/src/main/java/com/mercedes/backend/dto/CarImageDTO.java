package com.mercedes.backend.dto;

public class CarImageDTO {
    private Long id;
    private String imageUrl;
    private boolean isMain;
    private boolean is360;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isMain() { return isMain; }
    public void setMain(boolean main) { isMain = main; }

    public boolean isIs360() { return is360; }
    public void setIs360(boolean is360) { this.is360 = is360; }
}
