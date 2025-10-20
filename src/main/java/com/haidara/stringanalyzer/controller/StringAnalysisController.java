package com.haidara.stringanalyzer.controller;

import com.haidara.stringanalyzer.model.dto.*;
import com.haidara.stringanalyzer.service.StringAnalysisService;
import com.haidara.stringanalyzer.service.NaturalLanguageProcessor;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/strings")
public class StringAnalysisController {
    
    @Autowired
    private StringAnalysisService analysisService;
    
    @Autowired
    private NaturalLanguageProcessor naturalLanguageProcessor;
    
    // 1. Create/Analyze String
    @PostMapping
    public ResponseEntity<StringAnalysisResponse> analyzeString(@Valid @RequestBody AnalyzeRequest request) {
        try {
            StringAnalysisResponse response = analysisService.analyzeString(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("String already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            throw e;
        }
    }
    
    // 2. Get Specific String
    @GetMapping("/{stringValue}")
    public ResponseEntity<StringAnalysisResponse> getStringAnalysis(@PathVariable String stringValue) {
        try {
            StringAnalysisResponse response = analysisService.getStringAnalysis(stringValue);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("String not found")) {
                return ResponseEntity.notFound().build();
            }
            throw e;
        }
    }
    
    // 3. Get All Strings with Filtering
    @GetMapping
    public ResponseEntity<FilterResponse> getAllStrings(
            @RequestParam(required = false) Boolean is_palindrome,
            @RequestParam(required = false) Integer min_length,
            @RequestParam(required = false) Integer max_length,
            @RequestParam(required = false) Integer word_count,
            @RequestParam(required = false) String contains_character) {
        
        try {
            List<StringAnalysisResponse> results = analysisService.getStringsWithFilters(
                is_palindrome, min_length, max_length, word_count, contains_character);
            
            Map<String, Object> filtersApplied = new HashMap<>();
            if (is_palindrome != null) filtersApplied.put("is_palindrome", is_palindrome);
            if (min_length != null) filtersApplied.put("min_length", min_length);
            if (max_length != null) filtersApplied.put("max_length", max_length);
            if (word_count != null) filtersApplied.put("word_count", word_count);
            if (contains_character != null) filtersApplied.put("contains_character", contains_character);
            
            FilterResponse response = new FilterResponse(results, filtersApplied);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 4. Natural Language Filtering
    @GetMapping("/filter-by-natural-language")
    public ResponseEntity<Map<String, Object>> filterByNaturalLanguage(
            @RequestParam String query) {
        
        try {
            Map<String, Object> parsedFilters = naturalLanguageProcessor.parseNaturalLanguageQuery(query);
            naturalLanguageProcessor.validateFilters(parsedFilters);
            
            // Extract filters for service call
            Boolean isPalindrome = (Boolean) parsedFilters.get("isPalindrome");
            Integer minLength = (Integer) parsedFilters.get("minLength");
            Integer maxLength = (Integer) parsedFilters.get("maxLength");
            Integer wordCount = (Integer) parsedFilters.get("wordCount");
            String containsCharacter = (String) parsedFilters.get("containsCharacter");
            
            List<StringAnalysisResponse> results = analysisService.getStringsWithFilters(
                isPalindrome, minLength, maxLength, wordCount, containsCharacter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("data", results);
            response.put("count", results.size());
            response.put("interpreted_query", Map.of(
                "original", query,
                "parsed_filters", parsedFilters
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to parse natural language query");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // 5. Delete String
    @DeleteMapping("/{stringValue}")
    public ResponseEntity<Void> deleteString(@PathVariable String stringValue) {
        try {
            analysisService.deleteString(stringValue);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("String not found")) {
                return ResponseEntity.notFound().build();
            }
            throw e;
        }
    }
}
