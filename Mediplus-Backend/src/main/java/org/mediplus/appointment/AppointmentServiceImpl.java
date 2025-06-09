package org.mediplus.appointment;

import org.mediplus.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository apptRepo;

    public AppointmentServiceImpl(AppointmentRepository apptRepo) {
        this.apptRepo = apptRepo;
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return apptRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return apptRepo.findAll();
    }

    @Override
    public List<Appointment> getAppointmentsByUserId(Long userId) {
        // fetch all rows with the same userId
        return apptRepo.findAll()
                .stream()
                .filter(user -> user.getId().equals(userId))
                .toList();
    }
    @Override
    public List<Appointment> findByDoctorUsername(String doctorUsername) {
        return apptRepo.findByDoctorUsername(doctorUsername);
    }

    @Override
    public List<Appointment> findByPatientUsername(String patientUsername) {
        return apptRepo.findByPatientUsername(patientUsername);
    }

    @Override
    public Appointment createAppointment(Appointment appointment) {
        if (appointment.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create appointment in the past");
        }
        return apptRepo.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment appointment) {
        Appointment existing = apptRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        if (appointment.getDateTime() != null && appointment.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot reschedule appointment to a time in the past");
        }
        existing.setDateTime(appointment.getDateTime());
        existing.setDoctorUsername(appointment.getDoctorUsername());
        existing.setPatientUsername(appointment.getPatientUsername());
        existing.setStatus(appointment.getStatus());

        return apptRepo.save(existing);
    }

    @Override
    public Appointment updateAppointmentStatus(Long id, String status) {
        Appointment existing = apptRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
        existing.setStatus(status);
        return apptRepo.save(existing);
    }

    @Override
    public void deleteAppointment(Long id) {
        if (!apptRepo.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found with ID: " + id);
        }
        apptRepo.deleteById(id);
    }
}