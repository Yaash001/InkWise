package com.myinvo.inkwise.service;

import com.myinvo.inkwise.dto.SignupRequest;
import com.myinvo.inkwise.dto.SignupResponse;
import com.myinvo.inkwise.exception.ResourceAlreadyExistsException;
import com.myinvo.inkwise.model.User;
import com.myinvo.inkwise.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.myinvo.inkwise.exception.BadCredentialsException;


import com.myinvo.inkwise.dto.LoginRequest;
import com.myinvo.inkwise.dto.LoginResponse;
import com.myinvo.inkwise.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Register a new user (Signup)
     */
    public SignupResponse signup(SignupRequest request) {
        // 1. Validate username uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already taken");
        }

        // 2. Validate email uniqueness
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        // 3. Normalize username to lowercase (case-insensitive storage)
        String normalizedUsername = request.getUsername().toLowerCase();

        // 4. Hash password using BCrypt (strength: 10)
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // 5. Create new user
        User newUser = new User(
            normalizedUsername,
            request.getEmail().toLowerCase(),
            hashedPassword
        );

        // 6. Save to MongoDB
        User savedUser = userRepository.save(newUser);

        // 7. Return success response
        return new SignupResponse(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail()
        );
    }

    public LoginResponse login(LoginRequest request, HttpServletResponse httpResponse) {

    // 1. Find user by email OR username
    User user = userRepository.findByEmail(request.getIdentifier())
            .or(() -> userRepository.findByUsername(request.getIdentifier()))
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

    // 2. Verify password
    if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
        throw new BadCredentialsException("Invalid email or password");
    }

    // 3. Generate tokens
    String accessToken  = jwtUtil.generateAccessToken(user.getId());
    String refreshToken = jwtUtil.generateRefreshToken(user.getId());

    // 4. Set HttpOnly cookies
    httpResponse.addCookie(buildCookie("access_token",  accessToken,
            (int)(jwtUtil.getAccessExpiryMs()  / 1000)));
    httpResponse.addCookie(buildCookie("refresh_token", refreshToken,
            (int)(jwtUtil.getRefreshExpiryMs() / 1000)));

    return new LoginResponse(user.getId(), user.getUsername(), user.getEmail(), "Login successful");
}

private Cookie buildCookie(String name, String value, int maxAgeSeconds) {
    Cookie cookie = new Cookie(name, value);
    cookie.setHttpOnly(true);        // JS cannot read it — XSS protection
    cookie.setSecure(false);          // HTTPS only (set false locally if needed)
    cookie.setPath("/");
    cookie.setMaxAge(maxAgeSeconds);
    // cookie.setAttribute("SameSite", "Strict"); // uncomment for stricter CSRF protection
    return cookie;
}

public void logout(HttpServletResponse response) {
    response.addCookie(expireCookie("access_token"));
    response.addCookie(expireCookie("refresh_token"));
}

private Cookie expireCookie(String name) {
    Cookie cookie = new Cookie(name, "");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(0); // immediately expires
    return cookie;
}

}