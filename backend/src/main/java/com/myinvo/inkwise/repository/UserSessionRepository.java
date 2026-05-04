package com.myinvo.inkwise.repository;

import com.myinvo.inkwise.model.UserSessionInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserSessionRepository for Redis-based WebSocket session tracking
 * 
 * Key format: "session:{sessionId}:{userId}:{boardId}"
 * Expires after user disconnects (TTL: Session timeout)
 * 
 * Note: This uses Spring Data Redis (CrudRepository)
 * Implementation will be done in RedisConfig
 */
@Repository
public interface UserSessionRepository extends CrudRepository<UserSessionInfo, String> {

    /**
     * Find all active sessions for a specific user
     */
    List<UserSessionInfo> findByUserId(String userId);

    /**
     * Find all active sessions on a specific board
     */
    List<UserSessionInfo> findByBoardId(String boardId);

    /**
     * Find a session by user and board
     */
    Optional<UserSessionInfo> findByUserIdAndBoardId(String userId, String boardId);

    /**
     * Delete session when user disconnects
     */
    long deleteBySessionId(String sessionId);

    /**
     * Count active sessions on a board
     */
    long countByBoardId(String boardId);
}