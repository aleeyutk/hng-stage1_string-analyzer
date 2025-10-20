package com.haidara.stringanalyzer.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AnalyzeRequest {
    @NotBlank(message = "Value field is required")
    @NotNull(message = "Value field must not be null")
    private String value;
    
    public AnalyzeRequest() {}
    
    public AnalyzeRequest(String value) {
        this.value = value;
    }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
