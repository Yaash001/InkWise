package com.myinvo.inkwise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {

    private String type; // "ELEMENT_UPDATE", "CURSOR_MOVE", "CHAT_MESSAGE", "USER_JOINED", etc.

    private String boardId;

    private String userId;

    private String username;

    // CRDT update for element changes (Base64 encoded Yjs update)
    private String yDocUpdate;

    // Cursor position
    private Double cursorX;
    private Double cursorY;
    private String cursorColor;

    // Chat message content
    private String message;

    private LocalDateTime timestamp = LocalDateTime.now();

    // Generic data field for extensibility
    private Object data;
}