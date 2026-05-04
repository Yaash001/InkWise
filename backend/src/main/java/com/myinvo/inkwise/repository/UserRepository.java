package com.myinvo.inkwise.repository;

import com.myinvo.inkwise.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Find user by email (unique field)
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by username (unique field)
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Find users by partial username match (for mentions)
     */
    @Query("{ 'username': { $regex: ?0, $options: 'i' } }")
    java.util.List<User> findByUsernameContainingIgnoreCase(String username);
}