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

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository
            , PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        userRepository.save(demoPatient);
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
        userRepository.save(demoDoc);
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
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean authenticate(User user) {
        log.debug("Authenticating user: {}", user.getUsername());
        User existing = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (existing == null) {
            return false;
        }
        return passwordEncoder.matches(user.getPassword(), existing.getPassword());
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
