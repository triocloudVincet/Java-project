package com.bordercontrol.models;

import java.time.LocalDateTime;

public class FlaggedTraveler {
    private Long id;
    private Long travelerId;
    private String flagType;
    private String flagReason;
    private String flaggedBy;
    private LocalDateTime flagDate;
    private String status;
    private String resolutionNotes;
    private String resolvedBy;
    private LocalDateTime resolvedDate;

    public FlaggedTraveler(Long travelerId, String flagType, String flagReason) {
        this.travelerId = travelerId;
        this.flagType = flagType;
        this.flagReason = flagReason;
        this.flagDate = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTravelerId() { return travelerId; }
    public void setTravelerId(Long travelerId) { this.travelerId = travelerId; }

    public String getFlagType() { return flagType; }
    public void setFlagType(String flagType) { this.flagType = flagType; }

    public String getFlagReason() { return flagReason; }
    public void setFlagReason(String flagReason) { this.flagReason = flagReason; }

    public String getFlaggedBy() { return flaggedBy; }
    public void setFlaggedBy(String flaggedBy) { this.flaggedBy = flaggedBy; }

    public LocalDateTime getFlagDate() { return flagDate; }
    public void setFlagDate(LocalDateTime flagDate) { this.flagDate = flagDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public String getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; }

    public LocalDateTime getResolvedDate() { return resolvedDate; }
    public void setResolvedDate(LocalDateTime resolvedDate) { this.resolvedDate = resolvedDate; }
}