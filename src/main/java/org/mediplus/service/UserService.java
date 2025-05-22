package org.mediplus.service;

import org.mediplus.model.Doctor;
import org.mediplus.model.Patient;
import org.mediplus.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(User user);
    void registerPatient(Patient patient);
    void registerDoctor(Doctor doctor);
    Optional<User> findByUsername(String username);
    List<Doctor> findAllDoctors();
    void updatePatient(Patient patient);
    void updateDoctor(Doctor doctor);
}