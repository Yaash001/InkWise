package com.myinvo.inkwise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Element {

    private String id; // UUID

    private String type; // rectangle, circle, text, arrow, etc.

    private double x;
    private double y;

    private double width;
    private double height;

    private String color;

    // Extra metadata (label, content, etc.)
    private String data;

    private String createdBy; // userId who created this
    private String updatedBy; // userId who last updated

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}