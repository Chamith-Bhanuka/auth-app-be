package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse <T>{
    // true if request success
    private boolean success;

    // message for frontend
    private String message;

    // actual data
    private T data;

    // "cache" or "database"
    private String meta;
}
