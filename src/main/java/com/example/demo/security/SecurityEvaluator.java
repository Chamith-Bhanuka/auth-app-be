package com.example.demo.security;

import com.example.demo.dto.ServiceResult;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PermissionCacheService;
import com.example.demo.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Component("ps")
@RequiredArgsConstructor
public class SecurityEvaluator {

    private final PermissionService permissionService;
    private final PermissionCacheService permissionCacheService; // ← add this
    private final UserRepository userRepository;

    public boolean check(Authentication auth, String path) {

        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            // ✅ Use RequestContextHolder instead
            setSource("super_admin_bypass");
            return true;
        }

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ServiceResult<List<String>> result =
                permissionService.getPermissionsWithSource(user.getId());

        setSource(result.getSource());

        return result.getData().contains(path);
    }

    private void setSource(String source) {
        ((HttpServletRequest) ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes())
                .getRequest())
                .setAttribute("permissionSource", source);
    }
}