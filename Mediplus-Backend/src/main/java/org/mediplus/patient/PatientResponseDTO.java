package org.mediplus.patient;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class PatientResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String insuranceId;
    private String role;
    private Date dateOfBirth;

    public PatientResponseDTO() { }

    public PatientResponseDTO(Long id,
                              String username,
                              String email,
                              String insuranceId,
                              String role,
                              Date dateOfBirth) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.insuranceId = insuranceId;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
    }
}
