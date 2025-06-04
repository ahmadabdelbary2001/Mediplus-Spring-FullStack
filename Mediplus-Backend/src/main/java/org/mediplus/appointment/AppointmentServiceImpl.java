package org.mediplus.appointment;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository apptRepo;

    public AppointmentServiceImpl(AppointmentRepository apptRepo) {
        this.apptRepo = apptRepo;
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return apptRepo.findById(id).orElse(null);
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
        return apptRepo.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment appointment) {
        Appointment existing = apptRepo.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        return apptRepo.save(appointment);
    }

    @Override
    public Appointment updateAppointmentStatus(Long id, String status) {
        Appointment existing = apptRepo.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setStatus(status);
        return apptRepo.save(existing);
    }

    @Override
    public void deleteAppointment(Long id) {
        apptRepo.deleteById(id);
    }
}
