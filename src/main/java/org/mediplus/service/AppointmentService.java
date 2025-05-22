package org.mediplus.service;

import org.mediplus.model.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Optional<Appointment> findById(int id);
    List<Appointment> findAll();
    List<Appointment> findByDoctorUsername(String doctorUsername);
    void add(Appointment appointment);
    void saveAppointment(Appointment appointment);
    void deleteById(int id);
    void updateStatus(int id, String status);
}
