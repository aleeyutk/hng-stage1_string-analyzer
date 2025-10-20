package com.haidara.stringanalyzer.repository;

import com.haidara.stringanalyzer.model.StringAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StringAnalysisRepository extends JpaRepository<StringAnalysis, String> {
    
    Optional<StringAnalysis> findByInputValue(String inputValue);
    boolean existsByInputValue(String inputValue);
    
    // Custom query methods for filtering
    List<StringAnalysis> findByIsPalindrome(boolean isPalindrome);
    List<StringAnalysis> findByLengthBetween(int minLength, int maxLength);
    List<StringAnalysis> findByWordCount(int wordCount);
    
    @Query("SELECT sa FROM StringAnalysis sa WHERE sa.id IN " +
           "(SELECT cf.analysisId FROM CharacterFrequency cf WHERE cf.character = :character)")
    List<StringAnalysis> findByCharacterInFrequency(@Param("character") String character);
}
