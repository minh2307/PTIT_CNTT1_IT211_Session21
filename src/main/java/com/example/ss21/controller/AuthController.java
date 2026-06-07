package com.example.ss21.controller;

import com.example.ss21.dto.request.LoginRequest;
import com.example.ss21.dto.request.RefreshTokenRequest;
import com.example.ss21.dto.request.RegisterRequest;
import com.example.ss21.dto.response.AuthResponse;
import com.example.ss21.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){authService.register(request);
        return ResponseEntity.ok("Register Success");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader, @RequestBody RefreshTokenRequest request){
        String accessToken = authHeader.substring(7);
        authService.logout(accessToken, request.getRefreshToken());
        return ResponseEntity.ok("Logout Success");
    }

    @PostMapping("/users/{userId}/force-logout")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> forceLogout(@PathVariable Long userId) {
        authService.forceLogout(userId);
        return ResponseEntity.ok("User has been forced to logout and disabled.");
    }
}