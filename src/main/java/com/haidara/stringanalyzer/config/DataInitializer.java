package com.haidara.stringanalyzer.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private DataSource dataSource;
    
    @PostConstruct
    public void initialize() {
        logger.info("Initializing database...");
        try (Connection connection = dataSource.getConnection()) {
            // Create tables using plain SQL
            String createTablesSQL = 
                "CREATE TABLE IF NOT EXISTS string_analysis (" +
                "    id VARCHAR(255) PRIMARY KEY," +
                "    input_value CLOB NOT NULL," +
                "    length INT NOT NULL," +
                "    is_palindrome BOOLEAN NOT NULL," +
                "    unique_characters INT NOT NULL," +
                "    word_count INT NOT NULL," +
                "    sha256_hash VARCHAR(255) NOT NULL," +
                "    created_at TIMESTAMP NOT NULL" +
                ");" +
                "CREATE TABLE IF NOT EXISTS character_frequency (" +
                "    id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "    analysis_id VARCHAR(255) NOT NULL," +
                "    character_value VARCHAR(10) NOT NULL," +
                "    frequency INT NOT NULL," +
                "    FOREIGN KEY (analysis_id) REFERENCES string_analysis(id) ON DELETE CASCADE" +
                ");";
            
            // Split and execute each statement
            String[] statements = createTablesSQL.split(";");
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    connection.createStatement().execute(statement.trim());
                }
            }
            
            logger.info("Database initialized successfully");
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}
