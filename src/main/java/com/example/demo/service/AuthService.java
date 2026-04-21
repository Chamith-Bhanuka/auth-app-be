package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JWTUtil jwtService;


    // register new user
    public ApiResponse<String> register(RegisterRequest req) {

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());

        // encrypt password before saving
        user.setPassword(encoder.encode(req.getPassword()));

        user.setRole(req.getRole());

        repo.save(user);

        return new ApiResponse<>(true, "User registered", "success");
    }

    // login user
    public AuthResponse login(LoginRequest req) {
        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return new AuthResponse(accessToken, refreshToken, user.getRole().name());
    }

    // rotate access token using refresh token
    public String refreshAccessToken(String refreshToken) {
        // validate the refresh token first
        if (jwtService.isValid(refreshToken)) {
            String email = jwtService.extractEmail(refreshToken);
            User user = repo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // issue new access token
            return jwtService.generateAccessToken(user.getEmail(), user.getRole().name());
        }
        throw new RuntimeException("Refresh token is invalid or expired");
    }

}
