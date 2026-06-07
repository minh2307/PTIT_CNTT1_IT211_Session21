package com.example.ss21.service;

import com.example.ss21.dto.request.LoginRequest;
import com.example.ss21.dto.request.RegisterRequest;
import com.example.ss21.dto.response.AuthResponse;
import com.example.ss21.entity.RefreshToken;
import com.example.ss21.entity.RevokedToken;
import com.example.ss21.entity.User;
import com.example.ss21.repository.RefreshTokenRepository;
import com.example.ss21.repository.RevokedTokenRepository;
import com.example.ss21.repository.UserRepository;
import com.example.ss21.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import com.example.ss21.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshRepo;
    private final RevokedTokenRepository revokedRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public void register(RegisterRequest request){

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(request.getRole())
                .build();
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole().name());

        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        refreshRepo.save(RefreshToken.builder().token(refreshToken).expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .user(user)
                .build());

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken){
        RefreshToken token = refreshRepo.findByToken(refreshToken).orElseThrow();
        if(token.isRevoked()){
            throw new RuntimeException("Refresh token revoked");
        }

        String newAccessToken = jwtUtil.generateAccessToken(token.getUser().getUsername(), token.getUser().getRole().name());

        return new AuthResponse(newAccessToken, refreshToken);
    }

    public void logout(String accessToken, String refreshToken){
        revokedRepo.save(RevokedToken.builder().token(accessToken).revokedAt(LocalDateTime.now()).build());
        RefreshToken token = refreshRepo.findByToken(refreshToken).orElseThrow();
        token.setRevoked(true);
        refreshRepo.save(token);
    }

    public void forceLogout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setEnabled(false);
        userRepository.save(user);

        List<RefreshToken> tokens = refreshRepo.findByUserId(userId);
        tokens.forEach(t -> t.setRevoked(true));
        refreshRepo.saveAll(tokens);
    }
}