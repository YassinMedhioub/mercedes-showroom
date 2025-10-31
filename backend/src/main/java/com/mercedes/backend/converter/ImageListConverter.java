package com.mercedes.backend.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ImageListConverter implements AttributeConverter<List<String>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> imageUrls) {
        try {
            return objectMapper.writeValueAsString(
                    imageUrls.stream()
                            .map(url -> new ImageEntry(true, url)) 
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting image URLs to JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String json) {
        try {
            
            List<ImageEntry> entries = objectMapper.readValue(json, new TypeReference<List<ImageEntry>>() {});
            return entries.stream()
                    .map(ImageEntry::getImageUrl)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to image URLs", e);
        }
    }

    // Helper class to match JSON structure
    private static class ImageEntry {
        private boolean main;
        private String imageUrl;

        public ImageEntry() {}

        public ImageEntry(boolean main, String imageUrl) {
            this.main = main;
            this.imageUrl = imageUrl;
        }

        public boolean isMain() { return main; }
        public void setMain(boolean main) { this.main = main; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }
}