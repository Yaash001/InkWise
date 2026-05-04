package com.myinvo.inkwise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "boards")
public class Board {

    @Id
    private String id;

    private String title;

    @Indexed
    private String ownerUserId;

    private String description;

    // CRDT state: Serialized Yjs document (Base64 encoded)
    private String yDocState;
    private int version = 1;

    // Embedded elements (will migrate to separate collection if needed)
    private List<Element> elements = new ArrayList<>();

    // Embedded collaborators (small, always needed)
    private List<Collaborator> collaborators = new ArrayList<>();

    // Sharing settings
    private Sharing sharing = new Sharing();

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Nested class for sharing configuration
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sharing {
        private boolean isPublic = false;

        @Indexed(unique = true, sparse = true)
        private String publicShareToken; // Random unique token for public sharing

        private boolean allowPublicRequests = true;
    }
}