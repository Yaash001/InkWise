package com.myinvo.inkwise.controller;

import com.myinvo.inkwise.dto.SignupRequest;
import com.myinvo.inkwise.dto.SignupResponse;
import com.myinvo.inkwise.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myinvo.inkwise.dto.LoginRequest;
import com.myinvo.inkwise.dto.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;




import jakarta.validation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/v1/auth/signup
     * Register a new user
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse response) {

    LoginResponse loginResponse = authService.login(request, response);
    return ResponseEntity.ok(loginResponse);
}

@PostMapping("/logout")
public ResponseEntity<Void> logout(HttpServletResponse response) {
    authService.logout(response);
    return ResponseEntity.noContent().build(); // 204
}

}