package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.service.PermissionCacheService;
import com.example.demo.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PermissionService permissionService;
    private final RedisConnectionFactory redisConnectionFactory;

    @GetMapping("/cache/all")
    public ApiResponse<Map<String, Object>> getFullCache() {
        Map<String, Object> data = permissionService.getAllCachedValues();
        return new ApiResponse<>(true, "All cached permissions retrieved", data, null);
    }

    @DeleteMapping("/cache/flush")
    public ApiResponse<String> flushCache() {
        redisConnectionFactory.getConnection().serverCommands().flushDb();
        return new ApiResponse<>(true, "Redis Flushed", null, null);
    }
}
