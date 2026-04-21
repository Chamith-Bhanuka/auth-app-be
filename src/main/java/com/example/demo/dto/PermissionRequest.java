package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class PermissionRequest {
    private Long userId;
    private List<String> permissions;
}
