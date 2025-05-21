package org.mediplus.service;

import org.mediplus.model.Appointment;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final List<Appointment> appointments = new ArrayList<>();

    public Optional<Appointment> findById(int id) {
        return appointments.stream()
                .filter(a -> a.getId() == id)
                .findFirst();
    }

    public List<Appointment> findAll() {
        return appointments;
    }

    public List<Appointment> findByDoctorUsername(String doctorUsername) {
        return appointments.stream()
                .filter(a -> a.getDoctorUsername().equals(doctorUsername))
                .collect(Collectors.toList());
    }

    public void add(Appointment appointment) {
        appointments.add(appointment);
    }

    public void saveAppointment(Appointment appointment) {
        findById(appointment.getId()).ifPresentOrElse(existing -> {
            existing.setDateTime(appointment.getDateTime());
            existing.setDoctorUsername(appointment.getDoctorUsername());
            existing.setPatientUsername(appointment.getPatientUsername());
            existing.setStatus(appointment.getStatus());
        }, () -> {
            appointments.add(appointment);
        });
    }

    public void deleteById(int id) {
        appointments.removeIf(a -> a.getId() == id);
    }

    public void updateStatus(int id, String status) {
        appointments.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .ifPresent(a -> a.setStatus(status));
    }
}