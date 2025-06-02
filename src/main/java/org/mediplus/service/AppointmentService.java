package org.mediplus.service;

import org.mediplus.model.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment getAppointmentById(Long id);
    List<Appointment> getAllAppointments();
    List<Appointment> getAppointmentsByUserId(Long userId);
    List<Appointment> findByDoctorUsername(String doctorUsername);
    List<Appointment> findByPatientUsername(String patientUsername);
    Appointment createAppointment(Appointment appointment);
    Appointment updateAppointment(Long id, Appointment appointment);
    Appointment updateAppointmentStatus(Long id, String status);
    void deleteAppointment(Long id);
}
