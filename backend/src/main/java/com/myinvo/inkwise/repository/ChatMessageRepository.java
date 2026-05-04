package com.myinvo.inkwise.repository;

import com.myinvo.inkwise.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    /**
     * Find all messages for a board, ordered by creation time (descending)
     * This query uses the compound index: board_created_idx
     */
    List<ChatMessage> findByBoardIdOrderByCreatedAtDesc(String boardId);

    /**
     * Find messages for a board with pagination (for loading chat history)
     * Using Spring Data Page for large datasets
     */
    Page<ChatMessage> findByBoardIdOrderByCreatedAtDesc(String boardId, Pageable pageable);

    /**
     * Find messages sent by a specific user on a board
     */
    List<ChatMessage> findByBoardIdAndUserIdOrderByCreatedAtDesc(String boardId, String userId);

    /**
     * Find messages containing mentions of a specific user
     */
    @Query("{ 'boardId': ?0, 'mentions': ?1 }")
    List<ChatMessage> findMessagesMentioningUser(String boardId, String userId);

    /**
     * Find messages in a board within a time range (for activity logs)
     */
    @Query("{ 'boardId': ?0, 'createdAt': { $gte: ?1, $lte: ?2 } }")
    List<ChatMessage> findMessagesInTimeRange(String boardId, LocalDateTime from, LocalDateTime to);

    /**
     * Find all messages sent by a user (across all boards)
     */
    List<ChatMessage> findByUserIdOrderByCreatedAtDesc(String userId);

    /**
     * Search messages by keyword
     */
    @Query("{ 'boardId': ?0, 'message': { $regex: ?1, $options: 'i' } }")
    List<ChatMessage> searchMessagesByKeyword(String boardId, String keyword);

    /**
     * Count messages in a board
     */
    long countByBoardId(String boardId);

    /**
     * Delete all messages for a board when board is deleted
     */
    long deleteByBoardId(String boardId);

    /**
     * Delete messages from a user (for account deletion)
     */
    long deleteByUserId(String userId);
}