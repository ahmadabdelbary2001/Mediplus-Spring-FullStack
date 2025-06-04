package org.mediplus.doctor;

import java.util.List;

public interface DoctorService {
    Doctor createDoctor(Doctor doctor);
    Doctor updateDoctor(Doctor doctor);
    Doctor getDoctorById(Long id);
    List<Doctor> findAllDoctors();
    void deleteDoctor(Long id);
}
