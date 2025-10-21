package com.haidara.stringanalyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    
    @Autowired
    private ApplicationAvailability availability;
    
    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "String Analyzer API is running");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("liveness", availability.getLivenessState().toString());
        response.put("readiness", availability.getReadinessState().toString());
        return response;
    }
    
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("service", "String Analyzer API");
        return response;
    }
    
    @GetMapping("/")
    public Map<String, Object> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "String Analyzer API");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("endpoints", Map.of(
            "POST /strings", "Analyze a string",
            "GET /strings/{value}", "Get string analysis",
            "GET /strings", "Get all strings with filtering",
            "GET /strings/filter-by-natural-language", "Natural language filtering",
            "DELETE /strings/{value}", "Delete string analysis",
            "GET /health", "Health check",
            "GET /test", "Service status"
        ));
        return response;
    }
}
