package com.mercedes.backend.controller;

import com.mercedes.backend.dto.LoginRequest;
import com.mercedes.backend.entity.User;
import com.mercedes.backend.repository.UserRepository;
import com.mercedes.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // Enable cookies
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder; // Add PasswordEncoder bean!

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());

        // Check if user is found and password matches
        if (user.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // Generate JWT token
        String jwt = jwtService.generateToken(user.get().getUsername(), user.get().getRole());

        // Set JWT as HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .path("/")
                .sameSite("Strict")
                .secure(false) // Set to true if using HTTPS!
                .maxAge(Duration.ofHours(3))
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        // Send role to frontend for redirect logic
        return ResponseEntity.ok(Map.of("role", user.get().getRole()));
    }
}
