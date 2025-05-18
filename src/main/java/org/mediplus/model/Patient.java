package org.mediplus.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Patient extends User {
    private String insuranceId; // Health insurance number

    // Constructor
    public Patient(String username, String email, String password, String role, String insuranceId) {
        super(null, username, email, password, role);
        this.insuranceId = insuranceId;
    }
}