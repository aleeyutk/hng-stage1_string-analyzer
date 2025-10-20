package com.haidara.stringanalyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    
    @Autowired
    private ApplicationAvailability availability;
    
    @GetMapping("/test")
    public String test() {
        return "String Analyzer API is running!";
    }
    
    @GetMapping("/test/db")
    public String testDatabase() {
        return "In-memory storage is active";
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        AvailabilityState livenessState = availability.getLivenessState();
        AvailabilityState readinessState = availability.getReadinessState();
        
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", Instant.now().toString());
        health.put("application", "String Analyzer API");
        health.put("liveness", livenessState.toString());
        health.put("readiness", readinessState.toString());
        
        return ResponseEntity.ok(health);
    }
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> info = new HashMap<>();
        info.put("message", "String Analyzer API");
        info.put("version", "1.0.0");
        info.put("timestamp", Instant.now().toString());
        info.put("endpoints", Map.of(
            "POST /strings", "Analyze a string",
            "GET /strings/{value}", "Get string analysis",
            "GET /strings", "Get all strings with filtering",
            "GET /strings/filter-by-natural-language", "Natural language filtering",
            "DELETE /strings/{value}", "Delete string analysis",
            "GET /health", "Health check",
            "GET /test", "Simple test"
        ));
        
        return ResponseEntity.ok(info);
    }
}
