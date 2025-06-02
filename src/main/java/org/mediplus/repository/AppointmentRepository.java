package org.mediplus.repository;

import org.mediplus.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorUsername(String doctorUsername);
    List<Appointment> findByPatientUsername(String patientUsername);
}