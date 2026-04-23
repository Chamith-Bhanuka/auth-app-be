package com.example.demo.service;

import com.example.demo.dto.ServiceResult;
import com.example.demo.entity.UserPermission;
import com.example.demo.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepo;
    private final PermissionCacheService permissionCacheService; // ← inject separate bean
    private final RedisTemplate<String, Object> redisTemplate;

    // ✅ Calls external bean — proxy works — cache triggers correctly
    public ServiceResult<List<String>> getPermissionsWithSource(Long userId) {
        String cacheKey = "permissions::" + userId;
        boolean inCache = Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey));

        // external call → proxy intercepts → @Cacheable works
        List<String> permissions = permissionCacheService.getPermissionsByUserId(userId);
        String source = inCache ? "cache" : "database";

        return new ServiceResult<>(permissions, source);
    }

    // Delegate to cache service for simple lookups
    public List<String> getPermissionsByUserId(Long userId) {
        return permissionCacheService.getPermissionsByUserId(userId);
    }

    @Transactional
    @CacheEvict(value = "permissions", key = "#userId")
    public void assignPermissions(Long userId, List<String> paths) {
        permissionRepo.deleteByUserId(userId);

        List<UserPermission> list = paths.stream().map(path -> {
            UserPermission p = new UserPermission();
            p.setUserId(userId);
            // normalize slash
            p.setEndpointPath(path.startsWith("/") ? path : "/" + path);
            return p;
        }).toList();

        permissionRepo.saveAll(list);
        System.out.println("✅ Cache Evicted and DB Updated for userId: " + userId);
    }

    public Map<String, Object> getAllCachedValues() {
        Map<String, Object> cacheData = new HashMap<>();
        Set<String> keys = redisTemplate.keys("permissions::*");
        if (keys != null) {
            for (String key : keys) {
                Object value = redisTemplate.opsForValue().get(key);
                cacheData.put(key, value);
            }
        }
        return cacheData;
    }
}
