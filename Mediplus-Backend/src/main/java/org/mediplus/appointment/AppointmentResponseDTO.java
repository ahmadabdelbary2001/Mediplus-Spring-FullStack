package org.mediplus.appointment;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponseDTO {

    private Long id;
    private LocalDateTime dateTime;
    private String status;
    private String patientUsername;
    private String doctorUsername;

    public AppointmentResponseDTO() {
    }

    public AppointmentResponseDTO(Long id,
                                  LocalDateTime dateTime,
                                  String status,
                                  String patientUsername,
                                  String doctorUsername) {
        this.id = id;
        this.dateTime = dateTime;
        this.status = status;
        this.patientUsername = patientUsername;
        this.doctorUsername = doctorUsername;
    }
}
