package com.mercedes.backend.service;

import com.mercedes.backend.dto.EventDTO;
import com.mercedes.backend.entity.Event;
import com.mercedes.backend.entity.EventParticipation;
import com.mercedes.backend.repository.EventParticipationRepository;
import com.mercedes.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    private EventParticipationRepository participationRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        System.out.println("Fetched events: " + events);
        return events.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private EventDTO convertToDTO(Event event) {
        System.out.println("Converting event: " + event.getTitle() + ", images: " + event.getImages());
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDate(event.getDate());
        dto.setLocation(event.getLocation());
        dto.setPrice(event.getPrice());
        dto.setImages(event.getImages());
        return dto;
    }


    /**
     * Handles user participation in an event.
     * Checks for existing participation and throws exception if already registered.
     * @param userId The ID of the user
     * @param eventId The ID of the event
     * @throws RuntimeException if user is already participating or event not found
     */
    public ResponseEntity<?> participate(Long userId, Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Event not found"));
        }
        if (participationRepository.existsByUserIdAndEventId(userId, eventId)) {
            return ResponseEntity.status(409).body(Map.of("error", "User is already participating in this event"));  // FIXED: 409 + message
        }
        EventParticipation participation = new EventParticipation(userId, eventId);
        participationRepository.save(participation);
        return ResponseEntity.ok(Map.of("message", "Participation successful"));
    }

    public EventDTO getEventById(Long id) {
        return eventRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<Long> getUserParticipatedEventIds(Long userId) {
        return participationRepository.findEventIdsByUserId(userId);
    }
}