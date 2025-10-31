package com.mercedes.backend.repository;

import com.mercedes.backend.entity.EventParticipation; // Adjust entity import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {

    /**
     * Checks if a participation exists for a given user and event.
     * @param userId The ID of the user
     * @param eventId The ID of the event
     * @return true if participation exists, false otherwise
     */
    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    /**
     * Finds a specific participation by user and event IDs.
     * @param userId The ID of the user
     * @param eventId The ID of the event
     * @return Optional of EventParticipation if found
     */
    Optional<EventParticipation> findByUserIdAndEventId(Long userId, Long eventId);

    // Add these methods to the existing interface

    /**
     * Finds all participations for a user.
     */
    List<EventParticipation> findByUserId(Long userId);

    /**
     * Finds all participations for an event.
     */
    List<EventParticipation> findByEventId(Long eventId);

    @Query("SELECT ep.eventId FROM EventParticipation ep WHERE ep.userId = :userId")
    List<Long> findEventIdsByUserId(@Param("userId") Long userId);
}
