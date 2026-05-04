package com.myinvo.inkwise.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Chat {

    private String userId;
    private String message;

    private LocalDateTime createdAt = LocalDateTime.now();
}