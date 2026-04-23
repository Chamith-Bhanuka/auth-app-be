package com.example.demo.service;

import com.example.demo.dto.ServiceResult;
import com.example.demo.entity.UserPermission;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionCacheService {
    private final PermissionRepository permissionRepo;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;


    @Cacheable(value = "permissions", key = "#userId")
    public List<String> getPermissionsByUserId(Long userId) {
        System.out.println("🔴 Cache Miss! DB call for user " + userId);

        return permissionRepo.findByUserId(userId).stream()
                .map(UserPermission::getEndpointPath)
                .toList();
    }


//    @Transactional
//    @CacheEvict(value = "permissions", key = "#userId")
//    public void assignPermissions(Long userId, List<String> paths) {
//        // Delete existing from DB
//        permissionRepo.deleteByUserId(userId);
//
//        // Map and Save new to DB
//        List<UserPermission> list = paths.stream().map(path -> {
//            UserPermission p = new UserPermission();
//            p.setUserId(userId);
//            p.setEndpointPath(path);
//            return p;
//        }).toList();
//
//        permissionRepo.saveAll(list);
//        System.out.println("Cache Evicted and DB Updated for User ID: " + userId);
//    }
//
//    public ServiceResult<List<String>> getPermissionsWithSource(Long userId) {
//        String cacheKey = "permissions::" + userId;
//        boolean inCache = Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey));
//
//        List<String> permissions = getPermissionsByUserId(userId);
//        String source = inCache ? "cache" : "database";
//
//        return new ServiceResult<>(permissions, source);
//    }
//
//
//    public Map<String, Object> getAllCachedValues() {
//        Map<String, Object> cacheData = new HashMap<>();
//        // Search for the standard Spring Cache prefix
//        Set<String> keys = redisTemplate.keys("permissions::*");
//
//        if (keys != null) {
//            for (String key : keys) {
//                Object value = redisTemplate.opsForValue().get(key);
//                cacheData.put(key, value);
//            }
//        }
//        return cacheData;
//    }
}