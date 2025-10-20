package com.haidara.stringanalyzer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "string_analysis")
public class StringAnalysis {
    @Id
    @Column(name = "id")
    private String id; // SHA-256 hash
    
    @Column(name = "input_value", nullable = false, length = 10000)
    private String inputValue;
    
    @Column(name = "length")
    private int length;
    
    @Column(name = "is_palindrome")
    private boolean isPalindrome;
    
    @Column(name = "unique_characters")
    private int uniqueCharacters;
    
    @Column(name = "word_count")
    private int wordCount;
    
    @Column(name = "sha256_hash")
    private String sha256Hash;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public StringAnalysis() {}
    
    public StringAnalysis(String inputValue, String id, int length, boolean isPalindrome, 
                         int uniqueCharacters, int wordCount, String sha256Hash) {
        this.inputValue = inputValue;
        this.id = id;
        this.length = length;
        this.isPalindrome = isPalindrome;
        this.uniqueCharacters = uniqueCharacters;
        this.wordCount = wordCount;
        this.sha256Hash = sha256Hash;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getInputValue() { return inputValue; }
    public void setInputValue(String inputValue) { this.inputValue = inputValue; }
    
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
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
