package org.mediplus.patient;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.mediplus.user.User;
import org.mediplus.user.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;

    public PatientController(PatientService patientService,
                             UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody Patient patient) {
        log.info("Registering new user: {}", patient.getUsername());
        try {
            patientService.createPatient(patient);
            log.info("User registered successfully: {}", patient.getUsername());
            return ResponseEntity.ok("User registered successfully");
        } catch (DataIntegrityViolationException e) {
            log.error("Registration failed - duplicate entry: {}", patient.getUsername());
            return ResponseEntity.status(409).body("Username or email already taken");
        } catch (Exception e) {
            log.error("Unexpected registration error: {}", e.getMessage());
            return ResponseEntity.status(500).body("Registration failed");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Patient> getCurrentPatient(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        User u = userService.getUserByUsername(principal.getName());
        if (u == null || !(u instanceof Patient)) {
            return ResponseEntity.status(404).build();
        }
        Patient p = (Patient) u;
        log.debug("Retrieved current patient: {}", p.getUsername());
        return ResponseEntity.ok(p);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> listAllPatients() {
        List<Patient> list = patientService.findAllPatients();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient created = patientService.createPatient(patient);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient) {
        patient.setId(id);
        Patient updated = patientService.updatePatient(patient);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}