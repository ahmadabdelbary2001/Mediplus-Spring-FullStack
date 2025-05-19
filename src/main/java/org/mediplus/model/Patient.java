package org.mediplus.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class Patient extends User {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String insuranceId; // Health insurance number

    // Constructors
    public Patient() {
        super();
        this.setRole("PATIENT");
    }

    public Patient(String username, String email, String password, Date dateOfBirth, String insuranceId) {
        super(username, email, password, "PATIENT");
        this.dateOfBirth = dateOfBirth;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}