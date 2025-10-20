package com.haidara.stringanalyzer.model.dto;

import java.util.List;
import java.util.Map;

public class FilterResponse {
    private List<StringAnalysisResponse> data;
    private int count;
    private Map<String, Object> filtersApplied;
    
    public FilterResponse() {}
    
    public FilterResponse(List<StringAnalysisResponse> data, Map<String, Object> filtersApplied) {
        this.data = data;
        this.count = data.size();
        this.filtersApplied = filtersApplied;
    }
    
    // Getters and Setters
    public List<StringAnalysisResponse> getData() { return data; }
    public void setData(List<StringAnalysisResponse> data) { 
        this.data = data; 
        this.count = data != null ? data.size() : 0;
    }
    
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    
    public Map<String, Object> getFiltersApplied() { return filtersApplied; }
    public void setFiltersApplied(Map<String, Object> filtersApplied) { this.filtersApplied = filtersApplied; }
}
