package com.bordercontrol.models;

import java.time.LocalDate;

public class Traveler {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private LocalDate passportExpiryDate;

    // Constructor
    public Traveler(String firstName, String lastName, LocalDate dateOfBirth, 
                   String nationality, String passportNumber, 
                   LocalDate passportIssueDate, LocalDate passportExpiryDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.passportIssueDate = passportIssueDate;
        this.passportExpiryDate = passportExpiryDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }

    public LocalDate getPassportIssueDate() { return passportIssueDate; }
    public void setPassportIssueDate(LocalDate passportIssueDate) { this.passportIssueDate = passportIssueDate; }

    public LocalDate getPassportExpiryDate() { return passportExpiryDate; }
    public void setPassportExpiryDate(LocalDate passportExpiryDate) { this.passportExpiryDate = passportExpiryDate; }
}