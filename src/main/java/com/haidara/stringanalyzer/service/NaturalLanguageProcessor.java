package com.haidara.stringanalyzer.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class NaturalLanguageProcessor {
    
    public Map<String, Object> parseNaturalLanguageQuery(String query) {
        Map<String, Object> filters = new HashMap<>();
        String lowerQuery = query.toLowerCase();
        
        // Parse palindrome-related queries
        if (containsAny(lowerQuery, "palindrome", "palindromic")) {
            filters.put("isPalindrome", true);
        }
        
        // Parse length-related queries
        parseLengthFilters(lowerQuery, filters);
        
        // Parse word count queries
        parseWordCountFilters(lowerQuery, filters);
        
        // Parse character presence queries
        parseCharacterFilters(lowerQuery, filters);
        
        return filters;
    }
    
    private void parseLengthFilters(String query, Map<String, Object> filters) {
        // Patterns for length queries
        Pattern longerThan = Pattern.compile("(longer than|greater than|more than)\\s+(\\d+)\\s+characters");
        Pattern shorterThan = Pattern.compile("(shorter than|less than)\\s+(\\d+)\\s+characters");
        Pattern exactLength = Pattern.compile("(exactly|precisely)\\s+(\\d+)\\s+characters");
        
        Matcher matcher = longerThan.matcher(query);
        if (matcher.find()) {
            filters.put("minLength", Integer.parseInt(matcher.group(2)) + 1);
        }
        
        matcher = shorterThan.matcher(query);
        if (matcher.find()) {
            filters.put("maxLength", Integer.parseInt(matcher.group(2)) - 1);
        }
        
        matcher = exactLength.matcher(query);
        if (matcher.find()) {
            int length = Integer.parseInt(matcher.group(2));
            filters.put("minLength", length);
            filters.put("maxLength", length);
        }
    }
    
    private void parseWordCountFilters(String query, Map<String, Object> filters) {
        if (containsAny(query, "single word", "one word")) {
            filters.put("wordCount", 1);
        } else if (containsAny(query, "two words", "double word")) {
            filters.put("wordCount", 2);
        } else if (containsAny(query, "three words", "triple word")) {
            filters.put("wordCount", 3);
        }
        
        // Parse numeric word counts
        Pattern wordCountPattern = Pattern.compile("(\\d+)\\s+words");
        Matcher matcher = wordCountPattern.matcher(query);
        if (matcher.find()) {
            filters.put("wordCount", Integer.parseInt(matcher.group(1)));
        }
    }
    
    private void parseCharacterFilters(String query, Map<String, Object> filters) {
        // Look for character mentions
        Pattern charPattern = Pattern.compile("contain(s|ing)?\\s+(?:the\\s+)?(?:letter\\s+)?([a-zA-Z])");
        Matcher matcher = charPattern.matcher(query);
        if (matcher.find()) {
            filters.put("containsCharacter", matcher.group(2).toLowerCase());
        }
        
        // Special case for vowels
        if (query.contains("vowel")) {
            if (query.contains("first vowel")) {
                filters.put("containsCharacter", "a");
            } else if (query.contains("vowel")) {
                // Default to 'a' for any vowel mention
                filters.put("containsCharacter", "a");
            }
        }
    }
    
    private boolean containsAny(String text, String... terms) {
        for (String term : terms) {
            if (text.contains(term)) {
                return true;
            }
        }
        return false;
    }
    
    public void validateFilters(Map<String, Object> filters) {
        // Check for conflicting filters
        Integer minLength = (Integer) filters.get("minLength");
        Integer maxLength = (Integer) filters.get("maxLength");
        
        if (minLength != null && maxLength != null && minLength > maxLength) {
            throw new IllegalArgumentException("Conflicting filters: minLength > maxLength");
        }
    }
}
