package org.mediplus.service;

import org.mediplus.model.Patient;
import org.mediplus.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final Map<String, User> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
//        Patient demoPatient = new Patient(
//                "patient",
//                "patient@example.com",
//                passwordEncoder.encode("patient"),
//                "INS-12345"
//        );
        Patient demoPatient = new Patient();
        demoPatient.setUsername("patient");
        demoPatient.setEmail("patient@example.com");
        demoPatient.setPassword("patient");
        demoPatient.setRole("PATIENT");
        demoPatient.setInsuranceId("INS-12345");
        registerUser(demoPatient);
        // users.put(user.getUsername(), user);
    }

    public void registerUser(Patient patient) {
        // Encode the password before saving
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        users.put(patient.getUsername(), patient);
    }

    public User findByUsername(String username) {
        return users.get(username);
    }

    public Map<String, User> getAllUsers() {
        return users;
    }
}