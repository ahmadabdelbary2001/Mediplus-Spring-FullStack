package org.mediplus;

import lombok.extern.slf4j.Slf4j;
import org.mediplus.admin.Admin;
import org.mediplus.doctor.Doctor;
import org.mediplus.patient.Patient;
import org.mediplus.user.User;
import org.mediplus.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createDemoPatientIfNotExists();
        createDemoDoctorIfNotExists();
        createDemoAdminIfNotExists();
    }

    private void createDemoAdminIfNotExists() {
        String demoUsername = "admin";
        Optional<User> existingAdmin = userRepository.findByUsername(demoUsername);
        if (existingAdmin.isEmpty()) {
            log.debug("Creating demo admin account");
            Admin demoAdmin = new Admin();
            demoAdmin.setUsername(demoUsername);
            demoAdmin.setEmail("admin@example.com");
            demoAdmin.setPassword(passwordEncoder.encode("admin123"));
            demoAdmin.setRole("ADMIN");
            demoAdmin.setTermsAccepted(true);
            userRepository.save(demoAdmin);
            log.info("Demo admin 'admin' created");
        }
    }

    private void createDemoPatientIfNotExists() {
        String demoUsername = "patient";
        Optional<User> existingPatient = userRepository.findByUsername(demoUsername);
        if (existingPatient.isEmpty()) {
            log.debug("Creating demo patient account");
            Patient demoPatient = new Patient();
            demoPatient.setUsername(demoUsername);
            demoPatient.setEmail("patient@example.com");
            demoPatient.setPassword(passwordEncoder.encode("patient"));
            demoPatient.setRole("PATIENT");
            demoPatient.setInsuranceId("INS-12345");
            try {
                Date dob = new SimpleDateFormat("yyyy-MM-dd").parse("1990-06-15");
                demoPatient.setDateOfBirth(dob);
            } catch (ParseException e) {
                throw new RuntimeException("Invalid demo DOB", e);
            }
            userRepository.save(demoPatient);
            log.info("Demo patient 'patient' created");
        }
    }

    private void createDemoDoctorIfNotExists() {
        String demoUsername = "doctor";
        Optional<User> existingDoctor = userRepository.findByUsername(demoUsername);
        if (existingDoctor.isEmpty()) {
            log.debug("Creating demo doctor account");
            Doctor demoDoc = new Doctor();
            demoDoc.setUsername(demoUsername);
            demoDoc.setEmail("doctor@example.com");
            demoDoc.setPassword(passwordEncoder.encode("doctor"));
            demoDoc.setRole("DOCTOR");
            demoDoc.setSpecialization("General Medicine");
            demoDoc.setLicenseNumber("LIC-0001");
            demoDoc.setClinicLocation("Main Clinic");
            userRepository.save(demoDoc);
            log.info("Demo doctor 'doctor' created");
        }
    }
}
