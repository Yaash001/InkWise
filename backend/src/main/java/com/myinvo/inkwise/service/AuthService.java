package com.myinvo.inkwise.service;

import com.myinvo.inkwise.dto.LoginRequest;
import com.myinvo.inkwise.dto.LoginResponse;
import com.myinvo.inkwise.dto.SignupRequest;
import com.myinvo.inkwise.dto.SignupResponse;
import com.myinvo.inkwise.exception.BadCredentialsException;
import com.myinvo.inkwise.exception.ResourceAlreadyExistsException;
import com.myinvo.inkwise.model.User;
import com.myinvo.inkwise.repository.UserRepository;
import com.myinvo.inkwise.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        if (userRepository.existsByUsername(request.getUsername().toLowerCase())) {
            throw new ResourceAlreadyExistsException("Username already taken");
        }

        // 2. Validate email uniqueness
        if (userRepository.existsByEmail(request.getEmail().toLowerCase())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        // 3. Normalize username/email
        String normalizedUsername = request.getUsername().toLowerCase();
        String normalizedEmail = request.getEmail().toLowerCase();

        // 4. Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // 5. Create user
        User newUser = new User(
                normalizedUsername,
                normalizedEmail,
                hashedPassword
        );

        // 6. Save user
        User savedUser = userRepository.save(newUser);

        // 7. Return response
        return new SignupResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    /**
     * Login user
     */
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {

        String identifier = request.getIdentifier().toLowerCase();

        // 1. Find user by email or username
        User user = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByUsername(identifier))
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid email/username or password"));

        // 2. Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email/username or password");
        }

        // 3. Generate tokens
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // 4. Set cookies
        response.addCookie(buildCookie(
                "access_token",
                accessToken,
                (int) (jwtUtil.getAccessExpiryMs() / 1000)
        ));

        response.addCookie(buildCookie(
                "refresh_token",
                refreshToken,
                (int) (jwtUtil.getRefreshExpiryMs() / 1000)
        ));

        // 5. Return response
        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                "Login successful"
        );
    }

    /**
     * Refresh access token using refresh token
     */
    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) {

        // 1. Read refresh_token cookie
        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {

                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // 2. If refresh token missing
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token missing");
        }

        // 3. Validate refresh token
        String userId;

        try {
            userId = jwtUtil.validateAndExtractUserId(refreshToken);
        } catch (Exception e) {
            throw new BadCredentialsException("Refresh token invalid or expired");
        }

        // 4. Generate new access token
        String newAccessToken = jwtUtil.generateAccessToken(userId);

        // 5. Set new access token cookie
        response.addCookie(buildCookie(
                "access_token",
                newAccessToken,
                (int) (jwtUtil.getAccessExpiryMs() / 1000)
        ));
    }

    /**
     * Logout user
     */
    public void logout(HttpServletResponse response) {

        response.addCookie(expireCookie("access_token"));
        response.addCookie(expireCookie("refresh_token"));
    }

    /**
     * Build secure HttpOnly cookie
     */
    private Cookie buildCookie(String name,
                               String value,
                               int maxAgeSeconds) {

        Cookie cookie = new Cookie(name, value);

        cookie.setHttpOnly(true);

        // Set true in production HTTPS
        cookie.setSecure(false);

        cookie.setPath("/");

        cookie.setMaxAge(maxAgeSeconds);

        return cookie;
    }

    /**
     * Expire cookie immediately
     */
    private Cookie expireCookie(String name) {

        Cookie cookie = new Cookie(name, "");

        cookie.setHttpOnly(true);

        // Same value as buildCookie
        cookie.setSecure(false);

        cookie.setPath("/");

        cookie.setMaxAge(0);

        return cookie;
    }
}