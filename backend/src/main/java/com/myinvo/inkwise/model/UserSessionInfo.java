package com.myinvo.inkwise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionInfo {

    private String sessionId; // WebSocket session ID

    private String userId;

    private String boardId;

    private LocalDateTime connectedAt = LocalDateTime.now();

    private LocalDateTime lastActivity = LocalDateTime.now();
}