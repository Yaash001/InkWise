package com.myinvo.inkwise.repository;

import com.myinvo.inkwise.model.Presence;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PresenceRepository for Redis-based cursor/user presence tracking
 * 
 * Key format: "board:{boardId}:presence:{userId}"
 * Expires after user disconnects (TTL: 5 minutes)
 * 
 * Note: This uses Spring Data Redis (CrudRepository)
 * Implementation will be done in RedisConfig
 */
@Repository
public interface PresenceRepository extends CrudRepository<Presence, String> {

    /**
     * Find all active users on a specific board
     * Redis key pattern: "board:{boardId}:presence:*"
     */
    List<Presence> findByBoardId(String boardId);

    /**
     * Find a specific user's presence on a board
     */
    Optional<Presence> findByBoardIdAndUserId(String boardId, String userId);

    /**
     * Delete presence when user disconnects
     */
    long deleteByBoardIdAndUserId(String boardId, String userId);

    /**
     * Delete all presence records for a board
     */
    long deleteByBoardId(String boardId);
}