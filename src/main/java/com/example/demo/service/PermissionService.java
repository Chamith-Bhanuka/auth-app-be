package com.example.demo.service;

import com.example.demo.entity.UserPermission;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepo;
    private final UserRepository userRepository;

    // hits Redis cache. If empty, runs DB query and saves to Redis.
    @Cacheable(value = "permissions", key = "#userId")
    public List<String> getPermissionsByUserId(Long userId) {
        return permissionRepo.findByUserId(userId).stream()
                .map(UserPermission::getEndpointPath)
                .toList();
    }

    public List<String> getPermissionsByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return getPermissionsByUserId(user.getId());
    }

    @Transactional
    @CacheEvict(value = "permissions", key = "#userId")
    public void assignPermissions(Long userId, List<String> paths) {
        permissionRepo.deleteByUserId(userId);

        List<UserPermission> list = paths.stream().map(path -> {
            UserPermission p = new UserPermission();
            p.setUserId(userId);
            p.setEndpointPath(path);
            return p;
        }).toList();

        permissionRepo.saveAll(list);
    }

    public boolean hasAccess(Long userId, String currentPath) {
        return getPermissionsByUserId(userId).contains(currentPath);
    }
}
