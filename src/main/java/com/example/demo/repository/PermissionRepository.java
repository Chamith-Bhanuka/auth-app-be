package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<UserPermission, Long> {
    List<UserPermission> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
