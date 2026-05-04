package com.myinvo.inkwise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Presence {

    private String userId;

    private String username;

    private String boardId;

    private Double cursorX;
    private Double cursorY;

    private String color; // Unique color for this user

    private LocalDateTime lastUpdated = LocalDateTime.now();
}