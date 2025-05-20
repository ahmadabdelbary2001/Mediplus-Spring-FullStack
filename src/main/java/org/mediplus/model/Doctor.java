package org.mediplus.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Doctor extends User {
    private String specialization;
    private String licenseNumber;
    private String clinicLocation;
//    private List<LocalDateTime> availableSlots; // times the doctor is available

    public Doctor() {
        super();
        this.setRole("DOCTOR");
    }

    public Doctor(String username,
                  String email,
                  String password,
                  String specialization,
                  String licenseNumber,
                  String clinicLocation) {
        super(username, email, password, "DOCTOR");
        this.specialization = specialization;
        this.licenseNumber  = licenseNumber;
        this.clinicLocation = clinicLocation;
//        this.availableSlots = availableSlots;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getClinicLocation() {
        return clinicLocation;
    }

    public void setClinicLocation(String clinicLocation) {
        this.clinicLocation = clinicLocation;
    }

//    public List<LocalDateTime> getAvailableSlots() {
//        return availableSlots;
//    }
//
//    public void setAvailableSlots(List<LocalDateTime> availableSlots) {
//        this.availableSlots = availableSlots;
//    }
}
