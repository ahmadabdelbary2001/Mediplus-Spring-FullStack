package org.mediplus.patient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String insuranceId;
    private String role;

    public PatientResponseDTO() {}

    public PatientResponseDTO(Long id, String username, String email, String insuranceId, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.insuranceId = insuranceId;
        this.role = role;
    }
}
