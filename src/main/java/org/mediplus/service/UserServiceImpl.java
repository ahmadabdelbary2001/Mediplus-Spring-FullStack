package org.mediplus.service;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository
//            , PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
        log.info("Initializing User Service");
        initializeDemoData();
    }

    private void initializeDemoData() {
        // Only create demo data if users don't exist
        if (!existsByUsername("patient")) {
            createDemoPatient();
        }
        if (!existsByUsername("doctor")) {
            createDemoDoctor();
        }
    }

    private void createDemoPatient() {
        log.debug("Creating demo patient account");
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
        createPatient(demoPatient);
    }

    private void createDemoDoctor() {
        log.debug("Creating demo doctor account");
        Doctor demoDoc = new Doctor();
        demoDoc.setUsername("doctor");
        demoDoc.setEmail("doctor@example.com");
        demoDoc.setPassword("doctor");
        demoDoc.setRole("DOCTOR");
        demoDoc.setSpecialization("General Medicine");
        demoDoc.setLicenseNumber("LIC-0001");
        demoDoc.setClinicLocation("Main Clinic");
        createDoctor(demoDoc);
    }

    @Override
    public User createUser(User user) {
        log.info("Creating new user: {}", user.getUsername());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword(user.getPassword());
        return userRepository.save(user);
    }

    @Override
    public Patient createPatient(Patient patient) {
//        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient.setPassword(patient.getPassword());
        return userRepository.save(patient);
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
//        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctor.setPassword(doctor.getPassword());
        return userRepository.save(doctor);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public List<Doctor> findAllDoctors() {
        return userRepository.findAllDoctors();
    }

    @Override
    public Patient updatePatient(Patient patient) {
        return userRepository.save(patient);
    }

    @Override
    public Doctor updateDoctor(Doctor doctor) {
        return userRepository.save(doctor);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public boolean authenticate(User user) {
        log.debug("Authenticating user: {}", user.getUsername());
        User existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);

        if (existingUser == null) {
            return false;
        }

//        return passwordEncoder.matches(user.getPassword(), existingUser.getPassword());
        return existingUser.getPassword().equals(user.getPassword());
    }
}