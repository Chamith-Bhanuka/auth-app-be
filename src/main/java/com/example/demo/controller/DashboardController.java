package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    // ---------------------------------------------------------
    // Super Admin Level Endpoints

    @PreAuthorize("@ps.check(authentication, '/dashboard/system-metrics')")
    @GetMapping("/system-metrics")
    public ApiResponse<String> systemMetrics() {
        return new ApiResponse<>(true, "Success", "System metrics visible only to authorized users");
    }

    @PreAuthorize("@ps.check(authentication, '/dashboard/manage-tenants')")
    @GetMapping("/manage-tenants")
    public ApiResponse<String> manageTenants() {
        return new ApiResponse<>(true, "Success", "Tenant management visible only to authorized users");
    }

    // ---------------------------------------------------------
    // Tenant & Management Level Endpoints

    @PreAuthorize("@ps.check(authentication, '/dashboard/tenant-reports')")
    @GetMapping("/tenant-reports")
    public ApiResponse<String> tenantReports() {
        return new ApiResponse<>(true, "Success", "Tenant reports retrieved successfully");
    }

    @PreAuthorize("@ps.check(authentication, '/dashboard/manage-employees')")
    @GetMapping("/manage-employees")
    public ApiResponse<String> manageEmployees() {
        return new ApiResponse<>(true, "Success", "Employee management data access granted");
    }

    @PreAuthorize("@ps.check(authentication, '/dashboard/tenant-settings')")
    @GetMapping("/tenant-settings")
    public ApiResponse<String> tenantSettings() {
        return new ApiResponse<>(true, "Success", "Tenant settings access granted");
    }

    // ---------------------------------------------------------
    // Employee Level Endpoints

    @PreAuthorize("@ps.check(authentication, '/dashboard/employee-tasks')")
    @GetMapping("/employee-tasks")
    public ApiResponse<String> employeeTasks() {
        return new ApiResponse<>(true, "Success", "Task list loaded successfully");
    }

    @PreAuthorize("@ps.check(authentication, '/dashboard/employee-profile')")
    @GetMapping("/employee-profile")
    public ApiResponse<String> employeeProfile() {
        return new ApiResponse<>(true, "Success", "Profile information retrieved");
    }

    // ---------------------------------------------------------
    // General Customer/Authenticated Level Endpoint

    @PreAuthorize("@ps.check(authentication, '/dashboard/customer-home')")
    @GetMapping("/customer-home")
    public ApiResponse<String> customerHome() {
        return new ApiResponse<>(true, "Success", "Welcome to the customer dashboard home");
    }
}