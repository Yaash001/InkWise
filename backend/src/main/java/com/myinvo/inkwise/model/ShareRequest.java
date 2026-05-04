package com.myinvo.inkwise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shareRequests")
@CompoundIndexes({
    @CompoundIndex(name = "board_user_status_idx", def = "{'boardId': 1, 'toUserId': 1, 'status': 1}"),
    @CompoundIndex(name = "from_user_idx", def = "{'fromUserId': 1, 'status': 1}")
})
public class ShareRequest {

    @Id
    private String id;

    @Indexed
    private String boardId;

    @Indexed
    private String fromUserId; // User sending request

    @Indexed
    private String toUserId; // Board owner / approver

    private String status; // "PENDING", "ACCEPTED", "REJECTED"

    private String requestedPermission; // "VIEW" or "EDITOR"

    private String message; // Optional message from requester

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime respondedAt; // When request was approved/rejected

    private String response; // Optional message from approver
}