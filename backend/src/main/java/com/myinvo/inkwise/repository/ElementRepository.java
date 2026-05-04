package com.myinvo.inkwise.repository;

import com.myinvo.inkwise.model.Element;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * ElementRepository - Placeholder for future migration
 * 
 * Currently, elements are embedded in Board collection.
 * When boards scale (>500 elements), migrate to separate collection.
 * 
 * This interface is prepared for that migration.
 */
@Repository
public interface ElementRepository extends MongoRepository<Element, String> {
    // TODO: Implement when elements are moved to separate collection
    // Example queries:
    // List<Element> findByBoardId(String boardId);
    // List<Element> findByUpdatedBy(String userId);
}