package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    // register endpoint
    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest req) {
        return service.register(req);
    }

    // login endpoint
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest req) {
        AuthResponse auth = service.login(req);

        // Access token cookie (15 min)
        ResponseCookie accessCookie = createCookie("accessToken", auth.getToken(), 15 * 60);
        // Refresh token cookie (7 days)
        ResponseCookie refreshCookie = createCookie("refreshToken", auth.getRefreshToken(), 7 * 24 * 60 * 60);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new ApiResponse<>(true, "Login successful", auth));
    }

    private ResponseCookie createCookie(String name, String value, long maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)    // Prevents JS access
                .secure(false)      // if set as true it allow HTTPS only
                .path("/")         // available to all endpoints
                .maxAge(maxAge)    // expiration in seconds
                .sameSite("Lax")   // CSRF protection
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(HttpServletRequest request) {
        String refreshToken = getCookieValue(request, "refreshToken");

        if (refreshToken == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "Refresh token missing", null));
        }

        try {
            String newAccessToken = service.refreshAccessToken(refreshToken);
            ResponseCookie newAccessCookie = createCookie("accessToken", newAccessToken, 15 * 60);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, newAccessCookie.toString())
                    .body(new ApiResponse<>(true, "Token refreshed", "success"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // overwrite cookies with 0 maxAge to delete them
        ResponseCookie accessCookie = createCookie("accessToken", "", 0);
        ResponseCookie refreshCookie = createCookie("refreshToken", "", 0);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new ApiResponse<>(true, "Logged out successfully", null));
    }
}
