package com.haidara.stringanalyzer.model.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class StringAnalysisResponse {
    private String id;
    private String value;
    private Map<String, Object> properties;
    private LocalDateTime createdAt;
    
    public StringAnalysisResponse() {}
    
    public StringAnalysisResponse(String id, String value, Map<String, Object> properties, LocalDateTime createdAt) {
        this.id = id;
        this.value = value;
        this.properties = properties;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
