package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PermissionRequest;
import com.example.demo.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService service;

    @PostMapping("/assign")
    public ApiResponse<String> assign(@RequestBody PermissionRequest req) {
        service.assignPermissions(req.getUserId(), req.getPermissions());
        return new ApiResponse<>(true, "Permissions Updated", null);
    }

    @GetMapping("/user/{id}")
    public List<String> getUserPermissions(@PathVariable Long id) {
        return service.getPermissionsByUserId(id);
    }

    @GetMapping("/my")
    public List<String> getMyPermissions() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getPermissionsByEmail(email);
    }
}
