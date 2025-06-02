package org.mediplus.service;

import org.mediplus.model.Doctor;
import org.mediplus.model.Patient;
import org.mediplus.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Patient createPatient(Patient patient);
    Doctor createDoctor(Doctor doctor);
    User getUserByUsername(String username);
    User getUserById(Long userId);
    List<Doctor> findAllDoctors();
    Patient updatePatient(Patient patient);
    Doctor updateDoctor(Doctor doctor);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteUser(Long userId);
    boolean authenticate(User user);
}