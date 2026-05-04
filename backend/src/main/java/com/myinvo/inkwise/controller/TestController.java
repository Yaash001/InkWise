package com.myinvo.inkwise.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String getTest() {
        return "GET Working";
    }

    @PostMapping
    public String postTest() {
        return "POST Working";
    }
}