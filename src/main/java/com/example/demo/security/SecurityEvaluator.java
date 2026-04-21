package com.example.demo.security;

import com.example.demo.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ps")
@RequiredArgsConstructor
public class SecurityEvaluator {
    private final PermissionService permissionService;

    public boolean check(Authentication auth, String path) {
        if (auth == null || !auth.isAuthenticated()) return false;

        // bypass for SUPER_ADMIN
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            return true;
        }

        List<String> allowedPaths = permissionService.getPermissionsByEmail(auth.getName());
        return allowedPaths.contains(path);
    }
}
