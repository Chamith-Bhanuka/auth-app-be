package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PermissionRequest;
import com.example.demo.dto.ServiceResult;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PermissionService;  // ← back to PermissionService
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService service;  // ← PermissionService not CacheService
    private final UserRepository userRepository;

    @PostMapping("/assign")
    public ApiResponse<String> assign(@RequestBody PermissionRequest req) {
        service.assignPermissions(req.getUserId(), req.getPermissions());
        return new ApiResponse<>(true, "Permissions Updated", null, null);
    }

    @GetMapping("/user/{id}")
    public ApiResponse<List<String>> getUserPermissions(@PathVariable Long id) {
        ServiceResult<List<String>> result = service.getPermissionsWithSource(id);
        return new ApiResponse<>(true, "Permissions fetched", result.getData(), result.getSource());
    }

    @GetMapping("/my")
    public ApiResponse<List<String>> getMyPermissions() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ServiceResult<List<String>> result = service.getPermissionsWithSource(user.getId());

        return new ApiResponse<>(true, "Permissions fetched",
                result.getData(), result.getSource());
    }
}