package org.mediplus.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Appointment {
    private int id;
    private LocalDateTime dateTime;
    private String status;
    private String patientUsername;
    private String doctorUsername;

    public Appointment() {}

    public Appointment(int id, LocalDateTime dateTime, String status, String patientUsername, String doctorUsername) {
        this.id = id;
        this.dateTime = dateTime;
        this.status = status;
        this.patientUsername = patientUsername;
        this.doctorUsername = doctorUsername;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPatientUsername() {
        return patientUsername;
    }

    public void setPatientUsername(String patientUsername) {
        this.patientUsername = patientUsername;
    }

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
    }
}
