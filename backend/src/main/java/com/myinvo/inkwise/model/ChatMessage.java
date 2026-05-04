package com.myinvo.inkwise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chatMessages")
@CompoundIndex(name = "board_created_idx", def = "{'boardId': 1, 'createdAt': -1}")
public class ChatMessage {

    @Id
    private String id;

    @Indexed
    private String boardId;

    @Indexed
    private String userId;

    private String username;

    private String message;

    // List of mentioned user IDs
    private List<String> mentions = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}