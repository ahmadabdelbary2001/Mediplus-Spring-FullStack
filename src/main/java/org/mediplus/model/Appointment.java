package org.mediplus.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Appointment {
    private Long id;
    private LocalDateTime dateTime;
    private String notes;
    private String status;
    private Patient patient;
    private Doctor doctor;

    public Appointment(Long id, LocalDateTime dateTime, String notes, String status, Patient patient, Doctor doctor) {
        this.id = id;
        this.dateTime = dateTime;
        this.notes = notes;
        this.status = status;
        this.patient = patient;
        this.doctor = doctor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
