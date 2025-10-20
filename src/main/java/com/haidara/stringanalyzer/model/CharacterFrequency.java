package com.haidara.stringanalyzer.model;

import jakarta.persistence.*;

@Entity
@Table(name = "character_frequency")
public class CharacterFrequency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "analysis_id")
    private String analysisId;
    
    @Column(name = "character_value")
    private String character;
    
    @Column(name = "frequency")
    private Integer frequency;
    
    public CharacterFrequency() {}
    
    public CharacterFrequency(String analysisId, String character, Integer frequency) {
        this.analysisId = analysisId;
        this.character = character;
        this.frequency = frequency;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getAnalysisId() { return analysisId; }
    public void setAnalysisId(String analysisId) { this.analysisId = analysisId; }
    
    public String getCharacter() { return character; }
    public void setCharacter(String character) { this.character = character; }
    
    public Integer getFrequency() { return frequency; }
    public void setFrequency(Integer frequency) { this.frequency = frequency; }
}
