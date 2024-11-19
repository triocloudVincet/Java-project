package com.bordercontrol.models;

import java.time.LocalDateTime;

public class EntryExitRecord {
    private Long id;
    private Long travelerId;
    private String recordType; // "ENTRY" or "EXIT"
    private LocalDateTime recordTime;
    private String portOfEntry;
    private String purposeOfTravel;
    private String visaNumber;
    private String remarks;
    private String recordedBy;

    // Constructor
    public EntryExitRecord(Long travelerId, String recordType, String portOfEntry, 
                          String purposeOfTravel, String visaNumber, String remarks) {
        this.travelerId = travelerId;
        this.recordType = recordType;
        this.recordTime = LocalDateTime.now();
        this.portOfEntry = portOfEntry;
        this.purposeOfTravel = purposeOfTravel;
        this.visaNumber = visaNumber;
        this.remarks = remarks;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTravelerId() { return travelerId; }
    public void setTravelerId(Long travelerId) { this.travelerId = travelerId; }

    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }

    public LocalDateTime getRecordTime() { return recordTime; }
    public void setRecordTime(LocalDateTime recordTime) { this.recordTime = recordTime; }

    public String getPortOfEntry() { return portOfEntry; }
    public void setPortOfEntry(String portOfEntry) { this.portOfEntry = portOfEntry; }

    public String getPurposeOfTravel() { return purposeOfTravel; }
    public void setPurposeOfTravel(String purposeOfTravel) { this.purposeOfTravel = purposeOfTravel; }

    public String getVisaNumber() { return visaNumber; }
    public void setVisaNumber(String visaNumber) { this.visaNumber = visaNumber; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getRecordedBy() { return recordedBy; }
    public void setRecordedBy(String recordedBy) { this.recordedBy = recordedBy; }
}