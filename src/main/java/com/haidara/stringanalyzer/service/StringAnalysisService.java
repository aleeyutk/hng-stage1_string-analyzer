package com.haidara.stringanalyzer.service;

import com.haidara.stringanalyzer.model.StringAnalysis;
import com.haidara.stringanalyzer.model.CharacterFrequency;
import com.haidara.stringanalyzer.model.dto.AnalyzeRequest;
import com.haidara.stringanalyzer.model.dto.StringAnalysisResponse;
import com.haidara.stringanalyzer.repository.StringAnalysisRepository;
import com.haidara.stringanalyzer.repository.CharacterFrequencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StringAnalysisService {
    
    @Autowired
    private StringAnalysisRepository repository;
    
    @Autowired
    private CharacterFrequencyRepository characterFrequencyRepository;
    
    public StringAnalysisResponse analyzeString(AnalyzeRequest request) {
        String input = request.getValue();
        
        // Check if string already exists
        if (repository.existsByInputValue(input)) {
            throw new RuntimeException("String already exists");
        }
        
        // Compute properties
        String sha256Hash = computeSHA256(input);
        int length = input.length();
        boolean isPalindrome = isPalindrome(input);
        int uniqueCharacters = (int) input.chars().distinct().count();
        int wordCount = input.trim().isEmpty() ? 0 : input.trim().split("\\s+").length;
        Map<String, Integer> frequencyMap = computeCharacterFrequency(input);
        
        // Create and save entity
        StringAnalysis analysis = new StringAnalysis(input, sha256Hash, length, isPalindrome, 
                                                   uniqueCharacters, wordCount, sha256Hash);
        StringAnalysis saved = repository.save(analysis);
        
        // Save character frequencies
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            CharacterFrequency cf = new CharacterFrequency(saved.getId(), entry.getKey(), entry.getValue());
            characterFrequencyRepository.save(cf);
        }
        
        return convertToResponse(saved, frequencyMap);
    }
    
    private boolean isPalindrome(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        String clean = input.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return !clean.isEmpty() && clean.equals(new StringBuilder(clean).reverse().toString());
    }
    
    private String computeSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    private Map<String, Integer> computeCharacterFrequency(String input) {
        return input.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.groupingBy(
                    c -> c,
                    Collectors.summingInt(c -> 1)
                ));
    }
    
    @Transactional(readOnly = true)
    public StringAnalysisResponse getStringAnalysis(String stringValue) {
        StringAnalysis analysis = repository.findByInputValue(stringValue)
                .orElseThrow(() -> new RuntimeException("String not found"));
        
        List<CharacterFrequency> frequencies = characterFrequencyRepository.findByAnalysisId(analysis.getId());
        Map<String, Integer> frequencyMap = frequencies.stream()
                .collect(Collectors.toMap(
                    CharacterFrequency::getCharacter,
                    CharacterFrequency::getFrequency
                ));
        
        return convertToResponse(analysis, frequencyMap);
    }
    
    @Transactional(readOnly = true)
    public List<StringAnalysisResponse> getAllStrings() {
        List<StringAnalysis> analyses = repository.findAll();
        return analyses.stream()
                .map(analysis -> {
                    List<CharacterFrequency> frequencies = characterFrequencyRepository.findByAnalysisId(analysis.getId());
                    Map<String, Integer> frequencyMap = frequencies.stream()
                            .collect(Collectors.toMap(
                                CharacterFrequency::getCharacter,
                                CharacterFrequency::getFrequency
                            ));
                    return convertToResponse(analysis, frequencyMap);
                })
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<StringAnalysisResponse> getStringsWithFilters(Boolean isPalindrome, Integer minLength, 
                                                            Integer maxLength, Integer wordCount, 
                                                            String containsCharacter) {
        List<StringAnalysis> results;
        
        if (isPalindrome != null) {
            results = repository.findByIsPalindrome(isPalindrome);
        } else if (minLength != null || maxLength != null) {
            int min = minLength != null ? minLength : 0;
            int max = maxLength != null ? maxLength : Integer.MAX_VALUE;
            results = repository.findByLengthBetween(min, max);
        } else if (wordCount != null) {
            results = repository.findByWordCount(wordCount);
        } else if (containsCharacter != null) {
            results = repository.findByCharacterInFrequency(containsCharacter);
        } else {
            results = repository.findAll();
        }
        
        // Apply additional filters
        results = results.stream()
                .filter(analysis -> isPalindrome == null || analysis.isPalindrome() == isPalindrome)
                .filter(analysis -> minLength == null || analysis.getLength() >= minLength)
                .filter(analysis -> maxLength == null || analysis.getLength() <= maxLength)
                .filter(analysis -> wordCount == null || analysis.getWordCount() == wordCount)
                .collect(Collectors.toList());
        
        return results.stream()
                .map(analysis -> {
                    List<CharacterFrequency> frequencies = characterFrequencyRepository.findByAnalysisId(analysis.getId());
                    Map<String, Integer> frequencyMap = frequencies.stream()
                            .collect(Collectors.toMap(
                                CharacterFrequency::getCharacter,
                                CharacterFrequency::getFrequency
                            ));
                    return convertToResponse(analysis, frequencyMap);
                })
                .collect(Collectors.toList());
    }
    
    public void deleteString(String stringValue) {
        StringAnalysis analysis = repository.findByInputValue(stringValue)
                .orElseThrow(() -> new RuntimeException("String not found"));
        
        // Delete character frequencies first (due to foreign key constraint)
        characterFrequencyRepository.deleteByAnalysisId(analysis.getId());
        repository.delete(analysis);
    }
    
    private StringAnalysisResponse convertToResponse(StringAnalysis analysis, Map<String, Integer> frequencyMap) {
        // Create a properties map for the response
        Map<String, Object> properties = new HashMap<>();
        properties.put("length", analysis.getLength());
        properties.put("is_palindrome", analysis.isPalindrome());
        properties.put("unique_characters", analysis.getUniqueCharacters());
        properties.put("word_count", analysis.getWordCount());
        properties.put("sha256_hash", analysis.getSha256Hash());
        properties.put("character_frequency_map", frequencyMap);
        
        return new StringAnalysisResponse(
            analysis.getId(),
            analysis.getInputValue(), // Use inputValue instead of value
            properties,
            analysis.getCreatedAt()
        );
    }
}
