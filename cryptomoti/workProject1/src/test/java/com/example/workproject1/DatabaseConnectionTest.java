package com.example.workproject1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testDatabaseConnection() {
        // Test basic database connection
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertEquals(1, result);
        System.out.println("✅ Database connection successful");
    }

    @Test
    public void testUsersTableExists() {
        // Check if users table exists
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'users'", 
                Integer.class
            );
            assertTrue(count > 0, "Users table should exist");
            System.out.println("✅ Users table exists");
        } catch (Exception e) {
            System.out.println("❌ Users table does not exist: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testUsersTableStructure() {
        // Check if users table has the required columns
        try {
            jdbcTemplate.queryForObject(
                "SELECT id, first_name, last_name, email, passwordHash, salt FROM users LIMIT 1", 
                (rs, rowNum) -> {
                    // If this executes without exception, the columns exist
                    return "OK";
                }
            );
            System.out.println("✅ Users table has correct structure");
        } catch (Exception e) {
            System.out.println("❌ Users table structure issue: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testInsertUser() {
        // Test inserting a user
        try {
            String sql = "INSERT INTO users (first_name, last_name, email, passwordHash, salt) VALUES (?, ?, ?, ?, ?)";
            int result = jdbcTemplate.update(sql, "Test", "User", "test@example.com", "hash123", "salt123");
            assertEquals(1, result);
            System.out.println("✅ User insertion successful");
            
            // Clean up
            jdbcTemplate.update("DELETE FROM users WHERE email = ?", "test@example.com");
            System.out.println("✅ Test user cleaned up");
        } catch (Exception e) {
            System.out.println("❌ User insertion failed: " + e.getMessage());
            throw e;
        }
    }
}
