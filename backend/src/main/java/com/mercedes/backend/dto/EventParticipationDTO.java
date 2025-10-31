package com.mercedes.backend.dto;
import java.time.LocalDateTime;

public class EventParticipationDTO {

    private Long id;
    private Long userId;
    private Long eventId;
    private LocalDateTime createdAt;


    // Constructors
    public EventParticipationDTO() {}

    public EventParticipationDTO(Long id, Long userId, Long eventId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "EventParticipationDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", createdAt=" + createdAt +
                '}';
    }
}