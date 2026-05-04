package com.myinvo.inkwise.repository;

import com.myinvo.inkwise.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {

    /**
     * Find all boards owned by a specific user
     * Indexed query for performance
     */
    List<Board> findByOwnerUserId(String ownerUserId);

    /**
     * Find board by owner and title
     */
    Optional<Board> findByOwnerUserIdAndTitle(String ownerUserId, String title);

    /**
     * Find boards where user is a collaborator
     * Uses $elemMatch to search within embedded collaborators array
     */
    @Query("{ 'collaborators': { $elemMatch: { 'userId': ?0 } } }")
    List<Board> findBoardsUserIsCollaboratorOn(String userId);

    /**
     * Find board by public share token (for public link access)
     */
Optional<Board> findBySharingPublicShareToken(String publicShareToken);
    /**
     * Find all boards shared with a specific user (either as owner or collaborator)
     */
    
    @Query("{ $or: [ { 'ownerUserId': ?0 }, { 'collaborators.userId': ?0 } ] }")
    List<Board> findAllBoardsAccessibleToUser(String userId);

    /**
     * Find boards created after a specific timestamp (for activity feeds)
     */
    @Query("{ 'createdAt': { $gte: ?0 } }")
    List<Board> findRecentBoards(java.time.LocalDateTime fromDate);

    /**
     * Delete all boards owned by a user (cascade delete)
     */
    long deleteByOwnerUserId(String ownerUserId);

    /**
     * Check if user has specific permission on board
     */
    @Query("{ '_id': ?0, 'collaborators': { $elemMatch: { 'userId': ?1, 'role': ?2 } } }")
    boolean hasPermission(String boardId, String userId, String permission);
}