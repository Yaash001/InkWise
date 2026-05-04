package com.myinvo.inkwise.repository;

import com.myinvo.inkwise.model.ShareRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShareRequestRepository extends MongoRepository<ShareRequest, String> {

    /**
     * Find all pending share requests for a specific user (as recipient)
     * Indexed query for performance
     */
    List<ShareRequest> findByToUserIdAndStatus(String toUserId, String status);

    /**
     * Find all share requests sent by a user
     */
    List<ShareRequest> findByFromUserId(String fromUserId);

    /**
     * Find share requests for a specific board
     */
    List<ShareRequest> findByBoardId(String boardId);

    /**
     * Find share requests for a board with specific status
     */
    List<ShareRequest> findByBoardIdAndStatus(String boardId, String status);

    /**
     * Check if a share request already exists (prevent duplicates)
     */
    Optional<ShareRequest> findByBoardIdAndFromUserIdAndToUserIdAndStatus(
        String boardId,
        String fromUserId,
        String toUserId,
        String status
    );

    /**
     * Find all pending requests for a user on a specific board
     */
    @Query("{ 'boardId': ?0, 'toUserId': ?1, 'status': 'PENDING' }")
    List<ShareRequest> findPendingRequestsForUserOnBoard(String boardId, String userId);

    /**
     * Count pending requests for a user
     */
    long countByToUserIdAndStatus(String toUserId, String status);

    /**
     * Delete requests for a board when board is deleted
     */
    long deleteByBoardId(String boardId);

    /**
     * Delete requests sent/received by a user when user is deleted
     */
    long deleteByFromUserIdOrToUserId(String fromUserId, String toUserId);
}