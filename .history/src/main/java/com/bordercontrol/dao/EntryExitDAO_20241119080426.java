package com.bordercontrol.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bordercontrol.models.EntryExitRecord;
import com.bordercontrol.utils.DatabaseUtil;

public class EntryExitDAO {
    
    public void save(EntryExitRecord record) throws SQLException {
        String sql = """
            INSERT INTO entry_exit_records 
            (traveler_id, record_type, port_of_entry, purpose_of_travel, 
             visa_number, remarks, recorded_by)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setLong(1, record.getTravelerId());
            pstmt.setString(2, record.getRecordType());
            pstmt.setString(3, record.getPortOfEntry());
            pstmt.setString(4, record.getPurposeOfTravel());
            pstmt.setString(5, record.getVisaNumber());
            pstmt.setString(6, record.getRemarks());
            pstmt.setString(7, "SYSTEM"); // You can implement user authentication later
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    record.setId(generatedKeys.getLong(1));
                }
            }
        }
    }
}