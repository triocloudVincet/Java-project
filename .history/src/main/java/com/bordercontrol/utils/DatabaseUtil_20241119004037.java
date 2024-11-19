package com.bordercontrol.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/border_control";
    private static final String USER = "kiroko";
    private static final String PASSWORD = "4732"; // Change this

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createTables() {
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

        try (Connection conn = getConnection()) {
            conn.createStatement().execute(createTravelersTable);
            System.out.println("Database tables created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating database tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}