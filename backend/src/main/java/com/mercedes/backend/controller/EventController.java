package com.mercedes.backend.controller;

import com.mercedes.backend.entity.Event;
import com.mercedes.backend.dto.EventDTO;
import com.mercedes.backend.service.EventService;
import com.mercedes.backend.entity.User;
import com.mercedes.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    private final UserRepository userRepository;

    public EventController(EventService eventService, UserRepository userRepository) {
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping("/{id}/participate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> participate(@PathVariable Long id, Authentication auth) {
        System.out.println("🔑 Participate called for event " + id + " by principal: " + auth.getName());

        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(403).body("{\"error\": \"No authentication\"}");
        }

        Long userId;
        try {
            UserDetails principal = (UserDetails) auth.getPrincipal();
            String username = principal.getUsername();
            System.out.println("🔍 Username from principal: " + username);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            userId = user.getId();
            System.out.println("✅ User ID resolved: " + userId);  // Debug
        } catch (Exception e) {
            System.err.println("❌ Error resolving userId: " + e.getMessage());  // Log error
            return ResponseEntity.status(400).body("{\"error\": \"Invalid user in token: " + e.getMessage() + "\"}");
        }

        eventService.participate(userId, id);
        return ResponseEntity.ok().body("{\"message\": \"Participation successful\"}");
    }

    @GetMapping("/user/participations")
    @PreAuthorize("hasRole('USER')")
    public List<Long> getUserParticipations(Authentication auth) {
        String username = auth.getName();
        Long userId = getUserIdByUsername(username); 
        return eventService.getUserParticipatedEventIds(userId);
    }

    private Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

}