package com.bordercontrol.dao;

import com.bordercontrol.models.Traveler;
import com.bordercontrol.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TravelerDAO {
    
    public void save(Traveler traveler) throws SQLException {
        String sql = """
            INSERT INTO travelers (first_name, last_name, date_of_birth, nationality, 
                                 passport_number, passport_issue_date, passport_expiry_date)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, traveler.getFirstName());
            pstmt.setString(2, traveler.getLastName());
            pstmt.setDate(3, Date.valueOf(traveler.getDateOfBirth()));
            pstmt.setString(4, traveler.getNationality());
            pstmt.setString(5, traveler.getPassportNumber());
            pstmt.setDate(6, Date.valueOf(traveler.getPassportIssueDate()));
            pstmt.setDate(7, Date.valueOf(traveler.getPassportExpiryDate()));
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    traveler.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public Traveler findByPassportNumber(String passportNumber) throws SQLException {
        String sql = "SELECT * FROM travelers WHERE passport_number = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, passportNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTraveler(rs);
                }
            }
        }
        return null;
    }

    private Traveler mapResultSetToTraveler(ResultSet rs) throws SQLException {
        Traveler traveler = new Traveler(
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getDate("date_of_birth").toLocalDate(),
            rs.getString("nationality"),
            rs.getString("passport_number"),
            rs.getDate("passport_issue_date").toLocalDate(),
            rs.getDate("passport_expiry_date").toLocalDate()
        );
        traveler.setId(rs.getLong("id"));
        return traveler;
    }
}