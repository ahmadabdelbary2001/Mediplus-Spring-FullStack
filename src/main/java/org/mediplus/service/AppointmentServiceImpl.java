package org.mediplus.service;

import org.mediplus.model.Appointment;
import org.mediplus.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository apptRepo;

    public AppointmentServiceImpl(AppointmentRepository apptRepo) {
        this.apptRepo = apptRepo;
    }

    @Override
    public Optional<Appointment> findById(int id) {
        return apptRepo.findById(id);
    }

    @Override
    public List<Appointment> findAll() {
        return apptRepo.findAll();
    }

    @Override
    public List<Appointment> findByDoctorUsername(String doctorUsername) {
        return apptRepo.findByDoctorUsername(doctorUsername);
    }

    @Override
    public void add(Appointment appointment) {
        apptRepo.save(appointment);
    }

    @Override
    public void saveAppointment(Appointment appointment) {
        apptRepo.save(appointment);
    }

    @Override
    public void deleteById(int id) {
        apptRepo.deleteById(id);
    }

    @Override
    public void updateStatus(int id, String status) {
        Optional<Appointment> opt = apptRepo.findById(id);
        opt.ifPresent(a -> {
            a.setStatus(status);
            apptRepo.save(a);
        });
    }
}
