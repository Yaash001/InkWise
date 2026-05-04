package com.myinvo.inkwise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collaborator {

    private String userId;

    private String role; // "VIEW" or "EDITOR"

    private LocalDateTime joinedAt = LocalDateTime.now();
}