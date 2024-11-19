package com.bordercontrol.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.bordercontrol.models.FlaggedTraveler;
import com.bordercontrol.utils.DatabaseUtil;

public class FlaggedTravelerDAO {
    
    public void save(FlaggedTraveler flag) throws SQLException {
        String sql = """
            INSERT INTO flagged_travelers 
            (traveler_id, flag_type, flag_reason, flagged_by, status)
            VALUES (?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setLong(1, flag.getTravelerId());
            pstmt.setString(2, flag.getFlagType());
            pstmt.setString(3, flag.getFlagReason());
            pstmt.setString(4, "SYSTEM"); // Replace with actual user when authentication is implemented
            pstmt.setString(5, flag.getStatus());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    flag.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public void updateStatus(Long flagId, String status, String resolutionNotes) throws SQLException {
        String sql = """
            UPDATE flagged_travelers 
            SET status = ?, 
                resolution_notes = ?,
                resolved_by = ?,
                resolved_date = CURRENT_TIMESTAMP
            WHERE id = ?
        """;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, resolutionNotes);
            pstmt.setString(3, "SYSTEM"); // Replace with actual user
            pstmt.setLong(4, flagId);
            
            pstmt.executeUpdate();
        }
    }

    public List<FlaggedTraveler> findByTravelerId(Long travelerId) throws SQLException {
        String sql = """
            SELECT * FROM flagged_travelers 
            WHERE traveler_id = ? 
            ORDER BY flag_date DESC
        """;
        
        List<FlaggedTraveler> flags = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, travelerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    flags.add(mapResultSetToFlag(rs));
                }
            }
        }
        return flags;
    }

    private FlaggedTraveler mapResultSetToFlag(ResultSet rs) throws SQLException {
        FlaggedTraveler flag = new FlaggedTraveler(
            rs.getLong("traveler_id"),
            rs.getString("flag_type"),
            rs.getString("flag_reason")
        );
        
        flag.setId(rs.getLong("id"));
        flag.setFlaggedBy(rs.getString("flagged_by"));
        flag.setFlagDate(rs.getTimestamp("flag_date").toLocalDateTime());
        flag.setStatus(rs.getString("status"));
        
        Timestamp resolvedDate = rs.getTimestamp("resolved_date");
        if (resolvedDate != null) {
            flag.setResolvedDate(resolvedDate.toLocalDateTime());
            flag.setResolvedBy(rs.getString("resolved_by"));
            flag.setResolutionNotes(rs.getString("resolution_notes"));
        }
        
        return flag;
    }
}