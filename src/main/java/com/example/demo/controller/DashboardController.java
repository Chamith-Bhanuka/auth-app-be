package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    // ---------------------------------------------------------
    // super admin only endpoints (7 total for Super Admin)

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/system-metrics")
    public String systemMetrics() {
        return "System metrics visible only to SUPER_ADMIN";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/manage-tenants")
    public String manageTenants() {
        return "Tenant management visible only to SUPER_ADMIN";
    }

    // ---------------------------------------------------------
    // super admin + tenant (5 total for Tenant)

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT','EMPLOYEE')")
    @GetMapping("/tenant-reports")
    public String tenantReports() {
        return "Tenant reports visible to SUPER_ADMIN and TENANT";
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT','EMPLOYEE')")
    @GetMapping("/manage-employees")
    public String manageEmployees() {
        return "Employee management visible to SUPER_ADMIN, TENANT, EMPLOYEE";
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT')")
    @GetMapping("/tenant-settings")
    public String tenantSettings() {
        return "Tenant settings visible to SUPER_ADMIN, TENANT, EMPLOYEE";
    }

    // ---------------------------------------------------------
    // super admin + tenant + employee (4 total for Employee + 1 extra)

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT','EMPLOYEE')")
    @GetMapping("/employee-tasks")
    public String employeeTasks() {
        return "Employee tasks visible to SUPER_ADMIN, TENANT, EMPLOYEE";
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT','EMPLOYEE')")
    @GetMapping("/employee-profile")
    public String employeeProfile() {
        return "Employee profile visible to SUPER_ADMIN, TENANT, EMPLOYEE";
    }

    // ---------------------------------------------------------
    // customer (1 total for Customer)

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT','EMPLOYEE','CUSTOMER')")
    @GetMapping("/customer-home")
    public String customerHome() {
        return "Customer home visible to all authenticated users";
    }
}
