package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// controller/DashboardController.java
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final HttpServletRequest request;

    private String getSource() {
        Object source = request.getAttribute("permissionSource");
        return source != null ? source.toString() : "unknown";
    }

    @PreAuthorize("@ps.check(authentication, '/dashboard/system-metrics')")
    @GetMapping("/system-metrics")
    public ApiResponse<String> systemMetrics() {
        return new ApiResponse<>(true, "Success",
                "System metrics visible only to authorized users", getSource());
    }

    @PreAuthorize("@ps.check(authentication, '/dashboard/manage-tenants')")
    @GetMapping("/manage-tenants")
    public ApiResponse<String> manageTenants() {
        return new ApiResponse<>(true, "Success",
                "Tenant management visible only to authorized users", getSource());
    }

    @PreAuthorize("@ps.check(authentication, '/dashboard/tenant-reports')")
    @GetMapping("/tenant-reports")
    public ApiResponse<String> tenantReports() {
        return new ApiResponse<>(true, "Success",
                "Tenant reports retrieved successfully", getSource());
    }

    @PreAuthorize("@ps.check(authentication, '/dashboard/manage-employees')")
    @GetMapping("/manage-employees")
    public ApiResponse<String> manageEmployees() {
        return new ApiResponse<>(true, "Success",
                "Employee management data access granted", getSource());
    }

    @PreAuthorize("@ps.check(authentication, '/dashboard/tenant-settings')")
    @GetMapping("/tenant-settings")
    public ApiResponse<String> tenantSettings() {
        return new ApiResponse<>(true, "Success",
                "Tenant settings access granted", getSource());
    }

    @PreAuthorize("@ps.check(authentication, '/dashboard/employee-tasks')")
    @GetMapping("/employee-tasks")
    public ApiResponse<String> employeeTasks() {
        return new ApiResponse<>(true, "Success",
                "Task list loaded successfully", getSource());
    }

    @PreAuthorize("@ps.check(authentication, '/dashboard/employee-profile')")
    @GetMapping("/employee-profile")
    public ApiResponse<String> employeeProfile() {
        return new ApiResponse<>(true, "Success",
                "Profile information retrieved", getSource());
    }

    @PreAuthorize("@ps.check(authentication, '/dashboard/customer-home')")
    @GetMapping("/customer-home")
    public ApiResponse<String> customerHome() {
        return new ApiResponse<>(true, "Success",
                "Welcome to the customer dashboard home", getSource());
    }
}