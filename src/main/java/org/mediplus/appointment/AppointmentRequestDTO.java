package org.mediplus.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentRequestDTO {

    @NotNull(message = "dateTime is required")
    private LocalDateTime dateTime;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Patient username is required")
    private String patientUsername;

    @NotBlank(message = "Doctor username is required")
    private String doctorUsername;

    public AppointmentRequestDTO() {
    }
}
