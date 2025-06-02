package org.mediplus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name= "Appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private String status;

    @Column(name = "patient_username", nullable = false)
    private String patientUsername;

    @Column(name = "doctor_username", nullable = false)
    private String doctorUsername;

    public Appointment() {}

    public Appointment(Long id, LocalDateTime dateTime, String status, String patientUsername, String doctorUsername) {
        this.id = id;
        this.dateTime = dateTime;
        this.status = status;
        this.patientUsername = patientUsername;
        this.doctorUsername = doctorUsername;
    }

}
