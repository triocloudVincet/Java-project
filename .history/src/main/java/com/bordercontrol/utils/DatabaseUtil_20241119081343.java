package com.bordercontrol.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/border_control";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Set your MySQL password here

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createTables() {
        // Travelers table
        String createTravelersTable = """
            CREATE TABLE IF NOT EXISTS travelers (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                first_name VARCHAR(50) NOT NULL,
                last_name VARCHAR(50) NOT NULL,
                date_of_birth DATE NOT NULL,
                nationality VARCHAR(50) NOT NULL,
                passport_number VARCHAR(20) UNIQUE NOT NULL,
                passport_issue_date DATE NOT NULL,
                passport_expiry_date DATE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            );
        """;

        // Entry/Exit Records table
        String createEntryExitTable = """
            CREATE TABLE IF NOT EXISTS entry_exit_records (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                traveler_id BIGINT NOT NULL,
                record_type ENUM('ENTRY', 'EXIT') NOT NULL,
                record_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                port_of_entry VARCHAR(100) NOT NULL,
                purpose_of_travel VARCHAR(200),
                visa_number VARCHAR(50),
                remarks TEXT,
                recorded_by VARCHAR(100),
                FOREIGN KEY (traveler_id) REFERENCES travelers(id)
            );
        """;

        try (Connection conn = getConnection()) {
            conn.createStatement().execute(createTravelersTable);
            conn.createStatement().execute(createEntryExitTable);
            System.out.println("Database tables created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating database tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}