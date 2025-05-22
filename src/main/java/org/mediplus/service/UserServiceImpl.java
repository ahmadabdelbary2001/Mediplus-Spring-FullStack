package org.mediplus.service;

import org.mediplus.model.Doctor;
import org.mediplus.model.Patient;
import org.mediplus.model.User;
import org.mediplus.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        // --- create demo patient ---
        Patient demoPatient = new Patient();
        demoPatient.setUsername("patient");
        demoPatient.setEmail("patient@example.com");
        demoPatient.setPassword("patient");
        demoPatient.setRole("PATIENT");
        demoPatient.setInsuranceId("INS-12345");
        try {
            Date dob = new SimpleDateFormat("yyyy-MM-dd").parse("1990-06-15");
            demoPatient.setDateOfBirth(dob);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid demo DOB", e);
        }
        registerUser(demoPatient);

        // --- create demo doctor ---
        Doctor demoDoc = new Doctor();
        demoDoc.setUsername("doctor");
        demoDoc.setEmail("doctor@example.com");
        demoDoc.setPassword("doctor");
        demoDoc.setRole("DOCTOR");
        demoDoc.setSpecialization("General Medicine");
        demoDoc.setLicenseNumber("LIC-0001");
        demoDoc.setClinicLocation("Main Clinic");
        registerUser(demoDoc);
    }

    @Override
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void registerPatient(Patient patient) {
        registerUser(patient);
    }

    @Override
    public void registerDoctor(Doctor doctor) {
        registerUser(doctor);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<Doctor> findAllDoctors() {
        return userRepository.findAllDoctors();
    }

    @Override
    public void updatePatient(Patient patient) {
        userRepository.save(patient);
    }

    @Override
    public void updateDoctor(Doctor doctor) {
        userRepository.save(doctor);
    }
}