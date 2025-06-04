package org.mediplus.doctor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String specialization;
    private String licenseNumber;
    private String clinicLocation;
    private String role;

    public DoctorResponseDTO() {}

    public DoctorResponseDTO(Long id, String username, String email,
                             String specialization, String licenseNumber,
                             String clinicLocation, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.clinicLocation = clinicLocation;
        this.role = role;
    }
}
