package com.haidara.stringanalyzer.model;

import jakarta.persistence.*;
import java.util.Map;
import java.util.HashMap;

@Embeddable
public class AnalysisProperties {
    private int length;
    
    @Column(name = "is_palindrome")
    private boolean isPalindrome;
    
    @Column(name = "unique_characters")
    private int uniqueCharacters;
    
    @Column(name = "word_count")
    private int wordCount;
    
    @Column(name = "sha256_hash")
    private String sha256Hash;
    
    // We'll handle character frequency separately through relationships
    // This field is transient - not stored in database directly
    @Transient
    private Map<String, Integer> characterFrequencyMap;
    
    // Constructors
    public AnalysisProperties() {
        this.characterFrequencyMap = new HashMap<>();
    }
    
    public AnalysisProperties(int length, boolean isPalindrome, int uniqueCharacters, 
                             int wordCount, String sha256Hash, Map<String, Integer> characterFrequencyMap) {
        this.length = length;
        this.isPalindrome = isPalindrome;
        this.uniqueCharacters = uniqueCharacters;
        this.wordCount = wordCount;
        this.sha256Hash = sha256Hash;
        this.characterFrequencyMap = characterFrequencyMap != null ? characterFrequencyMap : new HashMap<>();
    }
    
    // Getters and Setters
    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }
    
    public boolean isPalindrome() { return isPalindrome; }
    public void setPalindrome(boolean palindrome) { isPalindrome = palindrome; }
    
    public int getUniqueCharacters() { return uniqueCharacters; }
    public void setUniqueCharacters(int uniqueCharacters) { this.uniqueCharacters = uniqueCharacters; }
    
    public int getWordCount() { return wordCount; }
    public void setWordCount(int wordCount) { this.wordCount = wordCount; }
    
    public String getSha256Hash() { return sha256Hash; }
    public void setSha256Hash(String sha256Hash) { this.sha256Hash = sha256Hash; }
    
    public Map<String, Integer> getCharacterFrequencyMap() { 
        return characterFrequencyMap != null ? characterFrequencyMap : new HashMap<>();
    }
    
    public void setCharacterFrequencyMap(Map<String, Integer> characterFrequencyMap) { 
        this.characterFrequencyMap = characterFrequencyMap != null ? characterFrequencyMap : new HashMap<>();
    }
}
