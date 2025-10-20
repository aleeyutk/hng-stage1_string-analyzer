package com.haidara.stringanalyzer.repository;

import com.haidara.stringanalyzer.model.CharacterFrequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterFrequencyRepository extends JpaRepository<CharacterFrequency, Long> {
    
    List<CharacterFrequency> findByAnalysisId(String analysisId);
    void deleteByAnalysisId(String analysisId);
}
