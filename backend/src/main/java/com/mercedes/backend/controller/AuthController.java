package com.mercedes.backend.controller;

import com.mercedes.backend.dto.AuthResponse;
import com.mercedes.backend.dto.LoginRequest;
import com.mercedes.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String username = userDetails.getUsername();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList());
        String token = jwtService.generateToken(username, roles);
        return ResponseEntity.ok(new AuthResponse(token, roles));
    }



    @GetMapping("/verify")
    public ResponseEntity<?> verify(Authentication authentication) {
        System.out.println("🔍 VERIFY CALLED - Authentication: " + authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("🔴 VERIFY - Not authenticated");
            return ResponseEntity.status(403).body(Map.of("error", "Not authenticated"));
        }

        String username = authentication.getName();
        System.out.println("🟢 VERIFY - Authenticated user: " + username);

        try {
            // Get user details (this might be causing issues)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                    .collect(Collectors.toList());

            System.out.println("🟢 VERIFY - Roles: " + roles);

            return ResponseEntity.ok(Map.of(
                    "username", username,
                    "roles", roles,
                    "authenticated", true
            ));
        } catch (Exception e) {
            System.err.println("🔴 VERIFY - UserDetails error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(403).body(Map.of("error", "User not found"));
        }
    }
}